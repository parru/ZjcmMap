package com.map.core.map.overlay;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.Log;

import com.map.model.road.RoadMark;
import com.map.core.map.GeoPoint;
import com.map.core.map.MapView;

/*
 * 道路图层，用来添加道路信息
 */
public class RouteOverlay extends Overlay {

	private List<RoadMark> roadMarks;
	private List<OverlayItem> items;
	public RouteOverlay(MapView mapView) {
		super(mapView);
		// TODO Auto-generated constructor stub
	    items = new ArrayList<OverlayItem>();
	}
	
	public List<RoadMark> getRoadMarks() {
		return roadMarks;
	}

	public List<OverlayItem> getItems() {
		return items;
	}

	public void setRoadMarks(List<RoadMark> roadMarks) {
		this.roadMarks = roadMarks;
	}

	public void setItems(List<OverlayItem> items) {
		this.items = items;
	}

    /*
     * 添加一个Item对象
     * @param item 一个Item对象
     */
	
	public void addItem(OverlayItem item){
		items.add(item);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.fpf.map.overlayer.OverLayer#draw(android.graphics.Canvas, int)
	 * @param canvas 画布
	 * @param deepZoom 当前地图的缩放等级
	 */
	@Override
	public void draw(Canvas canvas, int deepZoom) {
		// 判断当前图层是否显示
		if(!isShowingOverlay()){
			return;
		}
//		OverlayItemUtls itemUtls = new OverlayItemUtls(getMapView());
//		items = itemUtls.getItemsByRoadMarks(roadMarks);
		Log.i("ItemSize-->",""+items.size());
		Paint paint = new Paint();
		PaintFlagsDrawFilter pfd;
		pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		canvas.setDrawFilter(pfd);
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
		drawOverlayItems(canvas, paint);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.cucmap.map.overlay.Overlay#onClick(int, int)
	 * @param x 触摸点在手机屏幕上的X轴坐标
	 * @param y 触摸点在手机屏幕上的Y轴坐标
	 */
	@Override
	public void onClick(int x, int y) {
		// TODO Auto-generated method stub
		for(int i = 0;i < items.size();i++){
			items.get(i).onClick(x, y, getMapView(),null);
		}
	}
	
	/**
	 * 判断一条路是否在当前的屏幕上, 如果在当前屏幕，就画出来，否则就不画这条路
	 * @param startPoint  道路的起始点
	 * @param endPoint    道路的终止点
	 * @return 这条路在屏幕上就返回 true，否则就返回 false
	 */
	protected boolean isRoadInScreen(GeoPoint startPoint, GeoPoint endPoint){
		boolean flag = false;
		int mapHeight = getMapView().getMapHeight();
		int mapWidth = getMapView().getMapWidth();
		int screenHeight = getMapView().getScreenHeight();
		int screenWidth = getMapView().getScreenWidth();
		
		int x1 = startPoint.getMapX(mapWidth) + getMapView().getMapOffsetX();
		int y1 = startPoint.getMapY(mapHeight)+ getMapView().getMapOffsetY();
		int x2 = endPoint.getMapX(mapWidth) + getMapView().getMapOffsetX();
		int y2 = endPoint.getMapY(mapHeight)+ getMapView().getMapOffsetY();
		
		if(((x1 > 0 && x1 < screenWidth)&&(y1 > 0 && y1 < screenHeight))
			    || ((x2>0&&x2<screenWidth)&&(y2>0&&y2<screenHeight))){
			flag = true;
		}		
		return flag;
	}
	
	/**
	 * 在图层上画图标，
	 * 起点和终点的位置使用专门的Mark标识
	 * 路口的标识是一个小绿点
	 * @param canvas
	 * @param item
	 */
	protected void drawMapTile(Canvas canvas, OverlayItem item, 
			Paint paint, boolean flag){
		int mapWidth = getMapView().getMapWidth();
		int mapHeight = getMapView().getMapHeight();
		GeoPoint point = item.getPoint();
		int x = point.getMapX(mapWidth)+ getMapView().getMapOffsetX();
		int y = point.getMapY(mapHeight) + getMapView().getMapOffsetY();
		
		if(flag){
			//起点和终点的标注大小为30 * 30
			if(item.getMarker() != null){
			    canvas.drawBitmap(item.getMarker(), x - 30, y - 30, paint);
			}
			Log.i("Start-->", "####");
		}else{
			//中间点的标注大小为10 * 10
			if(item.getMarker() != null){
				canvas.drawBitmap(item.getMarker(), x - 5, y - 10, paint);
			}
		}
	}
	
	protected void drawLine(Canvas canvas, GeoPoint startGeoPoint,
			GeoPoint endGeoPoint, Paint paint) {
		int mapHeight = getMapView().getMapHeight();
		int mapWidth = getMapView().getMapWidth();
		int screenHeight = getMapView().getScreenHeight();
		int screenWidth = getMapView().getScreenWidth();	
		
		int x1 = startGeoPoint.getMapX(mapWidth) + getMapView().getMapOffsetX();
		int y1 = startGeoPoint.getMapY(mapHeight)+ getMapView().getMapOffsetY();
		int x2 = endGeoPoint.getMapX(mapWidth) + getMapView().getMapOffsetX();
		int y2 = endGeoPoint.getMapY(mapHeight)+ getMapView().getMapOffsetY();
		if( ((x1 > 0 && x1 < screenWidth)&&(y1 > 0 && y1 < screenHeight))
		    || ((x2>0&&x2<screenWidth)&&(y2>0&&y2<screenHeight))){
			
			canvas.drawLine(x1, y1, x2, y2, paint);
		}
	}
	
	protected void drawOverlayItems(Canvas canvas, Paint paint){
		//items = new OverlayItemUtls(getMapView()).getItemsByRoadMarks(roadMarks);
		int size = items.size();
		canvas.save();
		drawMapTile(canvas, items.get(0), paint, true);
		for(int i = 1; i < size; i++){
			GeoPoint startPoint = items.get(i-1).getPoint();
			GeoPoint endPoint = items.get(i).getPoint();
			if(isRoadInScreen(startPoint, endPoint)){
				drawLine(canvas, startPoint, endPoint, paint);
			}
			if( i < size - 1){
				drawMapTile(canvas, items.get(i), paint, false);
			}
			
		}
		drawMapTile(canvas, items.get(size - 1), paint, true);
		canvas.restore();
	}
}
