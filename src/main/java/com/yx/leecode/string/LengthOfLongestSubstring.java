package com.yx.leecode.string;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author xufeng
 * Create Date: 2019-10-17 19:42
 **/
public class LengthOfLongestSubstring {

    public static int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        Set<Character> characters = new HashSet<>();
        int maxLen = 0;
        int front = 0;
        int end = 0;
        while (end < s.length()) {
            Character c = s.charAt(end);
            if (characters.contains(c)) {
                Character first = s.charAt(front);
                //remove the  same item
                while (!Objects.equals(first, c) && front <= end) {
                    characters.remove(first);
                    front++;
                    first = s.charAt(front);
                }
                front++;
            } else {
                characters.add(c);
                maxLen = Math.max(characters.size(), maxLen);
            }
            end++;
        }
        return maxLen;
    }

    //todo 优化set


    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring("pwwkew"));
    }
}
