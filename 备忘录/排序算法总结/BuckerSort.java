import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BuckerSort {
    public static void bucketSort(int[ ] array){
        if (array == null || array.length == 0) return;

        int bucketNUms = 10; //为了方便，规定数字大小为[0,100)
        List<List<Integer>> buckets = new ArrayList<List<Integer>>();

        for (int i = 0; i < array.length; i++){
            buckets.add(new LinkedList<>());
        }

        for (int i = 0; i < buckets.size(); i++){
            if (!buckets.get(f(array[i])).isEmpty()){
                Collections.sort(buckets.get(i));
            }
        }

        int k = 0;
        for (List<Integer> bucket: buckets){
            for (int ele :bucket){
                array[k++] = ele;
            }
        }
    }
    public static int f(int x) {
        return x / 10;
    }
}
