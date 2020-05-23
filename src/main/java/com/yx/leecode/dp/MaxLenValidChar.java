package com.yx.leecode.dp;


import java.util.Stack;

/**
 * @author xufeng
 * Create Date: 2020-05-10 17:57
 **/
public class MaxLenValidChar {

    public int longestValidParentheses(String s) {
        int maxL = 0;
        if (s == null || s.length() == 0) {
            return 0;
        }
        int len = s.length();
        Stack<Integer> stack = new Stack<>();
        //-1相当于插入一个标示，标志者连续括号的开始
        //如果遇到弹出后为空的情况，说明 1.标示被弹出，2. 连续有效的括号一定被打断，需要将当前的index插入作为标示
        stack.push(-1);
        for (int i = 0; i < len; i++) {
            if ('(' == s.charAt(i)) {
                stack.push(i);
            } else {
                stack.pop();
                if (stack.isEmpty()) {
                    stack.push(i);
                }else {
                    maxL = Math.max(maxL,i - stack.peek());
                }

            }
        }
        return maxL;

    }
}
