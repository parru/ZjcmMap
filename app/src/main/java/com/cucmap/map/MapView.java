package com.cucmap.map;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.WindowManager;

import com.cucmap.animation.MyPlaceIconAnim;
import com.cucmap.map.overlay.Overlay;
/**
 * MapView is a widget extends view, which is in charge of the 
 * 使用改空间
 * @author Administrator
 */
@SuppressLint({ "ViewConstructor", "DrawAllocation", "NewApi" })
public class MapView extends View{

	private int screenWidth;       //显示屏幕的宽
	private int screenHeight;      //显示屏幕的高
	private int deepZoom = 1;      //地图的缩放级别
	
	private int mapWidth;          //地图的宽
	private int mapHeight;         //地图的高
	private int mapOffsetX = 0;    //地图的x轴偏移
	private int mapOffsetY = 0;    //地图的Y轴偏移	
	
	private Paint mPaint;           //地图画笔
	private Context context;
	
	private float mScale;
	private DrawTileCache tileCache;
	private Bitmap bitmap;
	
	private MyPlaceIconAnim mPlaceAnimation;   //显示当前位置的动画
	private GestureDetector gestureDetector;
	private ScaleGestureDetector scaleGestureDetector;
	
	private MapControl mapControl;             //地图的控制对象
	private Map<Integer, Overlay> overlayMap;
	
	/*
	 * 初始化地图
	 */
	public MapView(Context context) {
		super(context);
		initMap(context);
	}
	
	public MapView(Context context, AttributeSet attr){
		super(context, attr);
		initMap(context);
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}
	
	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public int getDeepZoom() {
		return deepZoom;
	}
	
	public int getMapWidth() {
		return mapWidth;
	}

	public int getMapHeight() {
		return mapHeight;
	}
	
	public int getMapOffsetX() {
		return mapOffsetX;
	}
	
	public int getMapOffsetY() {
		return mapOffsetY;
	}
	
	public void setDeepZoom(int deepZoom) {
		this.deepZoom = deepZoom;
	}
	
	public Context getMapViewContext(){
		return this.context;
	}
	
	public MyPlaceIconAnim getMyPlaceIconAnim(){
		return this.mPlaceAnimation;
	}
	
	public Map<Integer, Overlay> getOverlayMap() {
		return overlayMap;
	}	
	
	public MapControl getMapControl(){
		if(mapControl == null){
			mapControl = new MapControl(this, screenHeight, screenWidth);
		}
		return mapControl;
	}
	
	/**
	 * 初始化地图数据
	 * @param context
	 */
	@SuppressLint({ "NewApi", "UseSparseArrays" })
	private void initMap(Context context){
		this.context = context;
		mPaint = new Paint();
		/*得到手机屏幕的大小*/
		WindowManager window = (WindowManager)
                                  (context.getSystemService(Context.WINDOW_SERVICE));
        Display display = window.getDefaultDisplay();
        screenHeight = display.getHeight();
        screenWidth = display.getWidth();
        if(screenHeight < MapConfig.MIN_MAP_SIZE){
        	deepZoom = 1;
        }else {
			deepZoom = 2;
		}
        mapWidth = MapConfig.getMapSizeByDeepZoom(deepZoom);
        mapHeight = mapWidth;        
        
        mScale = 1.0f;        
        tileCache = new DrawTileCache(context, deepZoom);
        mPlaceAnimation = new MyPlaceIconAnim(context, 50, 50, this);
        overlayMap = new HashMap<Integer, Overlay>();
        
        setMapPosition((screenWidth - mapWidth) / 2,
        		(screenHeight - mapHeight) / 2);
	    gestureDetector = new GestureDetector(context, new ScrollListener());
	    scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		int mTileSize = (int) (MapConfig.TILE_SIZE * mScale);
		int m = screenHeight / MapConfig.TILE_SIZE + 2;
		int n = screenWidth / MapConfig.TILE_SIZE + 2;
		int num = mapWidth / mTileSize;
		int MAXNUM = num * num;
		
		/*
		 * 画布上第一个切片的坐标
		 */
		int canvasX = mapOffsetX % mTileSize;
		int canvasY = mapOffsetY % mTileSize;
		
		int tile_num = 0;
		
		//拼接地图
		for(int i = 0; i < m; i++){
			for(int j = 0; j < n; j++){
				int screenX = canvasX + (j * mTileSize);
				int screenY = canvasY + (i * mTileSize);
				if( screenX - mapOffsetX > mapWidth 
						|| screenY - mapOffsetY > mapHeight){
					tile_num = 0;
				}else{
					tile_num = (i+ (-mapOffsetY) / mTileSize) * num
				             + (j + 1 + (-mapOffsetX) / mTileSize);
				}
				if(tile_num > MAXNUM || tile_num < 1){
					tile_num = 0;
				}
				bitmap = tileCache.getMapTile(tile_num, mScale);
				drawMapTile(canvas, bitmap, mPaint, 
						screenX, screenY, mTileSize);
			}
		}
		drawOverlayer(canvas);
		mPlaceAnimation.renderAnimation(canvas, mPaint);
	}
	
