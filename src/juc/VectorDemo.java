package juc;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author HongTaiwei
 * @date 2021/8/4
 * 售票模拟
 */
public class VectorDemo {

    static Vector vector = new Vector();

    static ConcurrentLinkedQueue concurrentLinkedQueue = new ConcurrentLinkedQueue();

    static {
        for (int i = 1; i <= 1000; i++) {
            concurrentLinkedQueue.add(i);
            vector.add(i);
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
//        while(true) {
//            executorService.execute(() -> {
//                if (vector.size() != 0) {
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//
//                    }
//                    System.out.println(vector.remove(0));
//                }
//            });
//        }

        while(true) {
            executorService.execute(()->{
                Object poll = concurrentLinkedQueue.poll();
                if(poll != null){
                    System.out.println(poll);
                }
            });
        }
    }
}
