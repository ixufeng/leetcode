package com.yx.leecode;

/**
 * @author xufeng
 * Create Date: 2019-10-29 16:52
 * 给定两个大小为 m 和 n 的有序数组 nums1 和 nums2。
 * <p>
 * 请你找出这两个有序数组的中位数，并且要求算法的时间复杂度为 O(log(m + n))。
 * <p>
 * 你可以假设 nums1 和 nums2 不会同时为空。
 * <p>
 * 示例 1:
 * <p>
 * nums1 = [1, 3]
 * nums2 = [2]
 * <p>
 * 则中位数是 2.0
 * 示例 2:
 * <p>
 * nums1 = [1, 2]
 * nums2 = [3, 4]
 * <p>
 * 则中位数是 (2 + 3)/2 = 2.5
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/median-of-two-sorted-arrays
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 **/
public class MedianSortedArray {

    public static void main(String[] args) {
        int[] num1 = new int[1];
        num1[0] = 1;
        int[] num2 = new int[4];
        num2[0] = 2;
        num2[1] = 3;
        num2[2] = 4;
        num2[3] = 5;
        System.out.println(findMedianSortedArrays2(num1, num2));
    }

    /**
     * * 思路： 二路归并
     * 时间复杂度 o(n+m)
     *
     * @param nums1
     * @param nums2
     * @return
     */
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {

        if (nums1 == null && nums2 == null) {
            throw new NullPointerException();
        }

        int len = (nums1 == null ? 0 : nums1.length) + (nums2 == null ? 0 : nums2.length);

        boolean b = len % 2 == 0;
        int middle = len / 2;

        int c = 0;
        int index1 = 0;
        int index2 = 0;
        Integer lastNumber = null;

        for (int i = 0; i < len; i++) {
            int cNumber = 0;
            Integer num1 = nums1.length == index1 ? null : nums1[index1];
            Integer num2 = nums2.length == index2 ? null : nums2[index2];

            if (num1 == null) {
                cNumber = num2;
                index2++;
            }
            if (num2 == null) {
                cNumber = num1;
                index1++;
            }

            if (num1 != null && num2 != null) {
                if (num1 < num2) {
                    index1++;
                    cNumber = num1;
                } else {
                    index2++;
                    cNumber = num2;
                }
            }

            if (c == middle) {
                if (b) {
                    return (cNumber + lastNumber) / 2.0;
                } else {
                    return cNumber;
                }
            }
            c++;
            lastNumber = cNumber;
        }

        return 0;
    }

    /**
     * 二分查找
     * O(log(min(m,n)))
     * 思路：
     * 中位数即是将数组划分为长度相等的两块
     *
     * @param A
     * @param B
     * @return
     */
    public static double findMedianSortedArrays2(int[] A, int[] B) {
        int m = A.length;
        int n = B.length;
        if (m > n) {
            // to ensure m<=n
            int[] temp = A;
            A = B;
            B = temp;
            int tmp = m;
            m = n;
            n = tmp;
        }
        //查找合适的i
        //由 i+j = m-i + n-j  =》 j = (n+m+1)/2 +i
        int iMin = 0, iMax = m, halfLen = (m + n + 1) / 2;

        while (iMin <= iMax) {
            int i = (iMin + iMax) / 2;
            int j = halfLen - i;

            if (i < iMax && B[j - 1] > A[i]) {
                iMin = i + 1; // i is too small
            } else if (i > iMin && A[i - 1] > B[j]) {
                iMax = i - 1; // i is too big
            } else { // i is perfect
                //first select max(a[i-1],b[j-1])
                int maxLeft = 0;
                if (i == 0) {
                    //不存在a[i-1]
                    maxLeft = B[j - 1];
                } else if (j == 0) {
                    //不存在b[j-1]
                    maxLeft = A[i - 1];
                } else {
                    maxLeft = Math.max(A[i - 1], B[j - 1]);
                }
                //奇数则直接返回maxLeft
                if ((m + n) % 2 == 1) {
                    return maxLeft;
                }
                //偶数则求出min(a[i],b[j])
                int minRight = 0;
                if (i == m) {
                    minRight = B[j];
                } else if (j == n) {
                    minRight = A[i];
                } else {
                    minRight = Math.min(B[j], A[i]);
                }

                return (maxLeft + minRight) / 2.0;
            }
        }
        return 0.0;
    }
}
