
public class Main {
    public static void main(String[] args){
        int[] array = {1,2,3,4,5,6,7,8};
        //Bubble.bubbleSort(array);
        //SelectSort.selectSort(array);
        //InersrtSort.insertSort(array);
        //ShellSort.shellSort(array);
        //MergeSort.mergeSort(array);
        //QuickSort.quickSort(array);
        HeapSort.heapSort(array);
        for (int i = 0; i< array.length; i++){
            System.out.print(array[i]+ " ");
        }
    }
}
