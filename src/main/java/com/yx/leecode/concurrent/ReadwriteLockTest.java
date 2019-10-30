package com.yx.leecode.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author xufeng
 * Create Date: 2019-10-30 10:16
 **/
public class ReadwriteLockTest {

    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private static ReentrantReadWriteLock.ReadLock readLock = lock.readLock();

    private static ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(new ReadTask(readLock, i)).start();
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception e) {
            }
            if (i == 2) {
                new Thread(new WriteTask(writeLock)).start();
            }

        }
    }


    static class ReadTask implements Runnable {

        private ReentrantReadWriteLock.ReadLock readLock;
        private Integer index;

        public ReadTask(ReentrantReadWriteLock.ReadLock readLock, Integer integer) {
            this.readLock = readLock;
            this.index = integer;
        }

        @Override
        public void run() {
            readLock.lock();
            try {
                System.out.println("get read lock one seconds,index:" + index);
                TimeUnit.SECONDS.sleep(100);

            } catch (Exception e) {
            } finally {
                readLock.unlock();
            }


        }
    }

    static class WriteTask implements Runnable {

        private ReentrantReadWriteLock.WriteLock writeLock;

        public WriteTask(ReentrantReadWriteLock.WriteLock writeLock) {
            this.writeLock = writeLock;
        }

        @Override
        public void run() {
            writeLock.lock();
            try {
                System.out.println("get read lock");
                TimeUnit.SECONDS.sleep(1000);
            } catch (Exception e) {
            } finally {
                writeLock.unlock();
            }


        }
    }

}
