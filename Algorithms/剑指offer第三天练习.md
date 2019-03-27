# 剑指offer第三天练习

> - 矩阵覆盖
>
> ## 题目描述
>
> 我们可以用2`*`1的小矩形横着或者竖着去覆盖更大的矩形。请问用n个2`*`1的小矩形无重叠地覆盖一个2*n的大矩形，总共有多少种方法？
>
> ```java
> public class Solution {
>         public int RectCover(int target) {
>             if (target == 0) return 0;
>             if(target == 1) return 1;
>             else if (target == 2) return 2;
>             else return RectCover(target - 1) +  RectCover(target - 2);
>         }
>     }
> ```

## 解题思路

分为凉后再那个情况，n-1的时候，加一个就行， n-2 的时候，分为两种补，一种横放没有问题，一种竖着放，包含在n-1补一个重复种，所以有$f(x) = f(x-1)+f(x-2)$

> - 二进制中1的个数
>
> ## 题目描述
>
> 输入一个整数，输出该数二进制表示中1的个数。其中负数用补码表示。
>
> ```java
> public class Solution {
>     public int NumberOf1(int n) {
>         int count = 0;
>         while( n != 0)
>         {
>             count++;
>             n = n & (n-1);
>         }
>         return count;
>     }
> }
> ```

## 解题思路

- 一个技巧 `n & (n-1)`得到的值是二进制表示n去掉一个1之后的值

> - 数值的整数次方
>
> ## 题目描述
>
> 给定一个`double`类型的浮点数base和`int`类型的整数exponent。求base的exponent次方。
>
> ```java
> public class Solution {
>     public double Power(double base, int exponent) {
>             double result = 1;
>             int exp;
>             if (exponent > 0){
>                 exp = exponent;
>             }else if (exponent < 0){
>                 if (base ==0)
>                     throw new RuntimeException();
>                 exp = -exponent;
>             }
>             else return 1;
>             while (exp >0){
>                 if((exp & 1) ==1)
>                     result *= base;
>                 base *= base;
>                 exp >>=1;
>             }
>             return exponent >=0?result:(1/result);
>         }
> }
> ```

## 解题思路

- 快速幂运算

$$
2^{11} = 2^{5} \times 2^{5} \times 2
$$

类似的将指数化为2的倍数，然后再相乘。

- result = 1;
- 指数除以2，如果余数为1，result * base; 
- base *2 ,指数除以2

> - 调整数组顺序使奇数位于偶数前面
>
> ## 题目描述
>
> 输入一个整数数组，实现一个函数来调整该数组中数字的顺序，使得所有的奇数位于数组的前半部分，所有的偶数位于数组的后半部分，并保证奇数和奇数，偶数和偶数之间的相对位置不变。
>
> ```java
> public class Solution {
>         public void reOrderArray(int [] array) {
>         int[] a = new int[array.length];
>         int[] b = new int[array.length];
>         int a_index = 0;
>         int b_index = 0;
>         for(int i =0; i < array.length; i++){
>             if ((array[i] & 1) ==0) {
>                 //偶数
>                 a[a_index] = array[i];
>                 a_index++;
>             }
>             if ((array[i] & 1) ==1) {
>                 //奇数
>                 b[b_index] = array[i];
>                 b_index++;
>             }
>         }
>         for (int i = 0; i < b_index;i++){
>             array[i] = b[i];
>         }
>         for (int i = 0;i < a_index;i++)
>         {
>             array[b_index+i] = a[i];
>         }
>     }
> }
> ```
>
 ## 解题思路

 空间换时间

 用数组存储奇数偶数再填充回去，时间复杂度为o(n)但是新开辟了两个空间大小为o(n)的数组

