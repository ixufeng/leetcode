package com.yx.leecode.concurrent;


import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author xufeng
 * Create Date: 2019-11-21 17:24
 **/
public class FutureTaskTest {
    final static ExecutorService executor = Executors.newFixedThreadPool(1);

    public static void main(String[] args) throws Exception {
        System.out.println(Thread.currentThread().getName());

        FutureTask task = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName());
            return "ss";
        });
        executor.submit(task);

        Object o = task.get(1, TimeUnit.SECONDS);
        System.out.println(o);
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.peek();



    }
}
