package com.yx.leecode.concurrent;

import java.util.concurrent.Semaphore;

/**
 * @author xufeng
 * Create Date: 2020-09-28 15:01
 **/
public class SemaphoreTest {

    public static void main(String[] args) {

        Semaphore semaphore = new Semaphore(10);

        try {
            semaphore.acquire(1);
        } catch (Exception e) {

        }

        System.out.println(semaphore);


    }
}
