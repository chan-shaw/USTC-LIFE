public class MergeSort {
    public static void mergeSort(int[] array){
        mSort(array,0,array.length -1);
    }
    /**
     * 分治，一个数组分成两个，分别排好序之后合并
     * @param array 待排序数组
     * @param left  指向数组最左边节点指针
     * @param right 指向数组最右边节点指针
     */
    public static void mSort(int[] array, int left, int right){
        if (left >= right) return;
        int mid = (left + right) /2;
        mSort(array, left, mid);
        mSort(array, mid+1, right);
        merge(array, left, mid, right);
    }

    /**
     * 合并子数组
     * @param array 待排序数组
     * @param left  指向数组最左边节点指针
     * @param mid   指向数组中间位置节点
     * @param right 指向数组最右边节点指针
     */
    public static void merge(int[] array,int left,int mid,int right){
        //[left...mid] [mid...right]
        int[] temp = new int[right-left+1]; //中间数组
        int i = left;
        int j = mid + 1;
        int k = 0;
        while (i <= mid && j <= right){
            if (array[i] <= array[j]){
                temp[k++] = array[i++];
            }else {
                temp[k++] = array[j++];
            }
        }
        while (i <= mid){
            temp[k++] = array[i++];
        }

        while (j <= right){
            temp[k++] = array[j++];
        }
        for (int m = 0;m < temp.length;m++){
            array[left + m] = temp[m];
        }
    }
}
