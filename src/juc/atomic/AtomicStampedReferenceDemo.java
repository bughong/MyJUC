package juc.atomic;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author HongTaiwei
 * @date 2021/8/19
 */
public class AtomicStampedReferenceDemo {

    private static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference(19, 0);

    public static void main(String[] args) {
        //当前余额19,小于20就加20
        for (int i = 0; i < 3; i++) {
            int stamp = atomicStampedReference.getStamp();
            new Thread(()->{
                while(true) {
                    while(true) {
                        Integer balance = atomicStampedReference.getReference();
                        if (balance < 20) {
                            if(atomicStampedReference.compareAndSet(balance, balance + 20,stamp,stamp+1)){
                                System.out.println("用户充值20元,剩余余额:"+atomicStampedReference.getReference());
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
                    Integer balance = atomicStampedReference.getReference();
                    int stamp = atomicStampedReference.getStamp();
                    if(balance > 10){
                        if (atomicStampedReference.compareAndSet(balance,balance-10,stamp,stamp+1)) {
                            System.out.println("消费后的剩余余额:"+atomicStampedReference.getReference());
                            break;
                        }
                    }else{
                        break;
                    }
                }
            }
        }).start();
    }
}
