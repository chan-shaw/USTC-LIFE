public class ShellSort {
    public static void shellSort(int[] array){
        // 使用序列 1/2(3^k -1)作为排序间隔
        if (array == null || array.length == 0){
            return;
        }
        int h = 1;
        while (h < array.length / 3) h = 3 * h + 1;
        while (h >= 1){
            //将数组变为 h 有序
            for (int i = h; i < array.length; i++){
                //将 a[i] 插入到 a[i-h],a[i-2*h]...中
                for (int j = i; j >=h; j -=h){
                    if (array[j] < array[j-h]){
                        array[j] = array[j] + array[j-h];
                        array[j-h] = array[j] - array[j-h];
                        array[j] = array[j] -array[j-h];
                    }
                }
            }
            h = h/3;
        }
    }
}
