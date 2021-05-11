package com.yx.leecode;

/**
 * @author xufeng
 * Create Date: 2020-09-13 19:21
 **/
public class MaxCoins {

    public int maxCoins(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }

        return maxCoins4(nums, 0, nums.length - 1, 1, 1, new int[nums.length][nums.length]);
    }


    /**
     * @param nums
     * @param begin
     * @param end
     * @param leftVal  左边界的值，起初肯定是1
     * @param rightVal 右边界的值，起初也是1
     * @param cache
     * @return
     */
    public int maxCoins4(int[] nums, int begin, int end, int leftVal, int rightVal, int[][] cache) {
        if (begin > end) {
            return 0;
        }
        //最小分治单元
        if (begin == end) {
            return nums[begin] * leftVal * rightVal;
        }

        //缓存，避免重复计算
        if (cache[begin][end] != 0) {
            return cache[begin][end];
        }

        int max = 0;

        for (int lastNumIndex = begin; lastNumIndex <= end; lastNumIndex++) {
            //套公式
            int tempMax = maxCoins4(nums, begin, lastNumIndex - 1, leftVal, nums[lastNumIndex], cache)
                    + maxCoins4(nums, lastNumIndex + 1, end, nums[lastNumIndex], rightVal, cache)
                    + nums[lastNumIndex] * leftVal * rightVal;
            max = Math.max(max, tempMax);
        }

        cache[begin][end] = max;

        return max;
    }


}