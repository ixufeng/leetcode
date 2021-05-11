package com.yx.leecode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author xufeng
 * Create Date: 2020-06-07 20:21
 **/
public class CombinationSum {
    private int target;

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        this.target = target;
        List<List<Integer>> res = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        for (int index = 0; index < candidates.length; index++) {
            if (candidates[index] > target) {
                continue;
            }
            stack.push(candidates[index]);
            dfs(res, candidates, stack, candidates[index], index);
        }
        return res;
    }

    private void dfs(List<List<Integer>> res, int[] candidates, Stack<Integer> stack, int sum, int index) {
        if (sum > this.target) {
            return;
        }
        if (sum == this.target) {
            res.add(new ArrayList<>(stack));
            return;
        }

        for (int i = index; i < candidates.length; i++) {
            stack.push(candidates[i]);
            dfs(res, candidates, stack, sum + candidates[i], i);
            stack.pop();
        }
    }

    public static void main(String[] args) {
        int[] candidates = new int[]{2,3,6,7};
        CombinationSum combinationSum = new CombinationSum();
        combinationSum.combinationSum(candidates,7);

    }
}
