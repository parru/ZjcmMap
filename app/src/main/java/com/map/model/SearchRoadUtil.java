package com.map.model;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.map.model.position.Position;
import com.map.model.position.PositionDao;
import com.map.model.road.Road;
import com.map.model.road.RoadDao;
import com.map.model.road.RoadMark;

/**
 * 道路查询的接口
 * @author Pingfu
 */
public class SearchRoadUtil {

	private static double EARTH_RADIUS = 6378137;   //地球的半径
	public static int [][]roadMap;    //地图的数据结构，采用矩阵存储方式
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
	
	/**
	 * 计算两个Position之间的坐标
	 * @param p1 坐标点p1
	 * @param p2 坐标点p2
	 */
	public static int getDistance(Position p1,Position p2){
		double latitude1 = p1.getLatitude();
		double longitude1 = p1.getLongitude();
		double latitude2 = p2.getLatitude();
		double longitude2 = p2.getLongitude();
		double radLat1 = rad(latitude1);
        double radLat2 = rad(latitude2);
        double a = radLat1 - radLat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2.0), 2)
                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2.0), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return (int) Math.round(s);
	}
	
	/**
	 * 得到点position到路roadMark的距离
	 * @param roadMark 道路
	 * @param position 点
	 * @return distance 点到路的距离
	 */
	public static int getDistance(RoadMark roadMark,Position position){
		int d1 = getDistance(roadMark.getBeginPosition(), position);
		int d2 = getDistance(roadMark.getEndPosition(), position);
		int distance = Math.min(d1, d2);
		return distance;
	}
	
	/**
	 * 得到距离点Position最近的道路
	 * @param position 坐标点的几何
	 * @param position 坐标点
	 * @return p       得到的最近的坐标点
	 */
	public static Position getNearstPosition(List<Position> positions,
			Position position){
		int key = 0;
		int minDistance = Integer.MAX_VALUE;
		for(int i = 0; i < positions.size(); i++){
			Position temp = positions.get(i);
			int tempDistance = getDistance(temp, position);
			if(minDistance > tempDistance && tempDistance > 0){
				key = i;
				minDistance = tempDistance;
			}
		}
		return positions.get(key);
	}
	
	/**
	 * 根据一系列点生成一条道路
	 * @param positions
	 * @param database
	 * @return
	 */
	public static List<RoadMark> getRoadMarksByListPosition(List<Position>positions,
			SQLiteDatabase database){
		List<RoadMark> roadMarks = new ArrayList<RoadMark>();
		for(int i = 0; i < positions.size() - 1; i++){
			Position beginPosition = positions.get(i);
			Position endPosition = positions.get(i+1);
			String code = "";
			int begin_id = Math.min(beginPosition.getId(), endPosition.getId());
			int end_id = Math.max(beginPosition.getId(), endPosition.getId());
			code += getCode(begin_id);
			code += getCode(end_id);
			Road road = new RoadDao(database).findRoadByCode(code);
			RoadMark roadMark = new RoadMark(road, beginPosition, endPosition);
			roadMarks.add(roadMark);
		}
		return roadMarks;
	}
	
	/**
	 * 根据路径获取道路上的坐标点
	 * @param path
	 * @param database
	 * @return
	 */
	public static List<Position> getPositionsInRoute(String path[],
			SQLiteDatabase database){
		List<Position> positions = new ArrayList<Position>();
		int length = path.length;
		for(int i = length - 1; i > -1; i--){
			int id = Integer.parseInt(path[i]);
			Position position = new PositionDao(database).getPositionById(id);
			Log.i("id-->",path[i]);
			positions.add(position);
		}
		return positions;
	}
	
	protected static String[] getPathRoute(String []path){
		for(int i = 0; i < path.length / 2; i++){
			String temp = path[i];
			path[i] = path[path.length-i];
			path[path.length - 1] = temp;
		}
		return path;
	}
	
	protected static String getCode(int i){
		if(i > 100){
			return Integer.toString(i);
		}
		else if(i > 10){
			return "0" + Integer.toString(i);
		}
		else{
			return "00" + Integer.toString(i);
		}
	}
	
}
