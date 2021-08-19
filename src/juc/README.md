### CyclicBarrier源码
####重要的方法、await()

-------------------------------------------
简单的实例，CyclicBarrier用法，可以指定等多少个线程一起干一个事情。类似于一个重要的会议，每一个人都必须要当场后才能进行会议。
```java
public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        //模拟只有到了5个人后才会进行开会
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> {
            System.out.println("开会中。。。。");
        });

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 1; i <= 20 ; i++) {
            executorService.execute(()->{
                try {
                    //没有满就等待
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
}
```
------------
构造方法CyclicBarrier(int parties, Runnable barrierAction)和
public CyclicBarrier(int parties)的区别在于前者指定了等待到达指定线程后要做的事情，后者是等待到到达指定线程后不做任何事情

```java
    public CyclicBarrier(int parties, Runnable barrierAction) {
        if (parties <= 0) throw new IllegalArgumentException();
        //需要等待的线程数
        this.parties = parties;
        this.count = parties;
        //等待完成后需要做的事
        this.barrierCommand = barrierAction;
    }
```

```java
    public int await() throws InterruptedException, BrokenBarrierException {
        try {
            return dowait(false, 0L);
        } catch (TimeoutException toe) {
            throw new Error(toe); // cannot happen
        }
    }
```


```java
private int dowait(boolean timed, long nanos)
        throws InterruptedException, BrokenBarrierException,
               TimeoutException {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            final Generation g = generation;

            if (g.broken)
                throw new BrokenBarrierException();

            if (Thread.interrupted()) {
                breakBarrier();
                throw new InterruptedException();
            }

            int index = --count;
            if (index == 0) {  // tripped
                boolean ranAction = false;
                try {
                    final Runnable command = barrierCommand;
                    if (command != null)
                        command.run();
                    ranAction = true;
                    nextGeneration();
                    return 0;
                } finally {
                    if (!ranAction)
                        breakBarrier();
                }
            }

            // loop until tripped, broken, interrupted, or timed out
            for (;;) {
                try {
                    if (!timed)
                        trip.await();
                    else if (nanos > 0L)
                        nanos = trip.awaitNanos(nanos);
                } catch (InterruptedException ie) {
                    if (g == generation && ! g.broken) {
                        breakBarrier();
                        throw ie;
                    } else {
                        // We're about to finish waiting even if we had not
                        // been interrupted, so this interrupt is deemed to
                        // "belong" to subsequent execution.
                        Thread.currentThread().interrupt();
                    }
                }

                if (g.broken)
                    throw new BrokenBarrierException();

                if (g != generation)
                    return index;

                if (timed && nanos <= 0L) {
                    breakBarrier();
                    throw new TimeoutException();
                }
            }
        } finally {
            lock.unlock();
        }
    }
```



