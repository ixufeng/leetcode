package com.yx.leecode.aqs;

/**
 * @author xufeng
 * Create Date: 2020-05-23 19:02
 **/
public class Application {
    private static final Lock lock = new ReentrantLock(true);

    public static void main(String[] args) throws Exception {




        lock.lock();

        Thread thread1 = new Thread(() -> {
            try {
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

            lock.unlock();
        });
        thread1.setName("test1");
        thread1.start();

        new Thread(() -> {
            lock.lock();
            lock.unlock();
        }).start();

        new Thread(() -> {
            lock.lock();
            lock.unlock();
        }).start();

        new Thread(() -> {
            lock.lock();
            lock.unlock();
        }).start();

        new Thread(() -> {
            lock.lock();
            lock.unlock();
        }).start();


        Thread.sleep(100);

        System.out.println(123);

        Thread.sleep(10000);

        lock.unlock();

        Thread.currentThread().join();
    }
}
