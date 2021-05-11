package com.yx.leecode.list;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xufeng
 * Create Date: 2020-06-20 19:16
 **/
public class DetectCycle {


    public ListNode detectCycle(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode next = head;
        Map<ListNode, Integer> nodeMap = new HashMap<>();
        while (true) {
            nodeMap.merge(next, 1, Integer::sum);
            if (nodeMap.get(next) == 2) {
                return next;
            }
            if (next.next == null) {
                return null;
            }
            next = next.next;
        }

    }


    class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
            next = null;
        }
    }
}
