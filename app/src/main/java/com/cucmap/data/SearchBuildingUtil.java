package com.cucmap.data;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.cucmap.data.building.Building;
import com.cucmap.data.building.BuildingMark;
import com.cucmap.data.building.BuildingMarkDao;
import com.cucmap.data.building.BuildingPositionDao;
import com.cucmap.data.position.Position;


/*
 * 建筑查询的API
 * @author Pingfu
 */
public class SearchBuildingUtil {	
	
	/**
	 * 根据名称查找建筑，建筑的名称相似，所以可能查询到多个名称相似的建筑，
	 * 查询的结果放在List中
	 * @param database 数据库
	 * @param name 要查找的建筑名称
	 * @return buildingMarks 所有和名称name类似的建筑列表
	 */
    public static List<BuildingMark> getBuildingMarkByName(String name,
    		SQLiteDatabase database){
    	List<BuildingMark> buildingMarks = new ArrayList<BuildingMark>();
    	buildingMarks = new BuildingMarkDao(database).getBuildingMarkByName(name);
    	return buildingMarks;
    }
    
    /**
     * 根据id得到对应的建筑，建筑的ID唯一，故返回一个BuildingMark
     * @param id            建筑的id
     * @param database      Sqlite数据库
     * @return buildingMark 对应的BuildingMark对象
     */
    public static BuildingMark getBuildingMarkById(int id,SQLiteDatabase database){
    	BuildingMark buildingMark = null;
    	BuildingMarkDao buildingMarkDao = new BuildingMarkDao(database);
    	buildingMark = buildingMarkDao.getBuildingMarkById(id);
    	return buildingMark;
    }
    
    /**
     * 根据编号的到对应的建筑, 建筑的编号唯一，故返回一个BuildingMark
     * @param num           建筑的编号number
     * @param database      Sqlite数据库
     * @return buildingMark 对应的BuildingMark对象
     */
    public static BuildingMark getBuildingMarkByNum(int num,
    		SQLiteDatabase database){
    	BuildingMark buildingMark = null;
    	BuildingMarkDao buildingMarkDao = new BuildingMarkDao(database);
    	buildingMark = buildingMarkDao.getBuildingMarkByNumber(num);
    	return buildingMark;
    }
    
    /**
     * 得到距离建筑最近的点
     * @param building   对应的建筑
     * @param database   Sqlite数据库
     * @return Position  距离建筑building最近的道路上的点
     */
    public static Position getNearestPositionInRoad(Building building,
    		SQLiteDatabase database){
    	int b_id = building.getId();
    	BuildingPositionDao buildingPositionDao = new BuildingPositionDao(database);
    	Position position = buildingPositionDao.getBuidingPosition(b_id, 0);
    	return position;
    }
    
    /**
     * 查找距离一点最近的N个建筑，如果N过大，超过建筑的总数，就返回所有的建筑
     * @param position   当前的坐标点
     * @param n          要查询的建筑个数
     * @param database   Sqlite数据库
     * @return buildingMarks 查询的结果
     */
    public static List<BuildingMark> getNearBuildingMarks(Position position, 
    		int n, SQLiteDatabase database){
    	List<BuildingMark> buildingMarks = new ArrayList<BuildingMark>();
    	
    	return buildingMarks;
    }
    
}
