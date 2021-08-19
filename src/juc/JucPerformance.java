package juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 *
 * AtomicLong对比LongAdder对比synchronized
 *
 * @author HongTaiwei
 * @date 2021/7/14
 */
public class JucPerformance {

    private static AtomicLong atomicLong = new AtomicLong();
    private static LongAdder longAdder = new LongAdder();

    private static CountDownLatch countDownLatch = new CountDownLatch(10000000);

    private static int synchronizedNumber = 0;
    private static int AtomicLongNumber = 0;
    private static int LongAdderNumber = 0;

    public static void main(String[] args) throws Exception {
//        synchronizedNumber();
        atomicLong();
    }

    public static void synchronizedNumber() throws Exception {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            new Thread(()->{
                synchronized (JucPerformance.class){
                    for (int j = 0; j < 10000; j++) {
                        synchronizedNumber++;
                        countDownLatch.countDown();
                    }
                }
            }).start();
        }
        countDownLatch.await();
        System.out.println(System.currentTimeMillis()-startTime);
    }

    public static void atomicLong() throws Exception {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    atomicLong.incrementAndGet();
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();
        System.out.println("atomicLong:"+synchronizedNumber+"  times:"+(System.currentTimeMillis()-startTime));
    }


}
