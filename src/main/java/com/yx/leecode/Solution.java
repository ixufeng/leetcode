package com.yx.leecode;

import java.util.ArrayList;
import java.util.List;

class Solution {
    public static boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        List<TreeNode> list = new ArrayList<>();
        list.add(root.left);
        list.add(root.right);
        return help(list);
    }

    private static boolean help(List<TreeNode> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        if (!isSym(list)) {
            return false;
        }

        List<TreeNode> newList = new ArrayList<>();
        boolean next = false;
        for (TreeNode node : list) {
            if (node != null) {
                next = true;
                newList.add(node.left);
                newList.add(node.right);
            } else {
                newList.add(null);
                newList.add(null);
            }
        }
        if (next) {
            return help(newList);
        }
        return true;
    }

    private static boolean isSym(List<TreeNode> list) {
        if (list.isEmpty()) {
            return true;
        }
        int from = 0;
        int end = list.size() - 1;
        while (from < end) {

            TreeNode n1 = list.get(from);
            TreeNode n2 = list.get(end);
            if (n1 != null && n2 != null) {
                if (n1.val != n2.val) {
                    return false;
                }
            }
            if(n1 == null && n2 != null) {
                return false;
            }

            if(n1 != null && n2 == null) {
                return false;
            }
            from++;
            end--;
        }
        return true;
    }

    public static void main(String[] args) {
        TreeNode node = new TreeNode(2);
        node.left = new TreeNode(3);
        node.right = new TreeNode(3);
        node.left.left = new TreeNode(4);
        node.left.right = new TreeNode(5);

        node.right.left = new TreeNode(5);

        System.out.println(isSymmetric(node));

    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int x) {
        val = x;
    }
}

