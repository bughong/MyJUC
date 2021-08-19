package juc.atomic;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author HongTaiwei
 * @date 2021/8/19
 */
public class AtomicIntegerArrayDemo {
    private static AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10 ; i++) {
            int k = i;
            executorService.execute(()->{
                for (int j = 0; j < 1000 ; j++) {
                    atomicIntegerArray.incrementAndGet(k %atomicIntegerArray.length());
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
        System.out.println(atomicIntegerArray.toString());
    }
}
