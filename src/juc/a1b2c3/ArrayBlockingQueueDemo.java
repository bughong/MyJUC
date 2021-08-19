package juc.a1b2c3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * @author HongTaiwei
 * @date 2021/8/5
 */
public class ArrayBlockingQueueDemo {
    static int[] numbers = new int[]{1,2,3,4,5};
    static char[] chars = new char[]{'A','B','C','D','E'};
    static char[] symbols = new char[]{'!','@','#','$','%'};

    static ArrayBlockingQueue queue1 = new ArrayBlockingQueue(1);
    static ArrayBlockingQueue queue2 = new ArrayBlockingQueue(1);
    static ArrayBlockingQueue queue3 = new ArrayBlockingQueue(1);

    public static void main(String[] args){

        new Thread(() -> {
            for (int i = 0; i < numbers.length; i++) {
                try {
                    queue1.put(numbers[i]);
                    System.out.print(queue2.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(()->{
            for (int i = 0; i < chars.length; i++) {
                try {
                    System.out.print(queue1.take());
                    queue3.put(chars[i]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(()->{
            for (int i = 0; i < symbols.length; i++) {
                try {
                    System.out.print(queue3.take());
                    queue2.put(symbols[i]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
