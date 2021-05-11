package com.yx.leecode.tree;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xufeng
 * Create Date: 2021/5/11 11:10 下午
 * 从中序与后序遍历序列构造二叉树
 * 根据一棵树的中序遍历与后序遍历构造二叉树。
 *
 * 注意:
 * 你可以假设树中没有重复的元素。
 *
 * 例如，给出
 *
 * 中序遍历 inorder = [9,3,15,20,7]
 * 后序遍历 postorder = [9,15,7,20,3]
 * 返回如下的二叉树：
 *
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 *
 * 作者：力扣 (LeetCode)
 * 链接：https://leetcode-cn.com/leetbook/read/data-structure-binary-tree/xo98qt/
 * 来源：力扣（LeetCode）
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 **/
public class BuildTree {
    /**
     * root节点位置
     */
    public static int post_idx;

    public static int[] inorder_arr;

    public static int[] postorder_arr;

    public static Map<Integer,Integer> index_map = new HashMap<>();


    static class TreeNode {
        TreeNode left;
        TreeNode right;
        int val;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    public static TreeNode helper(int left, int right) {
        if (left > right) {
            return null;
        }
        int rootVal = postorder_arr[post_idx];
        TreeNode root = new TreeNode(rootVal);
        Integer mid = index_map.get(rootVal);
        //倒数第二位是右子树的根节点
        post_idx--;
        //右子树
        root.right = helper(mid + 1, right);

        //左子数
        root.left = helper(left,mid-1);

        return root;

    }

    public static TreeNode buildTree(int[] inorder, int[] postorder) {
        inorder_arr = inorder;
        postorder_arr = postorder;

        post_idx = inorder.length -1;

        //构造map
        for (int i = 0; i < inorder.length; i++) {
            index_map.put(inorder[i],i);
        }
        return helper(0,inorder.length -1);
    }
}
