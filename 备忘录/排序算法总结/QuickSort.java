import javax.rmi.ssl.SslRMIClientSocketFactory;

public class QuickSort {
    public static void quickSort(int[] array){
        qSort(array,0,array.length-1);
    }
    public static void qSort(int[] array, int left, int right){

        if (left < right){
            int pivotKeyIndex = patition(array, left, right);
            qSort(array, left, pivotKeyIndex - 1);
            qSort(array,pivotKeyIndex + 1,right);
        }
    }
    /**
     * 过程中有三个区间，一个小于基准值，一个大于基准值，一个待排区间
     * @param array 待排序数组
     * @param left  数组起始节点
     * @param right 数组终止节点
     * @return 基准值的位置，左边小于基准值，右边大于基准值
     */
    public static int patition(int[] array,int left,int right){
        int pivotKey = array[right]; //基准值
        int sortLessLast = left -1; //小于基准值的区间的最后一个元素
        int unRestrictedIndex = left; //未排序区间第一个数
        while (unRestrictedIndex <= right - 1){
            //遍历未排序区间
            if (array[unRestrictedIndex] <= pivotKey){
                //待排的数小于基准值
                sortLessLast++; //现在指针指的数是第一个大于基准值的数
                swap(array,sortLessLast,unRestrictedIndex);
            }
            unRestrictedIndex++;
        }
        swap(array,sortLessLast+1,right);
        return sortLessLast+1;
    }
    public static void swap(int[] arr, int left, int right) {
        int temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
    }
}
