package com.cucmap.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;


@SuppressLint("UseSparseArrays")
class DrawTileCache {
    private Map<Integer,VisitedMapTile> cache;
    private MapTileCache mapTileCache;
    private int MIN_VISITEDTIMES_OF_TILE_IN_CACHE = -10;
    private int MAX_INSERT_TILE_NUM_EACH_TIME = 10;
    private int insertTimes = 0;
    private int deepZoom;   
    
    public DrawTileCache(Context context, int deepZoom) {
		// TODO Auto-generated constructor stub
    	this.deepZoom = deepZoom;
    	cache = new HashMap<Integer, VisitedMapTile>();
    	mapTileCache = new MapTileCache(context, deepZoom);
	}
    
    /**
     * 设置当前地图的物理等级
     * @param deepZoom 新的地图缩放等级
     */
    public void setDeepZoom(int deepZoom){
    	if(this.deepZoom != deepZoom){
    		mapTileCache.setDeepZoom(deepZoom);
    		mapTileCache.cleanAllMapTiles();
    		this.deepZoom = deepZoom;
    	}
    }
    
    /**
     * 从换从中读取切片
     * @param tile_num  切片的编号
     * @param scale     当前切片的缩放大小
     * @return 在画图中实际使用的bitmap，如果得不到就返回null
     */
    public Bitmap getMapTile(int tile_num, float scale){
    	Bitmap bitmap = null;
    	if(cache.containsKey(tile_num)){
    		bitmap = ((VisitedMapTile) cache.get(tile_num)).bitmap;
    		cache.get(tile_num).visitedTimes ++;
    	}else{
    		//从缓冲中读取切片
    		Bitmap mBitmap = mapTileCache.getMapTile(tile_num);
    		
    		//生成指定大小的切片
    		Matrix matrix = new Matrix();
        	matrix.postScale(scale, scale);
    		int size = MapConfig.TILE_SIZE;
    		bitmap = Bitmap.createBitmap(mBitmap, 0, 0, size, size,
    				matrix, true);
    		insertMapTile(tile_num, bitmap);
    	}
    	return bitmap;
    }
    
    
    /**
     * 向缓存中插入切片
     * @param tile_num 切片的编号
     * @param bitmap   切片
     */
    protected void insertMapTile(int tile_num, Bitmap bitmap){
    	VisitedMapTile visitedMapTile = new VisitedMapTile(bitmap, 1);
    	cache.put(tile_num, visitedMapTile);
    	insertTimes ++;
    	if(insertTimes > MAX_INSERT_TILE_NUM_EACH_TIME){
    		cleanMapTiles();
    		insertTimes = 0;
    	}
    }
    
    /**
     * 删除缓存中访问次数最少的切片
     */
    protected void cleanMapTiles(){
    	for (Entry<Integer, VisitedMapTile> entry : cache.entrySet()) {
    	    int visitedTimes = -- entry.getValue().visitedTimes;
    	    if(visitedTimes < MIN_VISITEDTIMES_OF_TILE_IN_CACHE){
    	    	cache.remove(entry.getValue());
    	    }
        }
    }
    
    /**
     * 释放Cache中所有的Tile 
     */
    public void cleanAllMapTiles(){
    	cache.clear();
    	Log.i("Cache-->", Integer.toString(cache.size()));
    }
    
    class VisitedMapTile {
    	public Bitmap bitmap;
    	public int visitedTimes;
    	public VisitedMapTile(Bitmap bitmap, int visitedTimes){
    		this.visitedTimes = visitedTimes;
    		this.bitmap = bitmap;
    	}
    }
}
