package com.yx.leecode;

import java.util.*;

/**
 * @author xufeng
 * Create Date: 2020-09-10 21:04
 **/
public class CombinationSum2 {

    private List<List<Integer>> result = new ArrayList<>();


    public List<List<Integer>> combinationSum2(int[] candidates, int target) {

        //排序
        Arrays.sort(candidates);

        Deque<Integer> trace = new ArrayDeque<>(candidates.length);

        help(trace, candidates, 0, target);
        return result;
    }

    private void help(Deque<Integer> trace, int[] candidates, int nextIndex, int left) {
        if (left < 0) {
            return;
        }
        if (left == 0) {
            result.add(new ArrayList<>(trace));
        }

        for (int i = nextIndex; i < candidates.length; i++) {
            // 小剪枝：同一层相同数值的结点，从第 2 个开始，候选数更少，结果一定发生重复，因此跳过，用 continue
            if (i > nextIndex && candidates[i] == candidates[i - 1]) {
                continue;
            }
            int val = candidates[i];
            trace.addLast(val);
            help(trace, candidates, i + 1, left - val);
            trace.removeLast();
        }

    }
}
