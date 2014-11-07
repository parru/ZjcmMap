package com.map.core.map;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.map.model.position.Position;
import com.map.model.road.RoadMap;
import com.map.model.road.RoadMark;
import com.map.core.map.mark.ItemMark;


/*
 * 全局类型，单例模式实现
 * 记录地图的状态、缩放等级、坐标等基本信息
 * @author Pingfu
 */
public class MapManager extends Application {
	/*
	 * MapView的状态
	 * 分为初始状态、工作状态和结束状态
	 */
	public static final int INIT_STATE = 0;   
	public static final int WORKING_STATE = 1;
	public static final int END_STATE = 2;
	
	/*
	 * 第一缩放等级的MapView的长和宽
	 */
	public static final int INIT_MAP_HEIGHT = 750;
	public static final int INIT_MAP_WIDTH = 750;
	
	/*
	 * MapView的缩放等级限制
	 */
	public static final int MIN_DEEP_ZOOM = 1;           //做小缩放等级
	public static final int MAX_DEEP_ZOOM = 3;           //最大缩放等级
	
	/*
	 * 地图的默认图层的z-index值
	 */
	public static final int ITEM_OVERLAY_Z_INDEX = 10;
	public static final int ROUTE_OVERLAY_Z_INDEX = 1;
	
	private int myState;         //记录当前应用的状态    
    
    public List<RoadMark> searchedRoadMarks = new ArrayList<RoadMark>();
    public List<ItemMark> itemMarks = new ArrayList<ItemMark>();  //地图上的标记点
    public List<Position> positions = new ArrayList<Position>();  //地图上的道路
    public String path[];
    public RoadMap roadMap;
	
	public void setMyState(int state){
		this.myState = state;
	}
	
	public int getMyState(){
		return this.myState;
	}
	
	public void initRoadMap(SQLiteDatabase database){
		roadMap = new RoadMap(database);
	    Thread thread = new Thread(roadMap);
	    thread.start();
	}
	
	/**
	 * 根据地图的缩放等级，得到地图的高
	 * @param deep_zoom 地图当前的缩放等级
	 * @return height   当前地图的高 
	 */
	public static int getMapHeight(int deep_zoom){
		int height = 0;
		if(deep_zoom >= MIN_DEEP_ZOOM && deep_zoom <= MAX_DEEP_ZOOM){
			height = (int)Math.pow(2, deep_zoom - 1) * INIT_MAP_HEIGHT; 
		}
		return height;
	}
	
	/**
	 * 根据地图的缩放等级，得到地图的宽
	 * @param deep_zoom 地图当前的缩放等级
	 * @return width    当前地图的宽
	 */
	public static int getMapWidth(int deep_zoom){
		int width = 0;
		if(deep_zoom >= MIN_DEEP_ZOOM && deep_zoom <= MAX_DEEP_ZOOM){
			width = (int)Math.pow(2, deep_zoom - 1) * INIT_MAP_WIDTH;
		}
		return width;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		this.myState = INIT_STATE;
	}
}