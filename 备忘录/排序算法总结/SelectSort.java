public class SelectSort {
    public static void selectSort(int[] array){
        if (array == null || array.length == 0)
            return;
        for (int i = 0; i < array.length - 1; i++){
            //将array[i]和array[i+1...N]中最下的值进行交换
            int minIndex = i; //最下元素的索引
            for (int j = i + 1; j < array.length; j++){
                if (array[j] < array[minIndex]){
                    minIndex = j;
                }
            }
            if (minIndex != i){
                array[i] = array[i] + array[minIndex];
                array[minIndex] = array[i] - array[minIndex];
                array[i] = array[i] - array[minIndex];
            }

        }
    }
}
