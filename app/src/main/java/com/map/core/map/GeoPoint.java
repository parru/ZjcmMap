package com.map.core.map;

import com.map.model.position.Position;

/*
 * Conversion between the latitude and longitude coordinates and map coordinates
 */
public class GeoPoint {
	private final static double LEFT_TOP_LATITUDE = 39.915402;
	private final static double LEFT_TOP_LONGITUDE = 116.544283;
	
	private final static double RIGHT_BOTTOM_LONGITUDE = 116.560722;
	private final static double RIGHT_BOTTOM_LATITUDE = 39.906872;
	
	public final static GeoPoint SOUTH_POINT = new GeoPoint("",116.550346,39.908675);
	public final static GeoPoint NOUTH_POINT = new GeoPoint("", 116.549994, 39.914489);
	public final static GeoPoint EAST_POINT = new GeoPoint("", 116.554505, 39.912296);
	public final static GeoPoint WEST_POINT = new GeoPoint("", 116.548663, 39.911806);
	public final static GeoPoint CENTER_POINT = new GeoPoint("",116.552183, 39.912115);
	private String name = null;
    private double longitude;
    private double latitude;
    
    public GeoPoint(String name, double longitude, double latitude){
    	this.name = name;
    	this.longitude = longitude;
    	this.latitude = latitude;    	
    }
    
    /*
     * 得到该坐标点在地图坐标系上的X轴坐标
     * @param mapWidth 当前地图的宽
     * @return x 当前该点在地图坐标系中X轴坐标
     */
    public int getMapX(int mapWidth){
		double delta_x =  (RIGHT_BOTTOM_LONGITUDE - LEFT_TOP_LONGITUDE) / mapWidth;
		int x = (int)((longitude - LEFT_TOP_LONGITUDE) / delta_x);
		return x ;
    }
    
    /*
     * 得到该坐标点在地图坐标系上的Y轴坐标
     * @param mapHeight 当前地图的高
     * @return y 当前该点在地图坐标系中的Y轴坐标
     */
    public int getMapY(int mapHeight){
		double delta_y = (LEFT_TOP_LATITUDE - RIGHT_BOTTOM_LATITUDE) / mapHeight;
		int y = (int)((LEFT_TOP_LATITUDE - latitude) / delta_y);
		return y;
    }    
    
    /*
     * 得到Positon对象
     * @return postion Position对象
     */
    public Position getPosition(){
    	Position position = new Position(-1, this.longitude, this.latitude, this.name);
    	return position;
    }
    
    
    /*
     * 根据当前地图上任意一点的坐标计算该点的经纬度坐标，并生成GeoPoint对象
     * @param mapHeight 当前地图的高
     * @param mapWidth 当前地图的宽
     * @param x 当前坐标点在地图坐标系上的X轴坐标
     * @param y 当前坐标点在地图坐标系上的Y轴坐标
     * @return point 新生成的GeoPoint对象
     */
    public static GeoPoint getGeoPoint(int mapHeight,int mapWidth,int x,int y){
    	 //delta_x代表每个像素对应多少经度
    	double delta_x =  (RIGHT_BOTTOM_LONGITUDE - LEFT_TOP_LONGITUDE) / mapWidth;
    	double longitude = ((double)x * delta_x) + LEFT_TOP_LONGITUDE;
    	
    	//delta_y代表每个像素对应多少纬度
    	double delta_y = (LEFT_TOP_LATITUDE - RIGHT_BOTTOM_LATITUDE) / mapHeight;
    	double latitude = ((double) y * delta_y) + RIGHT_BOTTOM_LATITUDE;
    	GeoPoint point = new GeoPoint("",longitude, latitude);
    	return point;
    }
    
    /*
     * 将数据库中的Position对象转化为比较实用的GeoPoint对象
     * @param Position 数据库中的Position对象
     * @return point 新生成的GeoPoint对象
     */
    public static GeoPoint getGeoPoint(Position position){
    	String name = position.getName();
    	double longitude = position.getLongitude();
    	double latitude = position.getLatitude();
    	GeoPoint point = new GeoPoint(name, longitude, latitude);
    	return point;
    }
}
