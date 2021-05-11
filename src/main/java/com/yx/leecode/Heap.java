package com.yx.leecode;

/**
 * @author xufeng
 * Create Date: 2021/4/19 4:38 下午
 **/
public class Heap {

    public static void heap(int[] arr) {
        //1.构建大顶堆
        for (int i = arr.length / 2 - 1; i >= 0; i--) {
            //从第一个非叶子结点从下至上，从右至左调整结构
            adjustHeap(arr, i, arr.length);
        }
    }

    public static void sort(int[] arr) {
        //堆化
        heap(arr);
        //2.调整堆结构+交换堆顶元素与末尾元素
        for (int j = arr.length - 1; j > 0; j--) {
            swap(arr, 0, j);//将堆顶元素与末尾元素进行交换
            adjustHeap(arr, 0, j);//重新对堆进行调整
        }

    }

    /**
     * 调整大顶堆（仅是调整过程，建立在大顶堆已构建的基础上）
     *
     * @param arr
     * @param i
     * @param length
     */
    public static void adjustHeap(int[] arr, int i, int length) {
        //先取出当前元素i
        int temp = arr[i];
        //从i结点的左子结点开始，也就是2i+1处开始
        for (int left = i * 2 + 1; left < length; left = left * 2 + 1) {
            //找出大的子节点
            int max = left;
            int right = left + 1;
            if (right < length && arr[left] < arr[right]) {
                max = right;
            }
            //如果存在子节点大于父节点，将子节点值赋给父节点（不用进行交换）
            if (arr[max] > temp) {
                arr[i] = arr[max];
                i = max;
            } else {
                break;
            }
        }
        //将temp值放到最终的位置
        arr[i] = temp;
    }


    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private static void printArr(int[] arr) {
        for (int i : arr) {
            System.out.print(i + ", ");
        }
    }

    public static void main(String[] args) {
        int[] arr = new int[]{9, 0, 5, 6, 3, 100};
        sort(arr);
        printArr(arr);
    }
}
