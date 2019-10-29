package com.yx.leecode.other;

import java.util.Scanner;

/**
 * @author xufeng
 * Create Date: 2019-10-21 19:49
 **/
public class T1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            int x1 = scanner.nextInt();
            int y1 = scanner.nextInt();
            int x2 = scanner.nextInt();
            int y2 = scanner.nextInt();
            print(x1, y1, x2, y2);
        }
    }


    public static void print(int x1, int y1, int x2, int y2) {
        System.out.println((x1 + x2) / 2);
        System.out.println((y1 + y2) / 2);
    }


}
