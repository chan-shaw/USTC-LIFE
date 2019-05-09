
/*
 * @lc app=leetcode.cn id=771 lang=java
 *
 * [771] Jewels and Stones
 *
 * https://leetcode-cn.com/problems/jewels-and-stones/description/
 *
 * algorithms
 * Easy (78.94%)
 * Total Accepted:    29.4K
 * Total Submissions: 37.2K
 * Testcase Example:  '"aA"\n"aAAbbbb"'
 *
 * 给定字符串J 代表石头中宝石的类型，和字符串 S代表你拥有的石头。 S 中每个字符代表了一种你拥有的石头的类型，你想知道你拥有的石头中有多少是宝石。
 * 
 * J 中的字母不重复，J 和 S中的所有字符都是字母。字母区分大小写，因此"a"和"A"是不同类型的石头。
 * 
 * 示例 1:
 * 
 * 输入: J = "aA", S = "aAAbbbb"
 * 输出: 3
 * 
 * 
 * 示例 2:
 * 
 * 输入: J = "z", S = "ZZ"
 * 输出: 0
 * 
 * 
 * 注意:
 * 
 * 
 * S 和 J 最多含有50个字母。
 * J 中的字符不重复。
 * 
 * 
 */
class Solution {
    public int numJewelsInStones(String J, String S) {
        
        if(J.isEmpty() || S.isEmpty()) return 0;

        int count = 0;  // S 中 J 的字符的个数
        char [] A = J.toCharArray(); 
        char [] B = S.toCharArray(); 

        boolean[] equalmap = new boolean[128];
        
        for(int i = 0; i < A.length;i++){
            equalmap[A[i]] = true;
        }

        for(int i = 0; i < B.length; i++){
            if(equalmap[B[i]])
                count++;
        }
        return count;
    }
}

/*
class Solution {
    public int numJewelsInStones(String J, String S) {
        
        int num = 0;
        for(int i=0;i<S.length();i++){
            if(J.contains(S.charAt(i)+"")){
                num++;
            }
        }
        return num;
    }
}
*/