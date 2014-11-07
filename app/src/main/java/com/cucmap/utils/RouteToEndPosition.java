package com.cucmap.utils;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.cucmap.data.position.Position;
import com.cucmap.data.position.PositionDao;

/*
 * 管理所有到终点的道路
 * @author Pingfu
 */
public class RouteToEndPosition {
    private Position endPosition;
    private String path[];
    
    public RouteToEndPosition(Position endPosition){
    	this.endPosition = endPosition;
    }
    
    public void initRoute(int map[][]){
    	Dijkstra dijkstra = new Dijkstra();
    	dijkstra.getShortestDistance(map, endPosition.getId());
    	path = dijkstra.getPath();
    }
    
    /*
     * 得到起始点beginPosition到终点的最短路径
     * @param beginPosition 起始点
     * @param database      系统的Sqlite数据库
     * @return route        起始点到终点的路径，由一组Position对象组成
     */
    public List<Position> getRouteToEndPosition(Position beginPosition,
    		SQLiteDatabase database){
    	List<Position> route = new ArrayList<Position>();
    	String road = path[beginPosition.getId()];
    	String temp[] = road.split("_");
    	for(int i = 0; i < temp.length; i++){
    		int id = Integer.parseInt(temp[i]);
    		Position position = new PositionDao(database).getPositionById(id);
    		route.add(position);
    	}
    	return route; 
    }
    
}
