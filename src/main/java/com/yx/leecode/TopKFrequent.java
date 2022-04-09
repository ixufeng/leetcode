package com.yx.leecode;

import java.util.*;

/**
 * @author xufeng
 * Create Date: 2020-08-23 18:07
 **/
public class TopKFrequent {

    public static int[] topKFrequent(int[] nums, int k) {
        List<Integer> res = new ArrayList<>();
        // 使用字典，统计每个元素出现的次数，元素为键，元素出现的次数为值
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.merge(num, 1, Integer::sum);
        }

        //桶排序
        //将频率作为数组下标，对于出现频率不同的数字集合，存入对应的数组下标
        List[] list = new List[nums.length + 1];
        for (int key : map.keySet()) {
            // 获取出现的次数作为下标
            int i = map.get(key);
            if (list[i] == null) {
                list[i] = new ArrayList();
            }
            list[i].add(key);
        }

        // 倒序遍历数组获取出现顺序从大到小的排列
        for (int i = list.length - 1; i >= 0 && res.size() < k; i--) {
            if (list[i] == null) {
                continue;
            }
            res.addAll(list[i]);
        }
        int[] arr = new int[res.size()];
        for (int i = 0; i < res.size(); i++) {
            arr[i] = res.get(i);
        }
        return arr;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{1, 2};
        topKFrequent(arr, 2);
    }


}
