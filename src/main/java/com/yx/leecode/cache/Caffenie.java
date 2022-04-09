package com.yx.leecode.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author xufeng
 * Create Date: 2020-09-25 15:29
 **/
public class Caffenie {
    /**
     * 闪购皮肤商品本地缓存
     * 简单的超时策略
     */
    public static Cache<String, Integer> FLASH_GOODS_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .recordStats()
            .maximumSize(100)
            .build();

    public final static Integer NULL_OBJ = Integer.MAX_VALUE;

    public static void main(String[] args) {

        for (int i = 0; i < 101; i++) {
            FLASH_GOODS_CACHE.put(String.valueOf(i), i);
        }


        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        List<String> items = new ArrayList<>();
        items.add("100");
        items.add("99");
        items.add("98");
        items.add("918");
        items.add("928");
        items.add("938");
        FLASH_GOODS_CACHE.put("938", 1);

        Map<String, Integer> all = FLASH_GOODS_CACHE.getAll(items, (missList) -> {
            HashMap<String, Integer> missMap = new HashMap<>();
            for (String s : missList) {
                missMap.put(s, NULL_OBJ);
            }
            return missMap;
        });

        all.values().stream().filter(item -> item != NULL_OBJ).forEach(System.out::println);


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
