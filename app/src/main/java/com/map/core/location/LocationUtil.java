package com.map.core.location;

import android.location.Location;

import com.map.core.map.GeoPoint;

public class LocationUtil {
	
	/**
	 * 将当前的Location转换成地图上的坐标点
	 * @param location  当前的GPS坐标 
	 * @return point    GPS坐标对应的Map上的GeoPoint坐标
	 */
	public static GeoPoint getGeoPoint(Location location){
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		GeoPoint point = new GeoPoint("", longitude, latitude);
		return point;
	}
	
}