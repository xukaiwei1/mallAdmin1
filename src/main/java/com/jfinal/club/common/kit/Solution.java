package com.jfinal.club.common.kit;

import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<List<String>> solveNQueens(int n) {
        if (n <= 0) {
            return new ArrayList<>();
        }
        List<List<String>> res = new ArrayList<>();
        //左对角线
        boolean[] d1 = new boolean[2 * n - 1];
        //右对角线
        boolean[] d2 = new boolean[2 * n - 1];
        //该列是否已有皇后
        boolean[] colVisited = new boolean[n];
        //从第0行开始，逐行放置皇后，列出所有可能性结果
        findQueens(n, 0, colVisited, d1, d2, new ArrayList<>(), res);
        return res;
    }

    //在第 row 行 及其之后行放置皇后,pos.get(i) = j ,是标志 第 i 行的皇后放在了第 j列
    public void findQueens(int n, int row, boolean[] colVisited, boolean[] d1, boolean[] d2, ArrayList<Integer> pos, List<List<String>> res) {
        //所有行都已经添加完毕
        if (row == n) {
            res.add(generateGrid(pos));
            return;
        }

        //将皇后放在第row行第j列
        for (int j = 0; j < n; j++) {
            //若当前列或者当前对角线已有皇后，则不能放置
            if (colVisited[j] || d1[row + j] || d2[row - j + n - 1]) {
                continue;
            }
            //第row行第j列符合规范
            //设置标志
            colVisited[j] = true;
            d1[row + j] = true;
            d2[row - j + n - 1] = true;
            pos.add(j);
            //dfs
            findQueens(n, row + 1, colVisited, d1, d2, pos, res);
            //利用回溯，恢复标志
            colVisited[j] = false;
            d1[row + j] = false;
            d2[row - j + n - 1] = false;
            //恢复pos
            pos.remove(pos.size() - 1);
        }
        return;
    }

    //根据 pos来填满整个矩阵： pos.get(i) = j ,是标志 第 i 行的皇后放在了第 j列
    public ArrayList<String> generateGrid(ArrayList<Integer> pos) {
        ArrayList<String> res = new ArrayList<>();
        int n = pos.size();
        for (int i = 0; i < n; i++) {
            int col = pos.get(i);
            StringBuilder sb = new StringBuilder();
            for (int k = 0; k < n; k++) {
                sb.append(k == col ? 'Q' : '.');
            }
            res.add(sb.toString());
        }
        return res;
    }

    public static void main(String[] args) {
        Solution solution=new Solution();
        solution.solveNQueens(8);
    }
}
