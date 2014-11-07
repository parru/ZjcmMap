package com.map.core.map;

import android.util.Log;

import com.map.core.map.overlay.Overlay;

public class MapControl{
    private MapView mapView;
    private int mapWidth;
    private int mapHeight;
    private int screenHeight;
    private int screenWidth;
    
    public MapControl(MapView mapView, int screenHeight,int screenWidth) {
		this.mapView = mapView;
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		initMap();
	}
    
    public void initMap(){
    	this.mapHeight = mapView.getMapHeight();
    	this.mapWidth = mapView.getMapWidth();
    }
    
    /**
     * 设置地图的缩放级别
     * @param deepZoom 地图的缩放级别 取值范围为[1,3]
     */
    public void setZoom(int deepZoom){
    	if(deepZoom >= MapManager.MIN_DEEP_ZOOM 
    			&& deepZoom <= MapManager.MAX_DEEP_ZOOM){
    	    mapView.setDeepZoom(deepZoom);
    	}
    }
    
    /**
     * 放大地图，严格按照地图的缩放等级进行缩放地图
     */
    public void zoomIn(){
    	mapView.zoomIn();
    }
    
    //zoom out this map
    public void zoomOut(){
    	Log.i("zoom_out","###############");
    	mapView.zoomOut();
    }

    /*
     * to mave this map according with the vector (dx,dy)
     */
    public void moveMap(int dx,int dy){
    	mapView.moveMap(dx, dy);
    }
    
    /*
     * 得到屏幕中点对应的经纬度坐标 
     */
    public GeoPoint getCenterPoint(){
    	
    	int x = -mapView.getMapOffsetX() + mapWidth / 2;
    	int y = -mapView.getMapOffsetY() + mapHeight / 2;
    	Log.i("get_MapY-->", Integer.toString(y));
    	GeoPoint point = GeoPoint.getGeoPoint(mapHeight, mapWidth, x, y);
    	return point;
    }
    
    
    /**
     * 将坐标点point设置为屏幕的中点
     * @param point   Map的中心坐标
     */
    public void setCenter(GeoPoint point){
    	int x = point.getMapX(mapWidth);
    	int y = point.getMapY(mapHeight);
    	
    	int mapOffsetX = -(x - (this.screenWidth / 2));
    	int mapOffsetY = -(y - (this.screenHeight / 2));
    	mapView.setMapPosition(mapOffsetX, mapOffsetY);
    	Log.i("set__MapY-->", Integer.toString(y));
    }
    
    /**
     *动态的移动地图、实现平滑移动效果
     */
    public void animateTo(GeoPoint point){
    	
    }
    
    public void addOverlayer(Overlay overlay){
    	mapView.getOverlayMap().put(overlay.getzIndex(), overlay);
    }
}
