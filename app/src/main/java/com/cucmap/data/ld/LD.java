package com.cucmap.data.ld;

/*
 *模拟数据库查询的Like功能
 * 
 */
public class LD {
	public static final double SIMILARITY = 0.5;
	
	/**  
     * 计算矢量距离    
     * @param str1 str1
     * @param str2 str2  
     * @return ld  
     */ 
	public static int ld(String str1, String str2) {
		// Distance
		int[][] d;
		int n = str1.length();
		int m = str2.length();
		int i; // iterate str1
		int j; // iterate str2
		char ch1; // str1
		char ch2; // str2
		int temp;
		if (n == 0) {
			return m;
		}
		if (m == 0) {
			return n;
		}
		d = new int[n + 1][m + 1];
		for (i = 0; i <= n; i++) {
			d[i][0] = i;
		}
		for (j = 0; j <= m; j++) {
			d[0][j] = j;
		}
		for (i = 1; i <= n; i++) {
			ch1 = str1.charAt(i - 1);
			// match str2
			for (j = 1; j <= m; j++) {
				ch2 = str2.charAt(j - 1);
				if (ch1 == ch2) {
					temp = 0;
				} else {
					temp = 1;
				}

				d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1]
						+ temp);
			}
		}
		return d[n][m];
	}

	private static int min(int one, int two, int three) {
		int min = one;
		if (two < min) {
			min = two;
		}
		if (three < min) {
			min = three;
		}
		return min;
	}

	/**
	 * 计算相似度
	 * @param str1 str1
	 * @param str2 str2
	 * @return sim
	 */
	public static double sim(String str1, String str2) {
//		int ld = ld(str1, str2);
//		return 1 - (double) ld / Math.max(str1.length(), str2.length());
		int ld = ld(str1, str2);
		String str;
		int len1 = str1.length();
		int len2 = str2.length();
		int temp = 0;
		if(len1 > len2){
			str = str2;
			temp = len1 - ld;
		}else{
			temp = len2 - ld;
			str = str1;
		}
		return (double)temp / str.length();
	}
}
