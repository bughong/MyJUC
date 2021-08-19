package juc.atomic;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author HongTaiwei
 * @date 2021/8/19
 */
public class AtomicIntegerFieldUpdaterDemo {
    public static class Candidate{
        int id;
        volatile int score;

        @Override
        public String toString() {
            return "Candidate{" +
                    "id=" + id +
                    ", score=" + score +
                    '}';
        }
    }

    private static AtomicIntegerFieldUpdater atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(Candidate.class,"score");
    private static AtomicInteger totalScore = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        final Candidate candidate = new Candidate();
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < 1000 ; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    if(Math.random() > 0.4){
                        atomicIntegerFieldUpdater.incrementAndGet(candidate);
                        totalScore.incrementAndGet();
                    }
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < 1000; i++) {
            threads[i].join();
        }
        System.out.println(candidate);
        System.out.println(totalScore.get());
    }

}
