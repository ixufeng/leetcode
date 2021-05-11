package com.yx.leecode;

/**
 * @author xufeng
 * Create Date: 2020-08-27 15:32
 * dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i]];
 **/
public class CanPartition {
    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (sum % 2 != 0) {
            return false;
        }

        int half = sum >> 1;




        return false;
    }
}
