package com.yx.leecode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xufeng
 * Create Date: 2020-08-25 20:02
 **/
public class RemoveInvalidParentheses {
    private static boolean isValid(String str) {
        if (str == null) {
            return true;
        }
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '(') {
                count++;
            }
            if (c == ')') {
                count--;
            }
            if (count < 0) {
                return false;
            }
        }
        return count == 0;
    }

    public static List<String> removeInvalidParentheses(String s) {
        Set<String> stringSet = new HashSet<>();
        stringSet.add(s);

        while (!stringSet.isEmpty()) {

            //遍历str
            List<String> result = new ArrayList<>();

            for (String strItem : stringSet) {
                if (isValid(strItem)) {
                    result.add(strItem);
                }
            }
            //终止
            if (!result.isEmpty()) {
                return result;
            }
            //移除一个字符
            Set<String> nextStringSet = new HashSet<>();

            for (String strItem : stringSet) {
                for (int i = 0; i < strItem.length(); i++) {
                    char cur = strItem.charAt(i);
                    if (cur == '(' || cur == ')') {
                        String next = strItem.substring(0, i) + strItem.substring(i + 1);
                        nextStringSet.add(next);
                    }
                }
            }
            stringSet = nextStringSet;

        }
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        List<String> list = removeInvalidParentheses("()(()((1)");
        for (String s : list) {
            System.out.println(s);
        }
    }

}
