# 排序算法总结

> 记录10个比较常用的排序：
>
> - [冒泡排序](## 冒泡排序)
>
> - [选择排序](## 选择排序)
> - [插入排序](## 插入排序)
> - [希尔排序](## 希尔排序)
> - [归并排序](## 归并排序)
> - [快速排序](## 快速排序)
> - [堆排序（优先队列）](## 堆排序)
> - [计数排序](## 计数排序)
> - [桶排序](## 桶排序)
> - [基数排序](## 基数排序)
> - [总结](## 总结)

## 冒泡排序

最简单的排序之一，相邻元素两两比较，把小的数放到前面，过程类似水泡上升的过程，由此得名，每次冒泡能找到一个最小的数，总比较次数为$ \frac {n(n-1)}{2}$，总交换次数为$\frac{n(n-1)}{2}$算法时间复杂度为$O(n^2)$，每次排好序之后的值不会变，是稳定排序，代码：

```java
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
```

## 选择排序

思想和冒泡类似，每次遍历后都会将最小的元素放到最前面，但是过程和冒泡排序不大一样，冒泡是与相邻的元素进行交换，而选择每次都闲确定最小值，然后再进行交换，总比较次数依然是$\frac{n(n-1)}{2}$，但是总交换次数为$n$，是不稳定排序，代码：

```java
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
```

## 插入排序

类似于扑克牌整理，左边都是有序的，每次从无序堆选一个插入到有序堆，当数组原来就有序的时候，是最好情况，只需要$n-1$次比较和$0$次交换，最坏情况是数组逆序，总共需要$\frac{n(n-1)}{2}$次比较和$\frac{n(n-1)}{2}$次交换，平均情况取中值，排序稳定，代码：

```java
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
```

## 希尔排序

交换不相邻的元素对数组进行局部排序，然后使用插入排序将有序数组排序，算法复杂度$O(n) < O < O(n^2)$，平均算法复杂度大概是$O(n^{1.3})$,算法不稳定,代码：



```java
public class ShellSort {
    public static void shellSort(int[] array){
        if (array == null || array.length == 0){
            return;
        }
        int h = 1;
        // 使用序列 1/2(3^k -1)作为排序间隔
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
```

## 归并排序

主要思想是分治，将元素分解为各含有n/2个元素的子序列，用合并排序算法对两个子序列递归的进行排序，合并两个已经排好序的子序列得到结果。时间复杂度为$O(nlogn)$，不是原地排序，稳定，代码：

```java
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
```

## 快速排序

和归并排序一样，主要思想是分治，取一个flag：`array[p]`将数组分为两部分左边都小于`array[p]`右边都大于`array[p]`，然后递归解决两个子数组，最好情况下和平均情况下时间复杂度为$O(nlogn)$，最坏时间复杂度为$O(n^2)$，原地排序，算法不稳定。代码：

```java
public class QuickSort {
    public static void quickSort(int[] array){
        qSort(array,0,array.length-1);
    }
    public static void qSort(int[] array, int left, int right){

        if (left < right){
            int pivotKeyIndex = pattition(array, left, right);
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
```

## 堆排序

基本思想是利用堆实现选择排序，不用每一次遍历整个待排数组空间，时间复杂度从$O(n^2)$降为$O(n^2)$，不稳定，原地排序，思想和代码如下：

```java
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
```

## 计数排序

时间复杂度为$o(n)$,前提条件为数据要是在一定范围内的整数，需要用很大的辅助空间，基本思想是，利用待排序数作为计数数组下标，统计每个数字的个数，然后依次输出，代码如下：

```java
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
```

## 桶排序

定制通，基于银蛇函数将序列映射到桶中，对桶中的数据进行排序，然后依次枚举输出桶中元素，可以看到，需要桶满足一定的条件，保证如果$k_1 < k_2$，那么$f(k_1) < f(k_2)$，分桶时间复杂度为$O(n)$，排序最好复杂度为$O(nlogn)$，对于N个数据，M个桶，平均时间复杂度为$O(N)+O(M\times\frac{N}{M}\times\log{\frac{N}{M}})$ = $O(N+N\times \log{N} - N \times \log{M})$,桶排序是稳定的，可以看到，当一个桶只有一个函数的时候，时间复杂度是线性的，空间复杂度相对而言就很高，代码如下:

```java
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
```

## 基数排序

多次入桶，先按最小未大小排序，然后依次升高，最能能保证，在最高位的时候，桶中的数据是有序的，然后合并桶，算法稳定，代码如下：

```java
public class RadixSort {
    public static void sort(int[] number, int d) //d表示最大的数有多少位
    {
        int k = 0;
        int n = 1;
        int m = 1; //控制键值排序依据在哪一位
        int[][]temp = new int[10][number.length]; //数组的第一维表示可能的余数0-9
        int[]order = new int[10]; //数组orderp[i]用来表示该位是i的数的个数
        while(m <= d)
        {
            for(int i = 0; i < number.length; i++)
            {
                int lsd = ((number[i] / n) % 10);
                temp[lsd][order[lsd]] = number[i];
                order[lsd]++;
            }
            for(int i = 0; i < 10; i++)
            {
                if(order[i] != 0)
                    for(int j = 0; j < order[i]; j++)
                    {
                        number[k] = temp[i][j];
                        k++;
                    }
                order[i] = 0;
            }
            n *= 10;
            k = 0;
            m++;
        }
    }
}
```

## 总结

|   名称   |      最好      |            最坏            |            平均            |    空间    |  稳定  |  方法  |
| :------: | :------------: | :------------------------: | :------------------------: | :--------: | :----: | :----: |
| 冒泡排序 |    $ O(n) $    |          $O(n^2)$          |          $O(n^2)$          |     1      |  稳定  |  交换  |
| 选择排序 |    $O(n^2)$    |          $O(n^2)$          |          $O(n^2)$          |     1      |  稳定  |  选择  |
| 插入排序 |    $ O(n) $    |          $O(n^2)$          |          $O(n^2)$          |     1      |  稳定  |  插入  |
| 希尔排序 | $ O(n\log n) $ |   $ O(n^{\frac{4}{3}}) $   |       取决于差距序列       |     1      | 不稳定 |  插入  |
| 归并排序 | $ O(n\log n) $ |       $ O(n\log n) $       |       $ O(n\log n) $       |     n      |  稳定  |  归并  |
| 快速排序 | $ O(n\log n) $ |         $ O(n^2) $         |       $ O(n\log n) $       |     1      | 不稳定 |  分区  |
|  堆排序  |    $ O(n) $    |       $ O(n\log n) $       |       $ O(n\log n) $       |     1      | 不稳定 |  选择  |
| 计数排序 |       -        |         $ O(n+r) $         |         $ O(n+r) $         | $ O(n+r) $ |  稳定  | 非比较 |
|  桶排序  |       -        |         $ O(n+r) $         |         $ O(n+r) $         | $ O(n+r) $ |  稳定  | 非比较 |
| 基数排序 |       -        | $ O(n\times \frac{k}{d}) $ | $ O(n\times \frac{k}{d}) $ | $ O(n+r) $ |  稳定  | 非比较 |



## 参考文献

[1] [维基](https://en.wikipedia.org/wiki/Sorting_algorithm)

[2] [面试中常用的十大排序算法总结](https://github.com/francistao/LearningNotes/blob/master/Part3/Algorithm/Sort/%E9%9D%A2%E8%AF%95%E4%B8%AD%E7%9A%84%2010%20%E5%A4%A7%E6%8E%92%E5%BA%8F%E7%AE%97%E6%B3%95%E6%80%BB%E7%BB%93.md)