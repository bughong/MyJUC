package juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author HongTaiwei
 * @date 2021/7/20
 */
public class ReadWirteLock {

    static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    static Lock readLock = readWriteLock.readLock();

    static Lock writeLock = readWriteLock.writeLock();



    public static void main(String[] args) {
        readLock.lock();

    }
}
