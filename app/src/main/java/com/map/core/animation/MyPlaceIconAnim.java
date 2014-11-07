package com.map.core.animation;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.map.core.PathFindingActivity;
import com.map.model.position.Position;
import com.map.model.road.RoadMark;
import com.map.core.map.GeoPoint;
import com.map.core.map.MapView;
import com.map.core.common.SearchRoadUtil;
import com.map.R;

public class MyPlaceIconAnim {
    private int screenX;       //icon在手机屏幕上的X轴坐标
    private int screenY;       //icon在手机屏幕上的Y轴坐标
    
    private Context context;
    //private Location location;
    private static int STEP = 4;
    private Animation iconAnims = null;
    
    private List<RoadMark> roadMarks;
    private MapView mapView;
    public double longitude = 0;
    public double latitude = 0;
    
    /**
     * 导航设置
     */
    private boolean isNavigating = false;
    private boolean isShowMyPlace = false;
    
    public MyPlaceIconAnim(Context context, int screenX, int screenY, MapView mapView){
		this.context = context;
		this.screenX = screenX;
		this.screenY = screenY;
		this.mapView = mapView;
		isNavigating = false;
		initIconAnims();
	}
    
    /** 任意键被按下 **/
	private boolean state = false;
	
	public int getScreenX() {
		return screenX;
	}

	public void setScreenX(int screenX) {
		this.screenX = screenX;
	}

	public int getScreenY() {
		return screenY;
	}

