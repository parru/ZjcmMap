package com.map.core.common;

public class Floyd {

    public int[][] length = null;// 任意两点之间路径长度 
    public int[][][] path = null;// 任意两点之间的路径 

    public Floyd(int[][] G) {
        int MAX = 1000;
        int row = G.length;// 图G的行数 
        int[][] spot = new int[row][row];// 定义任意两点之间经过的点 
        int[] onePath = new int[row];// 记录一条路径 

        length = new int[row][row];
        path = new int[row][row][];

        for (int i = 0; i < row; i++) // 处理图两点之间的路径 
        {
            for (int j = 0; j < row; j++) {
                if (G[i][j] == 0) {
                    G[i][j] = MAX;// 没有路径的两个点之间的路径为默认最大 
                }
                if (i == j) {
                    G[i][j] = 0;// 本身的路径长度为0 
                }
            }
        }

        for (int i = 0; i < row; i++) // 初始化为任意两点之间没有路径 
        {
            for (int j = 0; j < row; j++) {
                spot[i][j] = -1;
            }
        }

        for (int i = 0; i < row; i++) // 假设任意两点之间的没有路径 
        {
            onePath[i] = -1;
        }

        for (int v = 0; v < row; ++v) {
            for (int w = 0; w < row; ++w) {
                length[v][w] = G[v][w];
            }
        }

        for (int u = 0; u < row; ++u) {
            for (int v = 0; v < row; ++v) {
                for (int w = 0; w < row; ++w) {
                    if (length[v][w] > length[v][u] + length[u][w]) {
                        length[v][w] = length[v][u] + length[u][w];// 如果存在更短路径则取更短路径 
                        spot[v][w] = u;// 把经过的点加入 
                    }
                }
            }
        }

        for (int i = 0; i < row; i++) {
            // 求出所有的路径 
            int[] point = new int[1];
            for (int j = 0; j < row; j++) {
                point[0] = 0;
                onePath[point[0]++] = i;
                outputPath(spot, i, j, onePath, point);
                path[i][j] = new int[point[0]];
                for (int s = 0; s < point[0]; s++) {
                    path[i][j][s] = onePath[s];
                }
            }
        }
    }

    void outputPath(int[][] spot, int i, int j, int[] onePath, int[] point) {
        // 输出i到j的路径的实际代码,point[]记录一条路径的长度 
        if (i == j) {
            return;
        }
        if (spot[i][j] == -1) {
            onePath[point[0]++] = j;
        } // System.out.print(" "+j+" "); 
        else {
            outputPath(spot, i, spot[i][j], onePath, point);
            outputPath(spot, spot[i][j], j, onePath, point);
        }
    }
}