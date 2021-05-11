package com.yx.leecode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xufeng
 * Create Date: 2020-06-03 22:39
 **/
public class TestGC {

    public static String minWindow(String s, String t) {
        //左右闭合
        int from = 0;
        int end = 0;

        //记录每个字符及其数量
        Map<Character, Integer> tarCountMap = new HashMap<>();
        for (int i = 0; i < t.length(); i++) {
            tarCountMap.merge(t.charAt(i), 1, Integer::sum);
        }
        //有效的字符数量
        int validCount = 0;
        //计数
        Map<Character, Integer> countMap = new HashMap<>();
        if (tarCountMap.keySet().contains(s.charAt(0))) {
            countMap.merge(s.charAt(0), 1, Integer::sum);
            if (countMap.get(s.charAt(0)).equals(tarCountMap.get(s.charAt(0)))) {
                validCount++;
            }
        }

        int resultFrom = -1;
        int resultEnd = -1;
        int len = s.length();

        while (from <= end && end <= len) {
            //已经找到答案
            if (tarCountMap.values().size() == validCount) {
                if (resultFrom == -1) {
                    resultFrom = from;
                    resultEnd = end;
                } else if ((end - from) < (resultEnd - resultFrom)) {
                    resultFrom = from;
                    resultEnd = end;
                }
                //计数
                if (tarCountMap.keySet().contains(s.charAt(from))) {
                    countMap.merge(s.charAt(from), -1, Integer::sum);
                    if (countMap.get(s.charAt(from)) < tarCountMap.get(s.charAt(from))) {
                        --validCount;
                    }
                }
                from++;
                continue;
            }
            //没有完全包含
            end++;
            if (end < len) {
                char next = s.charAt(end);
                if (tarCountMap.keySet().contains(next)) {
                    countMap.merge(s.charAt(end), 1, Integer::sum);
                    if (countMap.get(next).equals(tarCountMap.get(next))) {
                        ++validCount;
                    }
                }
            }

        }
        if (resultEnd == -1) {
            return "";
        }
        return s.substring(resultFrom, resultEnd + 1);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(minWindow("ADOBECODEBANC", "ABC"));
    }
}
