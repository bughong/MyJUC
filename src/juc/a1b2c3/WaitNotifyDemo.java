package juc.a1b2c3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

/**
 * @author HongTaiwei
 * @date 2021/8/6
 */
public class WaitNotifyDemo {
    static int[] numbers = new int[]{1,2,3,4,5};
    static char[] chars = new char[]{'A','B','C','D','E'};
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    static final Object o = new Object();

    public static void main(String[] args) {
        WaitNotifyDemo waitNotifyDemo = new WaitNotifyDemo();
        waitNotifyDemo.runThread(waitNotifyDemo);
    }

    public void runThread(WaitNotifyDemo w){
        new Thread(() -> {
            synchronized (o) {
                countDownLatch.countDown();
                for (int i = 0; i < numbers.length; i++) {
                    System.out.println(numbers[i]);
                    o.notify();
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        
        new Thread(()->{
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (o) {
                for (int i = 0; i < chars.length; i++) {
                    System.out.println(chars[i]);
                    o.notify();
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                o.notify();
            }
        }).start();





    }
}
