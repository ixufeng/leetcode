package com.yx.leecode;

/**
 * @author xufeng
 * Create Date: 2020-10-10 15:35
 **/
public class Test2 {
    public static void main(String[] args) {
        int n = 9 - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        System.out.println(n + 1);
    }
}
