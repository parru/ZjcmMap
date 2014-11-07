package com.map.model;

//接受一个有向图的权重矩阵，和一个起点编号start（从0编号，顶点存在数组中）  
public class Dijkstra {
    private String path[];     // 存放从start到其他各点的最短路径的字符串表示
    private int shortPath[];   // 存放从start到其他各点的最短路径
    
    public void getShortestDistance(int[][] map,int start){
		int n = map.length; // 顶点个数
		shortPath = new int[n]; 
		path = new String[n];
		for (int i = 0; i < n; i++) {
			path[i] = new String(start + "_" + i);
		}
		int[] visited = new int[n]; // 标记当前该顶点的最短路径是否已经求出,1表示已求出
		for(int i=0;i<n;i++){
			visited[i]=0;
		}
		//初始化，第一个顶点求出
		shortPath[start] = 0;
		visited[start] = 1;

		for (int count = 1; count <= n - 1; count++) { // 要加入n-1个顶点
			int k = -1; // 选出一个距离初始顶点start最近的未标记顶点
			int dmin = Integer.MAX_VALUE;
			for (int i = 0; i < n; i++) {
				if (visited[i] == 0 && map[start][i] < dmin) {
					dmin = map[start][i];
					k = i;
				}
			}
			// 将新选出的顶点标记为已求出最短路径，且到start的最短路径就是dmin
			shortPath[k] = dmin;
			visited[k] = 1;

			// 以k为中间点，修正从start到未访问各点的距离
			for (int i = 0; i < n; i++) {
				if (visited[i]==0&&map[start][k]+map[k][i]<map[start][i]) {
					map[start][i] = map[start][k] + map[k][i];
					path[i] = path[k] + "_" + Integer.toString(i);
				}
			}
		}
	}

	public String[] getPath() {
		return path;
	}

	public void setPath(String[] path) {
		this.path = path;
	}
	
	public int[] getShortPath() {
		return shortPath;
	}
	
	public void setShortPath(int[] shortPath) {
		this.shortPath = shortPath;
	}    
}
