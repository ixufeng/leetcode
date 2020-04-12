package com.yx.leecode;

/**
 * @author xufeng
 * Create Date: 2019-11-21 14:33
 **/
public class SharesDeal {

    public int maxProfit(int[] prices) {
        int min = Integer.MAX_VALUE;
        int max = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] < min) {
                min = prices[i];
            } else {
                max = Math.max(max, prices[i] - min);
            }
        }
        return max;

    }
}
