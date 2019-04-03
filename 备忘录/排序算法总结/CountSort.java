import java.util.Arrays;

public class CountSort {
    public static void countSort(int[] array){
        if (array == null || array.length ==0) return;
        int max = max(array);
        int[] count = new int[max+1];
        Arrays.fill(count,0);

        for (int i = 0; i < max; i++){
            count[array[i]]++;
        }
        int k=0;
        for (int i = 0; i < max; i++){
            for (int j = 0; j < count[i]; j++){
                array[k++] = i;
            }
        }
    }
    public static int max(int[] arr) {
        int max = Integer.MIN_VALUE;
        for(int ele : arr) {
            if(ele > max)
            max = ele;
        }
        return max;
    }
}