	/**
	 * 将某一地图切片绘制到指定位置
	 * @param canvas      画布
	 * @param bitmap      地图的一个切片
	 * @param paint       画笔
	 * @param screenX     该切片在画布上的X轴坐标
	 * @param screenY     该切片在画布上的Y轴坐标
	 * @param TITLE_SIZE  切片的大小（切片是正方形）
	 */
	protected void drawMapTile(Canvas canvas,Bitmap bitmap,Paint paint,
			                int screenX,int screenY,int mTileSize){
		if(canvas != null){
			canvas.save();
			canvas.clipRect(screenX, screenY, 
					screenX + mTileSize, screenY + mTileSize);
			canvas.drawBitmap(bitmap, screenX, screenY, paint);
			canvas.restore();
		}
	}

	/**
	 * 绘制地图上的逻辑图层
	 * @param canvas 当前的画布
	 */
	protected void drawOverlayer(Canvas canvas){
		for(Map.Entry<Integer, Overlay> entry: overlayMap.entrySet()){
			Overlay o = entry.getValue();
			if(o.isShowingOverlay()){
				o.draw(canvas, this.deepZoom);
			}
		}
	}
	
	/**
	 * 设置地图相对于手机屏幕的坐标
	 * @param mapOffsetX 地图在手机屏幕坐标系上X轴坐标
	 * @param mapOffsetY 地图在手机屏幕坐标系上Y轴坐标
	 */
	public void setMapPosition(int mapOffsetX,int mapOffsetY){
		if(mapOffsetX > 0){
			mapOffsetX = 0;
		}
		if(mapOffsetY > 0){
			mapOffsetY = 0;
		}
		this.mapOffsetX = mapOffsetX;
		this.mapOffsetY = mapOffsetY;
	}
	
	/**
	 * 移动图像
	 * @param dx 在X轴方向移动的距离
	 * @param dy 在Y轴方向移动的距离
	 */
	public void moveMap(int dx,int dy){
		mapOffsetX = mapOffsetX + dx;
        mapOffsetY = mapOffsetY + dy;
        
        if(mapOffsetX > screenWidth / 2){
        	mapOffsetX = screenWidth / 2;
        }else if(mapOffsetX < - mapWidth){
        	mapOffsetX = - mapWidth;
        }
        
        if(mapOffsetY > screenHeight / 2){
        	mapOffsetY = screenHeight / 2;
        }else if(mapOffsetY < - mapHeight){
        	mapOffsetY = - mapHeight;
        }
        mPlaceAnimation.moveAnim(dx, dy);
        postInvalidate();        
	}
	
	/**
	 * 将地图移动到指定的位置，
	 * 如果指定的位置在校园内，将指定的位置居中
	 * 如果指定的位置在校园外，不处理
	 * @param location    指定的位置
	 */
	public void moveToLocation(Location location){
		
	}
	
