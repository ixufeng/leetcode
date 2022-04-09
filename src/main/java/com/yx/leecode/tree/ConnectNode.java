package com.yx.leecode.tree;

/**
 * @author xufeng
 * Create Date: 2021/5/13 11:55 下午
 **/
public class ConnectNode {

    static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {
        }

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    }

    public static Node connect(Node root) {
        if (root == null || root.right == null) {
            return root;
        }

        root.left.next = root.right;
        help(root.left, root.right);
        return root;
    }

    public static void help(Node left, Node right) {
        if (left == null || right == null) {
            return;
        }
        Node left_left = left.left;
        Node left_right = left.right;
        Node right_left = right.left;
        Node right_right = right.right;
        if (left_left == null) {
            return;
        }
        //connect
        left_left.next = left_right;
        left_right.next = right.left;
        right_left.next = right.right;

        //递归
        help(left_left, left_right);
        help(left_right, right_left);
        help(right_left, right_right);
    }

    public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        connect(root);
    }


}
