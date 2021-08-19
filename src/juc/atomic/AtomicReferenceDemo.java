package juc.atomic;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author HongTaiwei
 * @date 2021/8/19
 */
public class AtomicReferenceDemo {

    private static AtomicReference<Integer> atomicReference = new AtomicReference();

    public static void main(String[] args) {
        //当前余额19,小于20就加20
        atomicReference.set(19);
        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                while(true) {
                    while(true) {
                        Integer balance = atomicReference.get();
                        if (atomicReference.get() < 20) {
                            if(atomicReference.compareAndSet(balance, balance + 20)){
                                System.out.println("用户充值20元,剩余余额:"+atomicReference.get());
                                break;
                            }
                        }
                    }
                }
            }).start();
        }
        new Thread(()->{
            for (int i = 0; i < 100; i++) {
                while(true){
                    Integer balance = atomicReference.get();
                    if(balance > 10){
                        if (atomicReference.compareAndSet(balance,balance-10)) {
                            System.out.println("消费后的剩余余额:"+atomicReference.get());
                            break;
                        }
                    }else{
                        break;
                    }
                    atomicReference.set(balance-20);
                }
            }
        }).start();

    }
}
