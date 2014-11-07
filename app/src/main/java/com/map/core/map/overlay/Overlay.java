
package com.map.core.map.overlay;
import android.graphics.Canvas;

import com.map.core.map.MapView;

/*
 * @author Pingfu
 * the abscract class of the overlayer in the map,
 * All the other class about 
 */
public abstract class Overlay{
	
	private MapView mapView;           //当前的地图逻辑层
	private int zIndex;                //新添加图层的Z坐标
	private boolean isShowing = false; //是否显示该图层
	
    public Overlay(MapView mapView) {
		// TODO Auto-generated constructor stub
    	this.mapView = mapView;
	}
	    
	public MapView getMapView() {
		return mapView;
	}

	public void setMapView(MapView mapView) {
		this.mapView = mapView;
	}

	public int getzIndex() {
		return zIndex;
	}

	public void setzIndex(int zIndex) {
		this.zIndex = zIndex;
	}
    
    public void displayOverlay(){
    	this.isShowing = true;
    }
    
    public void hideOverlay(){
    	this.isShowing = false;
    }
    
    public boolean isShowingOverlay(){
    	return this.isShowing;
    }
    
    /*
     * draw the items in the overlayer
     * @author Pingfu
     */
    public abstract void draw(Canvas canvas,int deepZoom);
    
    /*
     * 添加点击事件
     * @param event 当前的点击事件
     */
    public abstract void onClick(int x,int y);
}
