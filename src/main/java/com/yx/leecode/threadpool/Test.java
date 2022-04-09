package com.yx.leecode.threadpool;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author xufeng
 * Create Date: 2021/7/28 10:18 上午
 **/
public class Test {

    public static void main(String[] args) throws Exception {

        ConcurrentMap<String, Object> result = new ConcurrentHashMap<>(4);
        CompletableFuture<Object> future1 = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("self exception");
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("self exception2");
        });
        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(300);
                result.put("3", "result3");
                return 1;
            } catch (Exception e) {
                return null;
            }
        });

        CompletableFuture.allOf(future1, future2, future3).whenCompleteAsync((v, t) -> {
            System.out.println("result1 " + result.get("1"));
            System.out.println("result2 " + result.get("2"));
            System.out.println("result3 " + result.get("3"));
        }).exceptionally(e -> {
            System.out.println("come ex");
            e.printStackTrace();
            return null;
        }).get(1000, TimeUnit.MILLISECONDS);

        try {
            Thread.currentThread().join();
        } catch (Exception e) {

        }
    }
}
