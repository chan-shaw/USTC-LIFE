import java.util.ArrayList;
import java.util.List;

/*
 * @lc app=leetcode.cn id=77 lang=java
 *
 * [77] 组合
 *
 * https://leetcode-cn.com/problems/combinations/description/
 *
 * algorithms
 * Medium (66.39%)
 * Total Accepted:    8.3K
 * Total Submissions: 12.6K
 * Testcase Example:  '4\n2'
 *
 * 给定两个整数 n 和 k，返回 1 ... n 中所有可能的 k 个数的组合。
 * 
 * 示例:
 * 
 * 输入: n = 4, k = 2
 * 输出:
 * [
 * ⁠ [2,4],
 * ⁠ [3,4],
 * ⁠ [2,3],
 * ⁠ [1,2],
 * ⁠ [1,3],
 * ⁠ [1,4],
 * ]
 * 
 */
class Solution {
    public List<List<Integer>> combine(int n, int k) {

        List<List<Integer>> result = new ArrayList<List<Integer>>();
        List<Integer> nums = new ArrayList<Integer>();

        dfs(result, nums, n, k, 1);
        return result;
    }
    public static void dfs(List<List<Integer>> result, List<Integer> nums, int n, int k, int start){
        if(k==0) {
			result.add(new ArrayList<Integer>(nums));
			return;
        }
        
        for(int i = start; i < n + 1; i++){
            if(i > n - k + 1) break;
            nums.add(i);
            dfs(result, nums, n, k-1, i+1);
            nums.remove(nums.size()-1);
        }
    }
}

