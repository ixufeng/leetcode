package com.yx.leecode;

import java.util.*;

/**
 * @author xufeng
 * Create Date: 2020-08-22 23:22
 **/
public class CodeC {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public StringBuilder rserialize(TreeNode root, StringBuilder str) {
        if (root == null) {
            str.append("None,");
        } else {
            str.append(root.val).append(",");
            str = rserialize(root.left, str);
            str = rserialize(root.right, str);
        }
        return str;
    }

    public String serialize(TreeNode root) {
        StringBuilder builder = new StringBuilder();
        return rserialize(root, builder).toString();
    }

    public TreeNode rdeserialize(List<String> l) {
        if (l.get(0).equals("None")) {
            l.remove(0);
            return null;
        }

        TreeNode root = new TreeNode(Integer.valueOf(l.get(0)));
        l.remove(0);
        root.left = rdeserialize(l);
        root.right = rdeserialize(l);

        return root;
    }

    public TreeNode deserialize(String data) {
        String[] data_array = data.split(",");
        List<String> data_list = new LinkedList<String>(Arrays.asList(data_array));
        return rdeserialize(data_list);
    }

    public static void main(String[] args) {
        //add test
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(4);
        CodeC codeC = new CodeC();
        String serialize = codeC.serialize(root);
        System.out.println(serialize);

    }
}
