package juc.a1b2c3;

import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * @author HongTaiwei
 * @date 2021/8/4
 */
public class TransferQueueDemo {

    static int[] numbers = new int[]{1,2,3,4,5};
    static char[] chars = new char[]{'A','B','C','D','E'};

    public static void main(String[] args) {

        TransferQueue transferQueue = new LinkedTransferQueue();
        new Thread(()->{
            for (int i = 0; i < numbers.length; i++) {
                try {
                    transferQueue.transfer(numbers[i]);
                    System.out.print(transferQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(()->{
            for (int i = 0; i < chars.length; i++) {
                try {
                    System.out.print(transferQueue.take());
                    transferQueue.transfer(chars[i]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
