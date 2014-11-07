package com.map.core.map.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.map.R;


class OverlayItemConfig {
	/** OverlayItem属于建筑 */
    public static int BUILDING_ITEM_TYPE = 0;
    
    /** OverlayItem属于部门 */
    public static int FACILITY_ITEM_TYPE = 1;
    
    /** OverlayItem属于道路 **/
    public static int ROAD_ITEM_TYPE = 2;
    
    public static final int TURN_START = 10; 
    public static final int TURN_LEFT = 11;
    public static final int TURN_RIGHT = 12;
    public static final int TURN_UP = 13;
    public static final int TURN_DOWN = 14;
    public static final int TURN_END = 15;
    
    /**
	 * 根据Mark的编号设置Mark
	 * @param i Mark的编号
	 */
	public Bitmap getMarkerByNumber(Context context, int number){
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
		return bitmap;
	}
}
