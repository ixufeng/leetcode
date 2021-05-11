package com.yx.leecode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xufeng
 * Create Date: 2020-09-15 20:24
 **/
public class PathSum {

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    private static int resultCount = 0;

    private static Set<TreeNode> beginSet = new HashSet<>();

    public static int pathSum(TreeNode root, int sum) {
        cal(root, sum, 0,new ArrayList<>());
        return resultCount;
    }

    public static void cal(TreeNode node, int sum, int curSum, List<TreeNode> trace) {
        if (node == null) {
            return;
        }

        if (!beginSet.contains(node)) {
            beginSet.add(node);
            //新的开始点
            cal(node.left, sum, 0, new ArrayList<>());
            cal(node.right, sum, 0, new ArrayList<>());
        }


        trace.add(node);

        if (curSum + node.val == sum) {
            resultCount++;
        }
        //继续向下延
        cal(node.left, sum, curSum+node.val,new ArrayList<>(trace));
        cal(node.right, sum, curSum+node.val,new ArrayList<>(trace));
    }

    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(1);
        treeNode.right = new TreeNode(2);
        treeNode.right.right = new TreeNode(3);
        TreeNode right3 = treeNode.right.right;
        right3.right = new TreeNode(4);
        right3.right.right = new TreeNode(5);

        System.out.println(pathSum(treeNode,3));



    }
}
