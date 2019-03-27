# 剑指offer第一天练习

> - 二维数组中得查找
>
> ## 题目描述
>
> 在一个二维数组中（每个一维数组的长度相同），每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。请完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
>
> ```java
> public class Solution {
>  public  static boolean Find(int target, int [][] array) {
>         int row = 0;//行
>         int col = array[0].length - 1;//列
>         while( (row <= array.length -1) && (col >= 0))
>         {
>             if(target == array[row][col])
>                 return true;
>             else if(target < array[row][col])
>                 col--;
>             else row++;
>         }
>         return false;
>     }
> }
> ```
>

## 解题思路：

从右上到左下遍历，因为是有序的，所以直接能一次遍历，时间复杂度为O(n)，如果`target<array[row][col]`,前一列,如果`target>array[row][col]`，下一行。

>- 替换空格
>
>## 题目描述
>
>请实现一个函数，将一个字符串中的每个空格替换成“%20”。例如，当字符串为We Are Happy.则经过替换之后的字符串为We%20Are%20Happy。
>
>```java
>public class Solution {
>public static String replaceSpace(StringBuffer str) {
>        if(str == null)
>        {
>            return null;
>        }
>        int blockNum = 0;//空格的个数
>        for(int i = 0; i < str.length(); i++){
>            if(str.charAt(i) == ' ')
>                blockNum++;
>        }
>        int oldLength = str.length();
>        int newLength = str.length() + blockNum * 2;
>        str.setLength(newLength);
>        int oldFlag = oldLength-1;
>        int newFlag = newLength-1;
>        while(oldFlag >= 0 && newFlag >= 0)
>        {
>            if(str.charAt(oldFlag)== ' ')
>            {
>                oldFlag--;
>                str.setCharAt(newFlag--,'0');
>                str.setCharAt(newFlag--,'2');
>                str.setCharAt(newFlag--,'%');
>                System.out.println(str);
>            }
>            else {
>                str.setCharAt(newFlag-- , str.charAt(oldFlag--));
>            }
>        }
>        return str.toString();
>    }
>}
>```
>

## 解题思路：

第一次遍历，找到空格的个数，得到新的字符串长度。第二次遍历从后往前得到替换后的字符串。时间复杂度为O(n)

>- 从头到尾打印链表
>
>## 题目描述
>
>输入一个链表，按链表值从尾到头的顺序返回一个`ArrayList`。
>
>```java
>/**
>*    public class ListNode {
>*        int val;
>*        ListNode next = null;
>*
>*        ListNode(int val) {
>*            this.val = val;
>*        }
>*    }
>*
>*/
>//解法一
>import java.util.ArrayList;
>public class Solution {
>ArrayList<Integer> arrayList=new ArrayList<Integer>();
>    public ArrayList<Integer> printListFromTailToHead(ListNode listNode) {
>        if(listNode!=null){
>            this.printListFromTailToHead(listNode.next);
>            arrayList.add(listNode.val);
>        }
>        return arrayList;
>    }
>}
>//解法2
>import java.util.ArrayList;
>import java.util.Stack;
>public class Solution {
>	public ArrayList<Integer> printListFromTailToHead(ListNode listNode) {
>    	Stack<Integer> stack = new Stack<>();
>    	while(listNode != null)
>    	{
>        	stack.push(listNode.val);
>        	listNode = listNode.next;
>    	}
>    	ArrayList<Integer> arrayList = new ArrayList<>();
>    	while (!stack.empty())
>    	{
>        	arrayList.add(stack.pop());
>    	}
>    	return arrayList;
>	}
>}
>
>```

## 解题思路：

- 递归，终止条件为到最后一个，否则先打印后一个链的值，再打印当前链的值；
- 用一个栈存储链表值，出栈输入数组就是逆序。

>- 重建二叉树
>
>输入某二叉树的前序遍历和中序遍历的结果，请重建出该二叉树。假设输入的前序遍历和中序遍历的结果中都不含重复的数字。例如输入前序遍历序列{1,2,4,7,3,5,6,8}和中序遍历序列{4,7,2,1,5,3,8,6}，则重建二叉树并返回。
>
>```java
>/**
>* Definition for binary tree
>* public class TreeNode {
>*     int val;
>*     TreeNode left;
>*     TreeNode right;
>*     TreeNode(int x) { val = x; }
>* }
>*/
>public class Solution {
>public TreeNode reConstructBinaryTree(int [] pre,int [] in) {
>
>        if(pre.length == 0)
>            return null;
>
>        TreeNode treeNode = new TreeNode(pre[0]);//前序遍历的第一个数就是它的根节点
>
>        int root = 0;//在中序遍历中，存放根节点的数组下标
>        for(  ; in[root] != pre[0];root++);
>
>        int [] leftChildPre = new int[root];//子节点的前序遍历，根在3，代表中序前面有3个数，是左子树
>        int [] leftChildIn = new int[root];//子节点的中序遍历
>        //得到左子树
>        for(int i = 0;i < leftChildPre.length; i++){
>            leftChildPre[i] = pre[i+1];
>        }
>        for(int i = 0;i < leftChildIn.length; i++){
>            leftChildIn[i] = in[i];
>        }
>
>        int[] rightChildPre = new int[pre.length-root-1];
>        int[] rightChildIn = new int[in.length-root-1];
>        //得到右子树
>        for(int i = 0;i < rightChildIn.length; i++)
>        {
>            rightChildIn[i] = in[root + 1 + i];
>        }
>
>        for(int i = 0;i < rightChildPre.length; i++)
>        {
>            rightChildPre[i] = pre[root + 1 + i];
>        }
>
>        treeNode.left = reConstructBinaryTree(leftChildPre,leftChildIn);
>        treeNode.right = reConstructBinaryTree(rightChildPre,rightChildIn);
>        return treeNode;
>    }
>}
>```
>

## 解题思路：

递归，分三步，

- 从前序找到根，
- 找到左子树`pre` ` in`，右子树`pre` `in`
- 建立左子树和右子树