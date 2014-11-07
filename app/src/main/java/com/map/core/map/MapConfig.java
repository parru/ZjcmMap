package com.map.core.map;

import android.annotation.SuppressLint;

@SuppressLint("UseSparseArrays")
class MapConfig {
	/** 地图切片大小 **/
	public static final int TILE_SIZE = 75;
	
	public static final int MAX_MAP_SIZE = 3000;
	public static final int MIN_MAP_SIZE = 750;
	
	/** 地图的最大缩放等级 **/
    public static final int MAX_DEEP_ZOOM = 2;
    
    /** 地图的最小缩放等级 **/
    public static final int MIN_DEEP_ZOOM = 0;
    
    /**
     * 得到当前缩放等级的地图实际大小
     * @param deepZoom
     * @return
     */
	protected static int getMapSizeByDeepZoom(int deepZoom) {
		// TODO Auto-generated method stub
		int size = (int) (750 * Math.pow(2, deepZoom - 1));
		return size;
	}
    
    public static float getMapScale(int mapHeight, int deepZoom){
    	float scale = 1f;
    	int size = getMapSizeByDeepZoom(deepZoom);
    	scale = (float) ((float)mapHeight / (float)size);
    	return scale;
    }
    
    public static int getDeepZoomByMapSize(int mapSize){
    	int deepZoom = 1;
    	if(mapSize >= 750 && mapSize < 1500){
    		deepZoom = 1;
    	}
    	if(mapSize >= 1500 && mapSize < 3000){
    		deepZoom = 2;
    	}
    	if(mapSize >= 3000){
    		deepZoom = 3;
    	}
    	return deepZoom;
    }
}
