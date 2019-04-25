/*
 * @lc app=leetcode.cn id=203 lang=java
 *
 * [203] 移除链表元素
 *
 * https://leetcode-cn.com/problems/remove-linked-list-elements/description/
 *
 * algorithms
 * Easy (39.89%)
 * Total Accepted:    20.9K
 * Total Submissions: 52.1K
 * Testcase Example:  '[1,2,6,3,4,5,6]\n6'
 *
 * 删除链表中等于给定值 val 的所有节点。
 * 
 * 示例:
 * 
 * 输入: 1->2->6->3->4->5->6, val = 6
 * 输出: 1->2->3->4->5
 * 
 * 
 */
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode removeElements(ListNode head, int val) {
        ListNode header = new ListNode(-1);
        header.next = head;

        ListNode preNode = header;  //前一个节点
        ListNode currrent = head;   //遍历的当前节点

        while (currrent != null){
            if (currrent.val == val){
                preNode.next = currrent.next;
                currrent = currrent.next;
            }else {
                 preNode = preNode.next;
                 currrent = currrent.next;
            }
        }
        return header.next;
    }
}

