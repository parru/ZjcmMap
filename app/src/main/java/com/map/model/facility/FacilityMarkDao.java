package com.map.model.facility;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.map.model.building.BuildingFacilityDao;
import com.map.model.building.BuildingMark;
import com.map.model.building.BuildingMarkDao;

/*
 *
 * @author Pingfu
 */
public class FacilityMarkDao {
    private SQLiteDatabase database;
    
    public FacilityMarkDao(SQLiteDatabase database){
    	this.database = database;
    }
    
    /*
     * 根据Facility的id查找Facility
     * @param id 待查的Facility的id
     * @return facilityMark 一个Facility对象
     */
    public FacilityMark getFacilityMarkById(int id){
    	FacilityMark facilityMark = null;
    	Facility facility = new FacilityDao(database).getFacilityById(id);
    	int b_id = new BuildingFacilityDao(database).getBuildingIdByFacilityId(id);
    	BuildingMark buildingMark = new BuildingMarkDao(database).getBuildingMarkById(b_id);
    	
    	facilityMark = new FacilityMark(facility, buildingMark);    	
    	return facilityMark;
    }
    
    /*
     * 根据Facility的name查找Facility
     * @param name 待查的Facility的name
     * @return facilityMarks 与name相似的Facility对象列表
     */
    public List<FacilityMark> getFacilityMarkByName(String name){
    	List<FacilityMark> facilityMarks = new ArrayList<FacilityMark>();
    	List<Facility> facilities = new FacilityDao(database).getFacilityByName(name);
    	for(int i = 0; i < facilities.size(); i++){
    		int f_id = facilities.get(i).getId();
    		int b_id = new BuildingFacilityDao(database).getBuildingIdByFacilityId(f_id);
    		BuildingMark buildingMark = new BuildingMarkDao(database).getBuildingMarkById(b_id);
    		FacilityMark facilityMark = new FacilityMark(facilities.get(i), buildingMark);
    		facilityMarks.add(facilityMark);
    	}    	
    	return facilityMarks;
    }
    
    /*
     * 根据Facility的name查找Facility
     * @param name 待查的Facility的name
     * @return facilityMarks 与name相似的Facility对象列表
     */
    public List<FacilityMark> getFacilityMarkByType(int type){
    	List<FacilityMark> facilityMarks = new ArrayList<FacilityMark>();
    	List<Facility> facilities = new FacilityDao(database).getFacilityByType(type);
    	
    	for(int i = 0; i < facilities.size(); i++){
    		int f_id = facilities.get(i).getId();
    		int b_id = new BuildingFacilityDao(database).getBuildingIdByFacilityId(f_id);
    		BuildingMark buildingMark = 
    		    new BuildingMarkDao(database).getBuildingMarkById(b_id);
    		Log.i("####","#########");
    		Log.i("building-->", buildingMark.getBuilding().getName());
    		FacilityMark facilityMark = new FacilityMark(facilities.get(i), buildingMark);
    		facilityMarks.add(facilityMark);
    	}
    	return facilityMarks;
    }
} 