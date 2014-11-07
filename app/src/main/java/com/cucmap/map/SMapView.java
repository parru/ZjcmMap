package com.cucmap.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.cucmap.animation.MyPlaceIconAnim;
import com.cucmap.map.overlay.Overlay;

@SuppressLint("ViewConstructor")
public class SMapView extends SurfaceView implements Callback,Runnable{

	private static final int TILE_SIZE = 75;
	private int screenWidth = 320;       //显示屏幕的宽
	private int screenHeight = 480;      //显示屏幕的高
	private int screenX = 0;       //屏幕的X轴坐标
	private int screenY = 0;       //屏幕的Y轴坐标
	private int deepZoom = 1;      //地图的缩放级别
	
	private int mapWidth;          //地图的宽
	private int mapHeight;         //地图的高
	private int mapOffsetX = 0;    //地图的x轴偏移
	private int mapOffsetY = 0;    //地图的Y轴偏移	
	
	private int touchX = 0;        //触摸点的X轴坐标
	private int touchY = 0;        //触摸点的Y轴坐标
	
	private Paint paint;           //地图画笔
	private Context context;
	private Canvas mCanvas = null;
	
	private MyPlaceIconAnim myplace_animation;   //显示当前位置的动画
	
	private SurfaceHolder mySurfaceHolder = null;
	private Thread thread = null;
	private boolean isRunning = false;
	
	private Bitmap [][]bitmaps;
	private Bitmap background_bitmap = null;
	private MapControl mapControl;    //地图的控制对象
	private List<Overlay> overlays;   //地图的应用图层
	private Map<Integer, Overlay> overlayMap;
	
	/*
	 * 初始化地图
	 */
	public SMapView(Context context) {
		super(context);
		
		this.context = context;
		this.paint = new Paint();
		
		int height = MapManager.INIT_MAP_HEIGHT;
		int width = MapManager.INIT_MAP_WIDTH;
		
		setMapPosition(0, 0);
		initMap(height, width);
		mySurfaceHolder = getHolder();
		mySurfaceHolder.addCallback(this);
		setFocusable(true);
		
		//myplace_animation = new MyPlaceIconAnim(context, 10, 10, this);
		//mapControl = new MapControl(this, screenHeight, screenWidth);
		overlays = new ArrayList<Overlay>();
		overlayMap = new TreeMap<Integer, Overlay>();
		
		int m = screenHeight / TILE_SIZE + 2;
		int n = screenWidth / TILE_SIZE + 2;
		bitmaps = new Bitmap[m][n];
		initBitmaps();
		background_bitmap = new MapTile(context).getBitmapById(deepZoom, 0);
	}
	
