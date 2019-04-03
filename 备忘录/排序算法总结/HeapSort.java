
public class HeapSort {
    /**
     * 堆排序的过程：
     * 1 数组转化为大根堆或者小根堆（以大根堆为例
     * 1.1 自低向上遍历，从第一个非叶子节点开始 lastNodeIndex = (lastLeafIndex - 1)/2 = (array.length - 2)/2
     * 1.2 每步遍历的调整规则：将非叶节点和它的子节点作比较，最大的在上面
     * 1.3 每步遍历次数，如果子节点和父节点发生了交换，那么子节点还要向下验证
     * 2 调整堆，使其有序
     *  将根节点与数组最后一个数组交换位置，大根堆遍历长度减一，调整新堆，（只需要调整最上层，使其有序，直到遍历的长度为0
     * @param array array[0...length-1]
     *              对于节点array[k]有
     *              左孩子:array[2k+1]
     *              右孩子:array[2k+2]
     */
    public static void heapSort(int[] array){
        int lastLeafIndex = array.length -1;
        int beginIndex = (lastLeafIndex-1)/2;
        for (int i = beginIndex; i >= 0; i--){
            //将数组堆化
            maxHeap(array,i,lastLeafIndex);
        }
        //对堆数组排序
        for (int i = lastLeafIndex; i > 0; i--){
            swap(array,0,i);
            maxHeap(array,0,i-1);
        }
   }

    /**
     * 调整节点。构造大根堆
     * @param array             被调整的数组
     * @param parentNodeIndex   当前调整的节点
     * @param lastLeafIndex     最后一个叶节点，为了后面排序做准备
     */
    public static void maxHeap(int[] array,int parentNodeIndex,int lastLeafIndex){
        if (parentNodeIndex * 2 > array.length) return;
        int leftChildIndex = parentNodeIndex * 2 + 1;   //左孩子
        int rightChildIndex = leftChildIndex + 1;   //右孩子

        if (leftChildIndex > lastLeafIndex) return;

        int maxIndex = leftChildIndex; //最大的节点
        if (rightChildIndex <= lastLeafIndex && array[rightChildIndex] > array[maxIndex]){
            maxIndex = rightChildIndex;
        }
        if (array[maxIndex] > array[parentNodeIndex]){
            //如果孩子里面有比父亲大的节点，调整，此时 maxIndex存的是沉下去的数
            swap(array, maxIndex, parentNodeIndex);
            //继续判断沉下去之后的节点是否符合堆的特性
            maxHeap(array, maxIndex, lastLeafIndex);
        }
    }
    public static void swap(int[] array, int i, int j){
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
