public class InersrtSort {
    public static void insertSort(int[] array){
        if (array == null || array.length == 0){
            return;
        }
        for (int i = 1; i < array.length; i++){
            int key = array[i];
            //将 key 插入a[0...i]中
            int j = i - 1;
            //较大的元素右移
            while (j > 0 && array[j] > key){
                array[j+1] = array[j];
                j--;
            }
            array[j+1] = key;
        }
    }
}
