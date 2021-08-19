package juc;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author HongTaiwei
 * @date 2021/7/20
 */
public class SemaphoreDemo implements Runnable{

    static Semaphore semaphore = new Semaphore(1,true);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        SemaphoreDemo semaphoreDemo = new SemaphoreDemo();
        for (int i = 1; i <= 20 ; i++) {
            executorService.submit(semaphoreDemo);
        }

    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            Thread.sleep(1000);
            System.out.println("发车--------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }
}