	public void setScreenY(int screenY) {
		this.screenY = screenY;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public List<RoadMark> getRoadMarks() {
		return roadMarks;
	}

	public void setRoadMarks(List<RoadMark> roadMarks) {
		this.roadMarks = roadMarks;
	}
	
	public void setMapView(MapView mapView){
		this.mapView = mapView;
	}
	
	/**
	 * 初始化动画图标
	 */
	public void initIconAnims(){
		int[] animation_id = new int[]{
				R.drawable.frame_1, R.drawable.frame_2,
				R.drawable.frame_3,R.drawable.frame_4
			};
		iconAnims = new Animation(context, animation_id, true);
	}
    
	/*
	 * 判断是否显示当前动画
	 */
	public boolean isShowMyPlaceAnim(){
		return this.isShowMyPlace;
	}
	
	/**
	 * 显示当前动画
	 */
	public void showMyPlaceAnim(){
		this.isShowMyPlace = true;
	}
	
	/**
	 * 开始导航
	 */
	public void beginNavigating(){
		isNavigating = true;
	}
	
	/**
	 * 将动画移动到屏幕上的(x, y)坐标点
	 * @param x 屏幕上的X轴坐标
	 * @param y 屏幕上的Y轴坐标
	 */
	public void moveToScreenPosition(int x, int y){
		screenX = x;
		screenY = y;
	}
	
	/**
	 * 将动画移动到地图的Position位置
	 * @param position 要将动画移动到的位置 
	 * @param mapView  当前的地图
	 */
	public void moveToPosition(Position position, MapView mapView){
		GeoPoint point = GeoPoint.getGeoPoint(position);
		moveToPosition(point, mapView);
	}
	
	/**
	 * 将动画移动到地图的Point坐标上
	 * @param point   地图上的坐标点
	 * @param mapView 当前的地图
	 */
	public void moveToPosition(GeoPoint point, MapView mapView){
		int mapHeight = mapView.getMapHeight();
		int mapWidth = mapView.getMapWidth();
		
		int x = point.getMapX(mapWidth) + mapView.getMapOffsetX();
		int y = point.getMapY(mapHeight)+ mapView.getMapOffsetY();
		int screenHeight = mapView.getScreenHeight();
		int screenWidth = mapView.getScreenWidth();
		if(x > 0 && y > 0 && x < screenWidth && y < screenHeight){
			/**
			 * 当前位置在屏幕范围内
			 * 移动动画到屏幕上的（x,y）坐标点
			 */
			moveToScreenPosition(x, y);
			//开始导航
			if(isNavigating){
				if(!isInRoads()){
					createDialog(context);
				}
			}
		}else{
			moveToScreenPosition(screenWidth / 2, screenHeight / 2);
		}
	}
	
	/**
	 * 更新当前位置
	 * @param location 新得到的Location信息 
	 */
	public void updateMyLocation(Location location){
//		LocationManager locationManager = new LocationManager(context);
//		Location location = locationManager.requestLocation();
		double longitude = location.getLongitude();
		double latitude = location.getLatitude();
		Log.i("Longitude-->",Double.toString(longitude));
	    Position position = new Position(-1, longitude, latitude, "");
		moveToPosition(position, mapView);
		mapView.postInvalidate();
	}
	
	/**
	 * 判断当前坐标点是否超出屏幕
	 * @param screenHeight 当前手机屏幕的高度
	 * @param screenWidth  当前手机屏幕的宽度
	 * @return flag        是否超出屏幕的标志
	 */
	public boolean isBeyondScreen(int screenHeight,int screenWidth){
		boolean flag = false;
		if(screenX-STEP < 0 || screenY-STEP < 0 
				|| screenX+STEP > screenWidth || screenY+STEP> screenHeight){
			flag = true;
		}
		return flag;
	}	
	
	/**
	 * 在画板上绘制当前动画
	 * @param canvas 当前画布
	 * @param paint  当前的画笔
	 */	
	public void renderAnimation(Canvas canvas, Paint paint){
		if(longitude != 0 && latitude != 0){
			GeoPoint point = new GeoPoint("", longitude, latitude);
			screenX = point.getMapX(mapView.getMapWidth());
			screenY = point.getMapY(mapView.getMapHeight());
		}
		iconAnims.drawAnimation(canvas, paint , screenX, screenY);
		iconAnims.upDateAnimation();
	}
	
	/**
	 * 在一组道路上得到距离自己最近的道路
	 * @param roadMarks 一组道路
	 * @param mapView   当前的地图
	 * @return roadMark 距离自己最近的道路
	 */
	public RoadMark findNearestRoadMark(List<RoadMark>roadMarks){
		//RoadMark roadMark = roadMarks.get(0);
		int key = 0;
		int d = Integer.MAX_VALUE;
		for(int i = 0; i < roadMarks.size(); i++){
			RoadMark temp = roadMarks.get(i);
			GeoPoint point = GeoPoint.getGeoPoint(temp.getBeginPosition());
			int x1 = screenX - mapView.getMapOffsetX();
			int y1 = screenY - mapView.getMapOffsetY();
			int x2 = point.getMapX(mapView.getMapWidth());
			int y2 = point.getMapY(mapView.getMapHeight());
			int td = (int)(Math.pow((x1-x2), 2)+Math.pow((y1-y2), 2));
			if(td < d){
				key = i;
				d = td;
			}			
		}
		return roadMarks.get(key);
	}
	
	/**
	 * 判断当前位置是否在道路roadMark上
	 * @param roadMark  道路
	 * @param mapHeight 当前地图的高度，用于求当前的坐标点
	 * @param mapWidth  当前地图的宽度，用于求当前的坐标点
	 * @return flag     返回是否在地图上的标志
	 */
	protected boolean isInRoad(RoadMark roadMark){
		boolean flag = true;
		int mapHeight = mapView.getMapHeight();
		int mapWidth = mapView.getMapWidth();
		int x = screenX - mapView.getMapOffsetX();
		int y = screenY - mapView.getMapOffsetY();
		GeoPoint point = GeoPoint.getGeoPoint(mapHeight, mapWidth, x, y);
		
		Position position = point.getPosition();
		int weight = (int)Math.round(roadMark.getRoad().getWeight());
		int d1 = SearchRoadUtil.getDistance(position, roadMark.getBeginPosition());
		int d2 = SearchRoadUtil.getDistance(position, roadMark.getEndPosition());
		int d = Math.min(d1, d2);
		Log.i("roadMark-->", roadMark.getRoad().getCode());
		Log.i("roadWeight->",Integer.toString(weight)+" "+Integer.toString(d));
		if(d > weight){
			flag = false;
		}
		return flag;
	}
	
	public void moveAnim(int dx, int dy){
		screenX = screenX + dx;
		screenY = screenY + dy;
	}
	
	
	/**
	 * 判断当前位置是否在一组路上
	 * @return flag 判断当前位置是否在路上
	 */
	public boolean isInRoads(){
		boolean flag = false;
		RoadMark roadMark = findNearestRoadMark(roadMarks);
		flag = isInRoad(roadMark);
		return flag;
	}
	
	public void createDialog(final Context context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("友情提示");
		builder.setMessage("当前位置并不在最优路径上，是否重新导航");
		
		builder.setPositiveButton("取消导航",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						showDialog(context,"您已经取消导航");
					}
				});
		
		builder.setNeutralButton("重新导航",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						Intent intent = new Intent(context, 
								PathFindingActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("beginPosition","");
						bundle.putString("endPosition", "");
						intent.putExtras(bundle);
						context.startActivity(intent);
					}
				});
		builder.create().show();
	}	
	
	private void showDialog(Context context, String str) {
		new AlertDialog.Builder(context).setMessage(str).show();
	}
}