	/**
	 * 放大缩小地图
	 * @param factor
	 */
	public void scaleMap(float factor){
        mapHeight = (int)(mapHeight * factor);
        if(mapHeight < MapConfig.MIN_MAP_SIZE){
        	mapHeight = MapConfig.MIN_MAP_SIZE;
        }
        if(mapHeight > MapConfig.MAX_MAP_SIZE){
        	mapHeight = MapConfig.MAX_MAP_SIZE;
        }        
        mapWidth = mapHeight;
        
        int x = (int) (((1 - factor) / 2) * screenWidth + factor * mapOffsetX);
		int y = (int) (((1 - factor) / 2) * screenHeight + factor* mapOffsetY);
		int dx = x - mapOffsetX;
		int dy = y - mapOffsetY;
		//setMapPosition(x, y);
        
        int mDeepZoom = MapConfig.getDeepZoomByMapSize(mapHeight);
        if(mDeepZoom > deepZoom){
        	//放大
        	deepZoom++;
        	Log.i("###","放大########");
        	tileCache.cleanAllMapTiles();
        	tileCache.setDeepZoom(mDeepZoom);
        	mScale = MapConfig.getMapScale(mapHeight, mDeepZoom);
        }else if(mDeepZoom == deepZoom){
        	//
        	tileCache.cleanAllMapTiles();
        	mScale = MapConfig.getMapScale(mapHeight, mDeepZoom);
        }else{
        	deepZoom --;
        	Log.i("###","缩小########");
        	tileCache.setDeepZoom(mDeepZoom);
        	tileCache.cleanAllMapTiles();
        	mScale = MapConfig.getMapScale(mapHeight, mDeepZoom);
        }
        Log.i("MapHeight-->", "" + mapHeight);
        Log.i("DeepZoom-->", "" + deepZoom);
        Log.i("mScale-->", "" + mScale);
        moveMap(dx, dy);
        postInvalidate();
        
	}
	
	/**
     * 得到屏幕中点对应的经纬度坐标 
     */
    public GeoPoint getCenterPoint(){
    	int x = -mapOffsetX + mapWidth / 2;
    	int y = -mapOffsetY + mapHeight / 2;
    	Log.i("get_MapY-->", Integer.toString(y));
    	GeoPoint point = GeoPoint.getGeoPoint(mapHeight, mapWidth, x, y);
    	return point;
    }
    
    /**
     * 放大地图
     */
    public void zoomIn(){
    	Log.i("zoom_in","###############");
    	float factor = 1.3f;
    	scaleMap(factor);
    }
    
    /**
     * 缩小地图
     */
    public void zoomOut(){
    	Log.i("zoom_out","###############");
//    	if(deepZoom > MapManager.MIN_DEEP_ZOOM){
//        	deepZoom--;
//        	this.mapHeight = mapHeight / 2;
//        	this.mapWidth = mapWidth / 2;
//        	setDeepZoom(deepZoom);
//        	setMapPosition(screenWidth/4 + mapOffsetX / 2, 
//        			screenHeight/4 + mapOffsetY / 2);
//        	//setCenter(point);
//        	postInvalidate();
//    	}
    	float factor = 0.7f;
    	scaleMap(factor);
    }
    
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		if(event.getPointerCount() > 1){
	        scaleGestureDetector.onTouchEvent(event);
	    }else{
	    	switch (event.getAction()) {  
	        case MotionEvent.ACTION_DOWN:  
	            int touchX = (int) event.getRawX();  
	            int touchY = (int) event.getRawY();
	            for(Map.Entry<Integer, Overlay> entry: overlayMap.entrySet()){
	    			Overlay o = entry.getValue();
	    			if(o.isShowingOverlay()){
	    				o.onClick(touchX, touchY);
	    			}
	    		}
	            break;  
	        case MotionEvent.ACTION_MOVE:  
	            break;
	        case MotionEvent.ACTION_UP:
	          	break;
	        default:break;
			}
	        gestureDetector.onTouchEvent(event);
	    }		
		return true;
	}	
	
	class ScrollListener extends SimpleOnGestureListener {

	    @Override
	    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	            float velocityY) {
	        // TODO Auto-generated method stub
//	        int dx = (int)(velocityX / 5);
//	        int dy = (int)(velocityY / 5);
//	        moveMap(dx, dy);
	        return false;
	    }

	    @Override
	    public void onLongPress(MotionEvent e) {
	        // TODO Auto-generated method stub
	        
	    }

	    @Override
	    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
	            float distanceY) {
	        // TODO Auto-generated method stub
	        moveMap(- (int)distanceX, -(int)distanceY);
	        //thread.moveMap(- (int)distanceX, -(int)distanceY);
	        //thread.start();
	        return false;
	    }
	}
	
	class ScaleListener extends SimpleOnScaleGestureListener{

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // TODO Auto-generated method stub
            float d = (detector.getScaleFactor() - 1) / 6;
            scaleMap(1 + d);
            return super.onScale(detector);
        }
	}	
}
