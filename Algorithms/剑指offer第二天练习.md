# 剑指offer第二天练习

> - 用两个栈实现队列
>
> ## 题目描述
>
> 用两个栈来实现一个队列，完成队列的Push和Pop操作。 队列中的元素为int类型。
>
> ```java
> import java.util.Stack;
> 
> public class Solution {
>     Stack<Integer> stack1 = new Stack<Integer>();
>     Stack<Integer> stack2 = new Stack<Integer>();
>     
>     public void push(int node) {
>         stack1.push(node);
>     }
>     
>     public int pop() {
>         if(stack1.empty() && stack2.empty()){
>             return 0;
>         }
>         else if(stack2.empty())
>         {
>             while(!stack1.empty())
>             {
>                 stack2.push(stack1.pop());
>             }
>         }
>         return stack2.pop();
>     }
> }
> ```

## 解题思路：

- 栈A入队列
- 栈B出队列，操作如下：
  - 当A B 都为空的时候，没有数
  - 当A有数，B没有数要出队列，将A中的所有数移到栈B，然后出栈B
  - 如果A有数，B也有数，直接从B出栈数字

> - 旋转数组最小数字
>
> ## 题目描述
>
> 把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。 输入一个非减排序的数组的一个旋转，输出旋转数组的最小元素。 例如数组{3,4,5,1,2}为{1,2,3,4,5}的一个旋转，该数组的最小值为1。 NOTE：给出的所有元素都大于0，若数组大小为0，请返回0。
>
> ```java
> import java.util.ArrayList;
> public class Solution {
>         public int minNumberInRotateArray(int [] array) {
>             if(array.length = 0)
>                 return 0;
>             int min = array[0];
>             for(int i = 1 ; i < array.length; i++){
>                 if(array[i] < min){
>                     min = array[i];
>                     break;
>                 }
>             }
>             return min;
>         }
>     }
> 
> 
> import java.util.ArrayList;
> public class Solution {
>         public int minNumberInRotateArray(int [] array) {
>             if(array.length == 0)
>                 return 0;
>             if (array.length == 1)
>                 return array[0];
>             int low = 0;
>             int high = array.length -1;
>             int mid = 0;
>             while (low < high)
>             {
>                 mid = low + (high - low)/2;
>                 if(array[mid] > array[high]){
>                     //在右边
>                     low = mid + 1;
>                 }
>                 if(array[mid] == array[high]){
>                     //重复数字直接去掉再判断
>                     high = high - 1;
>                 }
>                 if(array[mid] < array[high]){
>                     //右边一定递增，看左边（包括mid）
>                     high = mid;
>                 }
>             }
>             return array[low];
>         }
>     }
> ```

## 解题思路

- 第一个数最小，遍历到一个比第一个小的数，结束循环，输出这个数。时间复杂度o(n)

- 二分查找，从中间开始分三种情况
  - mid > high 一定在右边 low = mid + 1
  - mid < high，在左边 low = high
  - mid = high，high = high - 1

>- 斐波那契数列
>
>## 题目描述
>
>大家都知道斐波那契数列，现在要求输入一个整数n，请你输出斐波那契数列的第n项（从0开始，第0项为0）。
>
>n<=39
>
>```java
>public class Solution {
>    public int Fibonacci(int n) {
>    
>    }
>}
>```



> - 跳台阶
>
> ## 题目描述
>
> 一只青蛙一次可以跳上1级台阶，也可以跳上2级。求该青蛙跳上一个n级的台阶总共有多少种跳法（先后次序不同算不同的结果）。
>
> ```java
> public class Solution {
>     public int JumpFloor(int target) {
> 
>     }
> }
> ```
>
> 