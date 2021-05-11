package com.yx.leecode.ecache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;

import java.io.Serializable;

/**
 * @author xufeng
 * Create Date: 2020-06-15 23:45
 **/
public class App {

    static class User implements Serializable {
        String name;
        Integer age;

        public User(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    public static void main(String[] args) {
        CacheManager cacheManager
                = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("preConfigured",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                Integer.class,
                                User.class,
                                ResourcePoolsBuilder.heap(10).offheap(10, MemoryUnit.MB)
                        )
                )
                .build();

        cacheManager.init();



        Cache<Integer, User> preConfigured =
                cacheManager.getCache("preConfigured", Integer.class, User.class);

        for (int i = 0; i < 100; i++) {
            preConfigured.put(1, new User("test", 12));
        }


        System.out.println(preConfigured.get(1));


    }
}
