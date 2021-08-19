package juc;

import java.io.IOException;

/**
 * @author HongTaiwei
 * @date 2021/7/29
 */
public class ThreadLocalDemo {
    public static void main(String[] args) throws IOException {
        new Thread(()->{
            System.out.println(Thread.currentThread().getName());
            ThreadLocal threadLocal = new ThreadLocal();
            threadLocal.set(1);
            ThreadLocal threadLocal1 = new ThreadLocal();
            threadLocal1.set(2);
            ThreadLocal threadLocal2 = new ThreadLocal();
            threadLocal2.set(3);
            threadLocal = null;
            System.gc();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ThreadLocal threadLocal3 = new ThreadLocal();
            threadLocal3.set(4);
        }).start();

        new Thread(()->{
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println(threadLocal.get());
        }).start();
        System.in.read();
    }
}