	public SMapView(Context context, AttributeSet attr){
		super(context, attr);
		int height = MapManager.INIT_MAP_HEIGHT;
		int width = MapManager.INIT_MAP_WIDTH;
		
		setMapPosition(0, 0);
		initMap(height, width);
		mySurfaceHolder = getHolder();
		mySurfaceHolder.addCallback(this);
		setFocusable(true);
		
		//myplace_animation = new MyPlaceIconAnim(context, 10, 10, this);
		//mapControl = new MapControl(this, screenHeight, screenWidth);
		overlays = new ArrayList<Overlay>();
		overlayMap = new TreeMap<Integer, Overlay>();
		
		int m = screenHeight / TILE_SIZE + 2;
		int n = screenWidth / TILE_SIZE + 2;
		bitmaps = new Bitmap[m][n];
		initBitmaps();
		background_bitmap = new MapTile(context).getBitmapById(deepZoom, 0);
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
	
	public MyPlaceIconAnim getMyPlaceIconAnim(){
		return this.myplace_animation;
	}
	
	public List<Overlay> getOverlays(){
		return this.overlays;
	}
	
	public int getMapOffsetX() {
		return mapOffsetX;
	}
	
	public int getMapOffsetY() {
		return mapOffsetY;
	}
	
	public void setDeepZoom(int deepZoom) {
		this.deepZoom = deepZoom;
		initBitmaps();
	}
	
	public void setMapWidth(int mapWidth) {
		this.mapWidth = mapWidth;
	}
	
	public void setMapHeight(int mapHeight) {
		this.mapHeight = mapHeight;
	}
	
	public void setMapOffsetX(int mapOffsetX) {
		this.mapOffsetX = mapOffsetX;
	}
	
	public void setMapOffsetY(int mapOffsetY) {
		this.mapOffsetY = mapOffsetY;
	}

	public Map<Integer, Overlay> getOverlayMap() {
		return overlayMap;
	}	
	
	public void setOverLayerMap(Map<Integer, Overlay> overlayMap) {
		this.overlayMap = overlayMap;
	}
	
	
	public void initBitmaps(){
		int m = screenHeight / TILE_SIZE + 2;
		int n = screenWidth / TILE_SIZE + 2;
		int tile_num = 0;
		int num = mapWidth / TILE_SIZE;
		MapTile mapTile = new MapTile(context);
		int MAXNUM = num * num;
		for(int j = 0; j < m; j++){
			for(int i = 0; i < n; i++){
				tile_num=(j+(-mapOffsetY)/TILE_SIZE)*num 
				           + (i + 1 + (-mapOffsetX) / TILE_SIZE);
	            if(tile_num > MAXNUM || tile_num < 1){
		            tile_num = 0;
	            }
	            Bitmap bitmap = mapTile.getBitmapById(deepZoom, tile_num);
	            bitmaps[j][i] = bitmap;
			}
		}
	}
	
	public void moveBitmaps(int dx, int dy){
		dx += mapOffsetX % TILE_SIZE;
		dy += mapOffsetY % TILE_SIZE;
		
		int a = -dx / TILE_SIZE;
		int b = -dy / TILE_SIZE;
		int m = screenHeight / TILE_SIZE + 2;
		int n = screenWidth / TILE_SIZE + 2;
		int num = mapHeight / TILE_SIZE;
		MapTile mapTile = new MapTile(context);
		for(int j = 0; j < m; j++){
			for(int i = 0; i < n; i++){
				int temp_i = i + a;
				int temp_j = j + b;
				if(temp_i >= n || temp_i < 0 || temp_j >= m || temp_j < 0){
					int tile_num=(j+(-mapOffsetY)/TILE_SIZE)*num 
			               + (i + 1 + (-mapOffsetX) / TILE_SIZE);
                    if(tile_num > num * num || tile_num < 1){
	                    tile_num = 0;
                    }
                    Bitmap bitmap = mapTile.getBitmapById(deepZoom, tile_num);
                    bitmaps[j][i] = null;
                    bitmaps[j][i] = bitmap;
				}else{
					bitmaps[j][i] = bitmaps[temp_j][temp_i];
				}
			}
		}
	}
	
	/*
	 * 得到MapControl对象
	 */
	public MapControl getMapControl(){
		if(this.mapControl == null){
			//return new MapControl(this, this.screenHeight, this.screenWidth);
		}
		else {
			return this.mapControl;
		}
		return null;
	}
	
	/*
	 * 绘制地图上的逻辑图层
	 * @param canvas 当前的画布
	 */
	public void drawOverlayer(Canvas canvas){
		for(Map.Entry<Integer, Overlay> entry: overlayMap.entrySet()){
			Overlay o = entry.getValue();
			if(o.isShowingOverlay()){
				o.draw(canvas, this.deepZoom);
			}
		}
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		isRunning = false;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRunning){
			synchronized (mySurfaceHolder) {
				try{
					mCanvas = mySurfaceHolder.lockCanvas(null);
					
					drawMap(mCanvas, deepZoom);
					drawOverlayer(mCanvas);
					if (myplace_animation.isShowMyPlaceAnim()) {
						myplace_animation.renderAnimation(mCanvas, paint);
					}
					Thread.sleep(100);
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}finally{
					if(mCanvas != null){
						mySurfaceHolder.unlockCanvasAndPost(mCanvas);
					}
				}
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
	
	/*
	 * 初始化地图
	 * @param height 地图的高度
	 * @param width  地图的宽度
	 */
	public void initMap(int height,int width){
		this.mapHeight = height;
		this.mapWidth = width;
	}
	
	/*
	 * 绘制对图
	 * @param canvas    当前的画布
	 * @param deep_zoom 地图的缩放级别
	 */
	protected void drawMap(Canvas canvas,int deep_zoom) {
		MapTile mapTile = new MapTile(getContext());
		int tile_num = 0;
		int m = screenHeight / TILE_SIZE + 2;
		int n = screenWidth / TILE_SIZE + 2;
		int num = mapWidth / TILE_SIZE;
		int MAXNUM = num * num;
		
		//拼接地图	
		for(int i = 0; i < m; i++){
			for(int j = 0; j < n; j++){
				tile_num = (i+(-mapOffsetY)/TILE_SIZE)*num
				             + (j + 1 + (-mapOffsetX) / TILE_SIZE);
				if(tile_num > MAXNUM || tile_num < 1){
					tile_num = 0;
				}
				Bitmap bitmap = mapTile.getBitmapById(deep_zoom, tile_num);
				screenX = (mapOffsetX % TILE_SIZE) + (j * TILE_SIZE);
				screenY = (mapOffsetY % TILE_SIZE) + (i * TILE_SIZE);
				drawMapTile(canvas, bitmap, paint, screenX,	screenY, TILE_SIZE);
			}
		}
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
	public void drawMapTile(Canvas canvas,Bitmap bitmap,Paint paint,
			                int screenX,int screenY,int TILE_SIZE){
		if(canvas != null){
			canvas.save();
			canvas.clipRect(screenX,screenY,screenX+TILE_SIZE,screenY+TILE_SIZE);
			canvas.drawBitmap(bitmap, screenX,screenY, paint);
			canvas.restore();
		}
	}

	/**
	 * 移动图像
	 * @param dx 在X轴方向移动的距离
	 * @param dy 在Y轴方向移动的距离
	 */
	public void moveMap(int dx,int dy){
		int x = mapOffsetX;
		int y = mapOffsetY;
		mapOffsetX = mapOffsetX + dx;
        mapOffsetY = mapOffsetY + dy;
       
        //设置地图位移的X轴编辑
        int a = screenWidth - mapWidth;
        if( a > 0){
        	//地图过小，屏幕宽度大于地图宽度
        	if(mapOffsetX < 0){
        		mapOffsetX = 0;
        	}else if(mapOffsetX > a){
        		mapOffsetX = a;
        	}
        }else{
        	//地图大于屏幕
        	if(mapOffsetX > 0){
        		mapOffsetX = 0;
        	}else if(mapOffsetX < a){
        		mapOffsetX = a;
        	}
        }
        
        //设置地图位移的Y轴边界
        int b = screenHeight - mapHeight;
        if( b > 0){
        	//地图过小，屏幕宽度大于地图宽度
        	if( mapOffsetY < 0){
        		mapOffsetY = 0;
        	}else if(mapOffsetY > b){
        		mapOffsetY = b;
        	}
        }else{
        	if( mapOffsetY > 0){
        		mapOffsetY = 0;
        	}else if(mapOffsetY < b){
        		mapOffsetY = b;
        	}
        }
        initBitmaps();
        myplace_animation.moveAnim(mapOffsetX - x, mapOffsetY - y);
	}

	public boolean moveMap(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY){
	    
	    return false;
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		myplace_animation.setState(false);
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
//		int dx = 0,dy = 0;
		Log.i("MapView-->", "#############");
		switch (event.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
            touchX = (int) event.getRawX();  
            touchY = (int) event.getRawY();
            for(Map.Entry<Integer, Overlay> entry: overlayMap.entrySet()){
    			Overlay o = entry.getValue();
    			if(o.isShowingOverlay()){
    				o.onClick(touchX, touchY);
    			}
    		}
            break;
        case MotionEvent.ACTION_MOVE:  
//            dx = (int) event.getRawX() - touchX;  
//            dy = (int) event.getRawY() - touchY; 
            break;
        case MotionEvent.ACTION_UP:
          	break;
        default:break;
		}	
		//moveMap(dx, dy);        //移动图像
//		if(myplace_animation.isShowMyPlaceAnim()){
//			myplace_animation.moveAnim(dx, dy);
//		}
		return true;
	}	
}
