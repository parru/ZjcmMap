package com.map.core.update.sdcard;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

/*
 * 将地图切片上传到sdcard上
 * @author Pingfu 
 */
@SuppressLint("SdCardPath")
public class UpLoadMapTiles implements Runnable{	
	public static final String IMAGE_DIR_1 = "/sdcard/zoom_1";
	public static final String IMAGE_DIR_2 = "/sdcard/zoom_2";
	public static final String IMAGE_DIR_3 = "/sdcard/zoom_3";
	private static int maxZoom = 3;
	private static int minZoom = 1;
		
	/*
	 * 从Assets中读取地图切片
	 * @param deep_zoom 地图缩放等级
	 * @param num       切片的编号
	 * @return bitmap   读取到的切片
	 */
	public Bitmap getBitmapFromAssets(int deep_zoom, int num){
		Bitmap bitmap = null;
		return bitmap;
	}
	
	/*
	 * 通过网络下载地图切片
	 * @see java.lang.Runnable#run()
	 */
	public Bitmap getBitmapFromWebServer(String uri){
		Bitmap bitmap = null;
		
		return bitmap;
	}
	
	/*
	 * 将地图切片存放到指定的目录下
	 */
	public void upLoadMapTile(Bitmap bitmap, String path){
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
	    	
	}
}
