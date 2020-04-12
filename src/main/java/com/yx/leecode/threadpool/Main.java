package com.yx.leecode.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author xufeng
 * Create Date: 2020-03-18 23:40
 **/
public class Main {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1), (task)->{
            Thread t = new Thread(task);
//            t.setDaemon(true);
            return t;
        }, (task, executor) -> {
            System.out.println("reject task!");
        });

        threadPoolExecutor.submit(() -> System.out.println("hello thread pool!!!"));
        threadPoolExecutor.shutdown();

    }
}
