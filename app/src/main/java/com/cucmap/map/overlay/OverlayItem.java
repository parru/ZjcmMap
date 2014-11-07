package com.cucmap.map.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.cucmap.map.GeoPoint;
import com.cucmap.map.MapView;
import com.cucmap.map.mark.ItemMark;
import com.zjcmmap.R;

/**
 * ItemizedOverlayOverlay图层上的最小数据单元
 * 建筑的标记、部门的标记、道路上的标记均由此生成
 * @author Pingfu
 */
public class OverlayItem{
	private GeoPoint point;       //坐标点    
    private String   description; //Item的提示信息
    private String   title;       //Item的名称    
    private Bitmap   marker;      //Item的标志（建筑标志30*30，道路标志10*10）
    private int      item_type;   //
    private Context  context;

    public boolean isClickable = true;
    
    public OverlayItem(GeoPoint point, String description,
    		String title, int item_type, Context context){
    	this.point = point;
    	this.description = description;
    	this.title = title;
    	this.item_type = item_type;
    	this.context = context;
    }

	public GeoPoint getPoint() {
        return point;
    }

    public void setPoint(GeoPoint point) {
        this.point = point;
    }

    public Bitmap getMarker() {
		return marker;
	}

	public void setMarker(Bitmap marker) {
		this.marker = marker;
	}

	public boolean isClickable() {
		return isClickable;
	}

	public void setClickable(boolean isClickable) {
		this.isClickable = isClickable;
	}
	
	public Context getContext(){
		return this.context;
	}
	
    public void setItemType(int item_type){
		this.item_type = item_type;
	}
	
	/**
	 * 根据Mark的编号设置Mark
	 * @param number Mark的编号
	 */
	public Bitmap getMarkerByNumber(int number){
		Bitmap bitmap = null;
		switch(number){
		case 0:
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon_marka);
			break;
		case 1:
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon_markb);
			break;
		case 2:
			bitmap = BitmapFactory.decodeResource(context.getResources(), 
					R.drawable.icon_markc);
			break;
		case 3:
			bitmap = BitmapFactory.decodeResource(context.getResources(), 
					R.drawable.icon_markd);
			break;
		case 4:
			bitmap = BitmapFactory.decodeResource(context.getResources(), 
					R.drawable.icon_marke);
			break;
		default:
			bitmap = BitmapFactory.decodeResource(context.getResources(), 
					R.drawable.icon_mark);
		}
		this.marker = bitmap;
		return bitmap;
	}
	
	/**
	 * 道路导航标志
	 * @param direction  道路的转向
	 * @return bitmap    道路转向的标志
	 */
	public Bitmap getMarkByDirection(int direction){
		Bitmap bitmap = null;
		switch (direction) {
		case OverlayItemConfig.TURN_START:
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.nav_start_mark);
			break;
			
		case OverlayItemConfig.TURN_LEFT:
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.road_left);
			break;
			
		case OverlayItemConfig.TURN_RIGHT:
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.road_right);
			break;
			
		case OverlayItemConfig.TURN_UP:
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.road_up);
			break;
			
		case OverlayItemConfig.TURN_DOWN:
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.road_down);
			break;
			
		case OverlayItemConfig.TURN_END:
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.nav_dest_mark);
			break;
			
		default:
			break;
		}
		this.marker = bitmap;
		return bitmap;
	}
	
	/**
	 * 判断当前Item是否被点中
	 * @param event   点击事件
	 * @param mapView 一个mapView对象
	 */
	public void onClick(int x, int y, MapView mapView, ItemMark itemMark){
		//如果当前的Item是可点击的
		if(isClickable){
			int mapWidth = mapView.getMapWidth();
			int mapHeight = mapView.getMapHeight();
			int mapOffsetX = mapView.getMapOffsetX();
			int mapOffsetY = mapView.getMapOffsetY();
			
			//当前Item在屏幕上的X轴坐标
			int mapX = point.getMapX(mapWidth) + mapOffsetX;   
			 //当前Item在屏幕上的Y轴坐标
			int mapY = point.getMapY(mapHeight) + mapOffsetY;  
			Log.i("longitude",Double.toString(point.getPosition().getLongitude()));
			Log.i("Item-->", Integer.toString(mapX)+" "+Integer.toString(mapWidth));
			
			/* 图像的画布大小是30*30 * 所以要计算是否点击了当前的Item对象 */
			if(Math.abs((x -mapX)) < 40 && Math.abs(y - (mapY + 30)) < 40){
				if(item_type == OverlayItemConfig.BUILDING_ITEM_TYPE){
				    ItemPopupWindow popupWindow = new ItemPopupWindow(context,
				            itemMark, title);
				    
					popupWindow.showItemPopupWindow(mapView, mapX, mapY);
				}
				else{
				    ItemPopupWindow popupWindow = new ItemPopupWindow(context,
                            itemMark, title);
					popupWindow.showNavPopupWindow(mapView, mapX, mapY);
				}
			}
		}
	}
	
	/**
	 * 显示提示信息
	 * @param context
	 */
	public void showToastInfo(Context context){
		Log.i("route-->", "LSDSFSDFSFD");
		Toast toast = Toast.makeText(context.getApplicationContext(), 
				description, Toast.LENGTH_LONG); 
		toast.setGravity(Gravity.CENTER, 0, 0); 
		toast.show();
	}
}
