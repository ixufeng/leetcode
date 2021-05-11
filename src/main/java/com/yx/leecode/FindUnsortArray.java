package com.yx.leecode;

/**
 * @author xufeng
 * Create Date: 2020-07-26 20:57
 **/
public class FindUnsortArray {

    public static void main(String[] args) {
        int[] nums = new int[]{2, 6, 4, 8, 10, 9, 15};
        int from = -1;
        int end = -1;
        int len = nums.length;
        int max = nums[0];

        for (int i = 0; i < len; i++) {
            int c = nums[i];
            max = Math.max(c, max);
            if (c < max) {
                //不合法
                if (from == -1) {
                    from = i;
                } else {
                    end = i;
                }
            }
        }

    }
}
