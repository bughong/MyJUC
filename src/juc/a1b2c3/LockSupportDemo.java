package juc.a1b2c3;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * @author HongTaiwei
 * @date 2021/8/5
 */
public class LockSupportDemo {
    static int[] numbers = new int[]{1,2,3,4,5};
    static char[] chars = new char[]{'A','B','C','D','E'};

    static Thread t1 = null;
    static Thread t2 = null;

    public static void main(String[] args) {

        t1 = new Thread(() -> {
            for (int i = 0; i < numbers.length; i++) {
                System.out.print(numbers[i]);
                LockSupport.unpark(t2);
                LockSupport.park();
            }
        });

        t2 = new Thread(()->{
            for (int i = 0; i < chars.length; i++) {
                LockSupport.park();
                System.out.print(chars[i]);
                LockSupport.unpark(t1);
            }
        });
        t1.start();
        t2.start();
    }
}
