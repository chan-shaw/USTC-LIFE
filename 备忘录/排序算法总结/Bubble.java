public class Bubble {
    public static void bubbleSort(int[] array){
        if (array == null || array.length == 0)
            return;
        for (int i = 0; i < array.length;i++){
            //从后往前冒泡
            for (int j = array.length-1; j > i;j--){
                if (array[j] < array[j-1]){
                    array[j] = array[j] + array[j-1];
                    array[j-1] = array[j] - array[j-1];
                    array[j] = array[j] - array[j-1];
                }
            }
        }
    }
}
/** tips:
 * 不引入辅助变量交换两个数
 * 方法一:
 * x = x + y;
 * y = x - y;
 * x = x - y;
 *
 * 方法二:
 * x ^= y;
 * y ^= x;
 * x ^= x;
 */