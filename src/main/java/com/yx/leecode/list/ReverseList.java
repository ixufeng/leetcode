package com.yx.leecode.list;

/**
 * @author xufeng
 * Create Date: 2021/4/23 2:55 下午
 * 链表反转
 **/
public class ReverseList {

    static class Node {
        int val;
        Node next;

        public Node(int val) {
            this.val = val;
        }
    }

    /**
     * 单节点反转
     *
     * @param head
     * @return
     */
    public static Node reverse(Node head) {
        if (head == null) {
            return null;
        }
        if (head.next == null) {
            return head;
        }
        Node last = reverse(head.next);
        //反转
        head.next.next = head;
        head.next = null;
        //返回最后一个节点
        return last;
    }

    private static Node successor = null; // 后驱节点


    public static Node reverseN(Node head, int n) {
        if (head == null || head.next == null) {
            return head;
        }
        if (n == 1) {
            successor = head.next;
            return head;
        }
        Node last = reverseN(head.next, n - 1);
        head.next.next = head;
        head.next = successor;
        return last;
    }

    public static void printNode(Node head) {
        if (head == null) {
            return;
        }
        System.out.print(head.val + ",");
        printNode(head.next);
    }

    public static void main(String[] args) {
        Node head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(4);

        Node node = reverseN(head, 2);
        printNode(node);
    }

}
