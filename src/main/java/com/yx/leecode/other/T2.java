package com.yx.leecode.other;

import java.util.Scanner;

/**
 * @author xufeng
 * Create Date: 2019-10-21 20:04
 * 全排列
 **/
public class T2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int len = scanner.nextInt();
            int[] arr = new int[len];
            for (int i = 0; i < len; i++) {
                arr[i] = scanner.nextInt();
            }
            print(arr, 0);
        }
    }


    public static void print(int arr[], int start) {
        if (start == arr.length - 1) {
            for (int i : arr) {
                System.out.print(i);
                System.out.print("\t");
            }
            System.out.println();
        }
        for (int i = start; i <= arr.length - 1; i++) {

            Swap(arr, i, start);
            print(arr, start + 1);
            Swap(arr, i, start);
        }
    }

    public static void Swap(int chs[], int i, int j) {
        int temp;
        temp = chs[i];
        chs[i] = chs[j];
        chs[j] = temp;
    }
}
