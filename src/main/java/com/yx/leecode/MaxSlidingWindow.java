package com.yx.leecode;

import java.util.*;

/**
 * @author xufeng
 * Create Date: 2020-08-18 21:16
 **/
public class MaxSlidingWindow {

    public static int[] maxSlidingWindow(int[] nums, int k) {
        Deque<Integer> queue = new ArrayDeque<>();
        Deque<Integer> maxQueue = new ArrayDeque<>();
        int count = 0;
        List<Integer> result = new ArrayList<>();
        int maxQueueItem;

        for (int num : nums) {

            //新增元素
            queue.add(num);
            count++;

            //存最大值1
            Integer newFirst = maxQueue.peekFirst();
            if (newFirst == null || num >= newFirst) {
                maxQueue.add(num);
            }

            //容量未满
            if (count < k) {


            } else {
                //count == k
                //获取最大值
                Integer nowMax = maxQueue.peekLast();
                result.add(nowMax);


                Integer take = queue.poll();
                Integer peekFirst = maxQueue.peekFirst();
                if (Objects.equals(take, peekFirst)) {
                    maxQueue.poll();
                }


                count--;

            }


        }

        int[] arr = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            arr[i] = result.get(i);
        }
        return arr;

    }

    //[1,3,1,2,0,5]

    public static void main(String[] args) {
        int[] nums = new int[]{1, 3, 1, 2, 0, 5};
        int[] ints = maxSlidingWindow(nums, 3);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }
}
