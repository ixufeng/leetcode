package com.yx.leecode;

/**
 * @author xufeng
 * Create Date: 2020-09-17 20:32
 **/
public class Demo {
    public static void main(String[] args) {
        String tar1 = "1233411";
        String tar2 = "56778";
        int len = tar2.length() > tar1.length() ? tar2.length() : tar1.length();

        int left = 0;
        String result = "";
        for(int i =0;i< len;i++) {
            char item1 = '0';
            char item2 = '0';
            if (i < tar1.length()) {
                item1 = tar1.charAt(i);
            }
            if (i < tar2.length()) {
                item2 = tar2.charAt(i);
            }
            int sum = (int)item2 + (int)item1 + left;
            System.out.println(sum);
            if(sum >= 10) {
                left = 1;
                result = (sum - 10) + result;
            }else {
                left = 0;
                result = sum + result;
            }
        }
        if(left == 1) {
            result = 1 + result;
        }

        System.out.print(result);
    }

    /**
     * 判断一个数是否是素数
     *
     * @param i
     * @return
     */
    private static boolean isPrime(int i) {
        for (int j = 2; j < Math.sqrt(i); j++) {
            if (i % j == 0) {
                return false;
            }
        }
        return true;
    }
}
