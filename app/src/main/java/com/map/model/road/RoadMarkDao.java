package com.map.model.road;

import java.util.ArrayList;
import java.util.List;

import com.map.model.position.Position;
import com.map.model.position.PositionDao;

import android.database.sqlite.SQLiteDatabase;

public class RoadMarkDao {
    private SQLiteDatabase database;
    
    public RoadMarkDao(SQLiteDatabase database){
    	this.database = database;
    }
    
    /*
     * 根据road的id得到RoadMark
     * @param road_id road的id
     * @return roadMark 一个RoadMark对象
     */
    public RoadMark getRoadMarkById(int road_id){
    	Road road = new RoadDao(database).findRoadById(road_id);
    	String road_code = road.getCode();
    	String pString1 = road_code.substring(0, 3);
    	String pString2 = road_code.substring(3,road_code.length());
    	int beginPosition_id = Integer.parseInt(pString1);
    	int endPosition_id = Integer.parseInt(pString2);
    	
    	PositionDao positionDao = new PositionDao(database);
    	Position beginPosition = positionDao.getPositionById(beginPosition_id);
    	Position endPosition = positionDao.getPositionById(endPosition_id);
    	return new RoadMark(road, beginPosition, endPosition);
    }
    
    /*
     * 根据road的code得到RoadMark
     * @param road_id road的id
     * @return roadMark 一个RoadMark对象
     */
    public RoadMark getRoadMarkById(String code){
    	Road road = new RoadDao(database).findRoadByCode(code);
    	String pString1 = code.substring(0, 3);
    	String pString2 = code.substring(3,code.length());
    	int beginPosition_id = Integer.parseInt(pString1);
    	int endPosition_id = Integer.parseInt(pString2);
    	
    	PositionDao positionDao = new PositionDao(database);
    	Position beginPosition = positionDao.getPositionById(beginPosition_id);
    	Position endPosition = positionDao.getPositionById(endPosition_id);
    	return new RoadMark(road, beginPosition, endPosition);
    }
    
    /*
     * 得到图中所有的道路
     * @return roadMarks 返回图中的所有道路
     */
    public List<RoadMark> getAllRoadMarks(){
    	List<Road> roads= new RoadDao(database).findAllRoadsInMap();
    	List<RoadMark>roadMarks = new ArrayList<RoadMark>();
    	for(int i=0; i< roads.size();i++){
    		Road road = roads.get(i);
    		String code = road.getCode();
    		String pString1 = code.substring(0, 3);
        	String pString2 = code.substring(3,code.length());
        	int beginPosition_id = Integer.parseInt(pString1);
        	int endPosition_id = Integer.parseInt(pString2);
        	
        	PositionDao positionDao = new PositionDao(database);
        	Position beginPosition = positionDao.getPositionById(beginPosition_id);
        	Position endPosition = positionDao.getPositionById(endPosition_id);
        	roadMarks.add(new RoadMark(road, beginPosition, endPosition));
    	}
    	return roadMarks;
    }
}
