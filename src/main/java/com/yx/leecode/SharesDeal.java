package com.yx.leecode;

/**
 * @author xufeng
 * Create Date: 2019-11-21 14:33
 **/
public class SharesDeal {

    public static void sortColors(int[] nums) {
        int left = -1; //0的边界，包括
        int right = nums.length; //2的边界，包括

        for(int cur = 0;cur< right;cur++) {
            int curVal = nums[cur];
            if(curVal == 0) {
                left++;
                exchange(left,cur,nums);
            }else if(curVal == 2) {
                right--;
                exchange(right,cur,nums);
            }
            if(nums[cur] != 1 && cur > left) {
                cur--;
            }
        }
    }

    public static void exchange(int from,int to,int[] nums) {
        int temp = nums[from];
        nums[from] = nums[to];
        nums[to] = temp;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{2,0,2,1,1,0};
        sortColors(nums);
        System.out.println();
    }
}
