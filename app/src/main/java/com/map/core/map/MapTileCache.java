package com.map.core.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * 管理MapTile，减少IO操作
 * @author Pingfu
 */
@SuppressLint("UseSparseArrays")
class MapTileCache {
    private Map<Integer,VisitedMapTile> cache;
    private int MIN_VISITEDTIMES_OF_TILE_IN_CACHE = -10;
    private int MAX_INSERT_TILE_NUM_EACH_TIME = 10;
    private int insertTimes = 0;
    private int deepZoom;   
    private MapTile mapTile;
    
    public MapTileCache(Context context, int deepZoom) {
		// TODO Auto-generated constructor stub
    	this.deepZoom = deepZoom;
    	cache = new HashMap<Integer, VisitedMapTile>();
    	mapTile = new MapTile(context);
	}
    
    /**
     * 设置当前地图的物理等级
     * @param deepZoom
     */
    public void setDeepZoom(int deepZoom){
    	this.deepZoom = deepZoom;
    }
    
    /**
     * 
     * @param tile_num
     * @return
     */
    public Bitmap getMapTile(int tile_num){
    	Bitmap bitmap = null;
    	if(cache.containsKey(tile_num)){
    		bitmap = ((VisitedMapTile) cache.get(tile_num)).bitmap;
    		cache.get(tile_num).visitedTimes ++;
    	}else{
    		bitmap = mapTile.getBitmapById(deepZoom, tile_num);
    		insertMapTile(tile_num, bitmap);
    	}
    	return bitmap;
    }
    
    
    /**
     * 想缓存中出啊如切片
     * @param tile_num  切片的编号
     * @param bitmap    切片
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
