package com.yx.leecode.threadpool;

/**
 * @author xufeng
 * Create Date: 2020-03-29 18:12
 **/
public class InterruptTest {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        System.out.println(thread.getState());
        thread.interrupt();
        System.out.println(thread.getState());

        synchronized (thread) {
            System.out.println("get thread lock");
            thread.wait();
        }

        System.out.println("thread wait");


        thread.interrupt();

        System.out.println(thread.getState());
    }
}
