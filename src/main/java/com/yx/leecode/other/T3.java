package com.yx.leecode.other;

import java.util.Scanner;

/**
 * @author xufeng
 * Create Date: 2019-10-21 19:56
 *
 **/
public class T3 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int num = scanner.nextInt();
            for (int i = 0; i < num; i++) {
                int x1 = scanner.nextInt();
                int x2 = scanner.nextInt();
                String result = isPrimeNumber(x1 * x1 - x2 * x2) ? "YES" : "NO";
                System.out.println(result);
            }
        }
    }

    public static boolean isPrimeNumber(int num) {
        if (num == 2) return true;
        if (num < 2 || num % 2 == 0) return false;
        for (int i = 3; i <= Math.sqrt(num); i += 2) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

}
