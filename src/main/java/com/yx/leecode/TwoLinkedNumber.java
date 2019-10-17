package com.yx.leecode;

import java.util.List;

/**
 * @author xufeng
 * Create Date: 2019-10-17 18:30
 *
 * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
 *
 * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
 *
 * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 *
 * 示例：
 *
 * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
 * 输出：7 -> 0 -> 8
 * 原因：342 + 465 = 807
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/add-two-numbers
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 **/
public class TwoLinkedNumber {

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode resultNode = null;
        ListNode cNode = null;
        ListNode c1 = l1;
        ListNode c2 = l2;
        int lastLeft = 0;
        while (true) {
            if (c1 == null && c2 == null) {
                break;
            }
            int v1 = c1 == null ? 0 : c1.val;
            int v2 = c2 == null ? 0 : c2.val;
            int sum = v1 + v2 + lastLeft;
            if (sum >= 10) {
                lastLeft = 1;
                sum = sum - 10;
            } else {
                lastLeft = 0;
            }
            ListNode node = new ListNode(sum);
            if (cNode == null) {
                resultNode = node;
                cNode = node;
            } else {
                cNode.next = node;
                cNode = node;
            }
            if (c1 != null) {
                c1 = c1.next;
            }
            if (c2 != null) {
                c2 = c2.next;
            }
        }
        if (lastLeft != 0) {
            cNode.next = new ListNode(1);
        }
        return resultNode;
    }

}
