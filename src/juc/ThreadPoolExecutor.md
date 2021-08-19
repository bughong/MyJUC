### ThreadPoolExecutor源码
####重要的方法、await()

-------------------------------------------
主要的字段，其中ctl一共32位其中高3位表示线程状态，状态一共5个，3位可以表示8个状态，但是如果用2位只能表示
4个状态，所以必须使用3位；低29位表示的时线程数量。

```java
    //11100000 00000000 00000000 00000000和0按位与所以还是得到11100000 00000000 00000000 00000000
    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

    //Integer.SIZE为32,所以COUNT_BITS = 29
    private static final int COUNT_BITS = Integer.SIZE - 3;
    
    //(1 << COUNT_BITS)得到00100000 00000000 00000000 00000000再-1
    //00011111 11111111 11111111 11111111
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    // -1二进制为11111111 11111111 11111111 11111111左移29位后
    // 的二进制为11100000 00000000 00000000 00000000 
    private static final int RUNNING    = -1 << COUNT_BITS;
    
    // 左移29为后00000000 00000000 00000000 00000000
    private static final int SHUTDOWN   =  0 << COUNT_BITS;

    // 左移29为后00100000 00000000 00000000 00000000
    private static final int STOP       =  1 << COUNT_BITS;
    
    // 左移29为后01000000 00000000 00000000 00000000
    private static final int TIDYING    =  2 << COUNT_BITS;

    // 左移29为后01100000 00000000 00000000 00000000
    private static final int TERMINATED =  3 << COUNT_BITS;
```

几个重要的方法


workerCountOf方法源码
c是ctl其中CAPACITY为00011111 11111111 11111111 11111111通过&得到低29位也即是线程数量
```java
private static int workerCountOf(int c)  { return c & CAPACITY; }

```

isRunning源码
```java
private static boolean isRunning(int c) {
    //c如果小于SHUTDOWN那么就是Running状态
    return c < SHUTDOWN;
}
```

execute方法源码
```java
public void execute(Runnable command) {
        if (command == null)
            throw new NullPointerException();
        /*
         * Proceed in 3 steps:
         *
         * 1. If fewer than corePoolSize threads are running, try to
         * start a new thread with the given command as its first
         * task.  The call to addWorker atomically checks runState and
         * workerCount, and so prevents false alarms that would add
         * threads when it shouldn't, by returning false.
         *
         * 2. If a task can be successfully queued, then we still need
         * to double-check whether we should have added a thread
         * (because existing ones died since last checking) or that
         * the pool shut down since entry into this method. So we
         * recheck state and if necessary roll back the enqueuing if
         * stopped, or start a new thread if there are none.
         *
         * 3. If we cannot queue task, then we try to add a new
         * thread.  If it fails, we know we are shut down or saturated
         * and so reject the task.
         */
        //拿到ctl
        int c = ctl.get();
        //workerCountOf方法是获取当前工作线程数量，当工作线程小于核心线程数添加核心线程
        if (workerCountOf(c) < corePoolSize) {
            //添加核心线程，如果成功直接return，失败说明核心线程数满则重新获取ctl
            if (addWorker(command, true))
                return;
            c = ctl.get();
        }
        //如果是running状态，同时添加任务到队列中成功
        if (isRunning(c) && workQueue.offer(command)) {
            //再次拿到ctl进行检测
            int recheck = ctl.get();
            //如果不是running状态，那么删除调刚刚加入队列中的任务,同时执行拒绝策略
            if (! isRunning(recheck) && remove(command))
                reject(command);
            //如果不是
            else if (workerCountOf(recheck) == 0)
                addWorker(null, false);
        }
        //加入到任务队列，如果任务队列满了，执行拒绝策略
        else if (!addWorker(command, false))
            reject(command);
    }

```


addWorker源码

```java
private boolean addWorker(Runnable firstTask, boolean core) {
        retry:
        for (;;) {
            int c = ctl.get();
            int rs = runStateOf(c);

            // Check if queue empty only if necessary.
            if (rs >= SHUTDOWN &&
                ! (rs == SHUTDOWN &&
                   firstTask == null &&
                   ! workQueue.isEmpty()))
                return false;

            for (;;) {
                int wc = workerCountOf(c);
                if (wc >= CAPACITY ||
                    wc >= (core ? corePoolSize : maximumPoolSize))
                    return false;
                if (compareAndIncrementWorkerCount(c))
                    break retry;
                c = ctl.get();  // Re-read ctl
                if (runStateOf(c) != rs)
                    continue retry;
                // else CAS failed due to workerCount change; retry inner loop
            }
        }

        boolean workerStarted = false;
        boolean workerAdded = false;
        Worker w = null;
        try {
            w = new Worker(firstTask);
            final Thread t = w.thread;
            if (t != null) {
                final ReentrantLock mainLock = this.mainLock;
                mainLock.lock();
                try {
                    // Recheck while holding lock.
                    // Back out on ThreadFactory failure or if
                    // shut down before lock acquired.
                    int rs = runStateOf(ctl.get());

                    if (rs < SHUTDOWN ||
                        (rs == SHUTDOWN && firstTask == null)) {
                        if (t.isAlive()) // precheck that t is startable
                            throw new IllegalThreadStateException();
                        workers.add(w);
                        int s = workers.size();
                        if (s > largestPoolSize)
                            largestPoolSize = s;
                        workerAdded = true;
                    }
                } finally {
                    mainLock.unlock();
                }
                if (workerAdded) {
                    t.start();
                    workerStarted = true;
                }
            }
        } finally {
            if (! workerStarted)
                addWorkerFailed(w);
        }
        return workerStarted;
    }
```
