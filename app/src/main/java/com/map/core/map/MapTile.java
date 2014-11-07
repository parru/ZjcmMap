/*
 * 负责地图切片的获取（从Assets中获取）
 */

package com.map.core.map;

import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MapTile {
    private Context context;
    public static final int TILE_SIZE = 75;
    public static final int DEFAULT_MAP_SIZE = 750;
    
    public MapTile(Context context){
    	this.context = context;
    }
    public Bitmap getBitmapById(int zoom_level,int id){
    	String pathString = "zoom_"+Integer.toString(zoom_level) + "/";
    	String idString = null;
    	if(id < 10){
    		idString = "0"+Integer.toString(id);
    	}
    	else{
    		idString = Integer.toString(id);
    	}
    	pathString += "cucmap_" + idString + ".png";
    	Bitmap bitmap = getImageFromeAssets(pathString);
    	return bitmap;
    }
    public Bitmap getImageFromeAssets(String path){
    	Bitmap image = null;
    	try{
    		AssetManager am = context.getAssets();
    		InputStream is = am.open(path);
    		image = BitmapFactory.decodeStream(is);
    		is.close();
    	}catch (Exception e) {
			// TODO: handle exception
		}
    	return image;
    }
}
