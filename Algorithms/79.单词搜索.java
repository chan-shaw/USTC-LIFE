/*
 * @lc app=leetcode.cn id=79 lang=java
 *
 * [79] 单词搜索
 *
 * https://leetcode-cn.com/problems/word-search/description/
 *
 * algorithms
 * Medium (36.39%)
 * Total Accepted:    8.3K
 * Total Submissions: 22.8K
 * Testcase Example:  '[["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]]\n"ABCCED"'
 *
 * 给定一个二维网格和一个单词，找出该单词是否存在于网格中。
 * 
 * 单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。同一个单元格内的字母不允许被重复使用。
 * 
 * 示例:
 * 
 * board =
 * [
 * ⁠ ['A','B','C','E'],
 * ⁠ ['S','F','C','S'],
 * ⁠ ['A','D','E','E']
 * ]
 * 
 * 给定 word = "ABCCED", 返回 true.
 * 给定 word = "SEE", 返回 true.
 * 给定 word = "ABCB", 返回 false.
 * 
 */
class Solution {
    public boolean exist(char[][] board, String word){
        boolean result;
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length; j++){
                if (board[i][j] == word.charAt(0)){
                    result = dfs(board,word,i,j,0);
                    if (result)
                        return true;
                }
            }
        }
        return false;
    }
    private static boolean dfs(char[][] board, String word,int row, int column ,int index){
        if (index == word.length()) return true;
        if (row < 0 || row >= board.length || column < 0 || column >= board[0].length) return false;
        if (board[row][column] != word.charAt(index)) return false;
        board[row][column] <<= 1;
        boolean result = dfs(board, word, row+1, column, index+1)||
                dfs(board, word, row, column+1, index+1)||
                dfs(board, word, row-1, column, index+1)||
                dfs(board, word, row, column-1, index+1);
        board[row][column] >>= 1;
        return result;
    }
}

