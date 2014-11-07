package com.cucmap.map.mark;

import java.util.ArrayList;
import java.util.List;

import com.cucmap.data.building.BuildingMark;
import com.cucmap.data.facility.FacilityMark;

public class ItemMarkDao {
    
	public static final int BUILDING_DATA_TYPE = 0;
	public static final int FACILITY_DATA_TYPE = 1;
	public static final int SCENCE_DATA_TYPE = 2;
	
	/**
	 * 根据建筑列表得到建筑在地图上的标志
	 * @param buildingMarks  buildingMark的列表
	 * @return marks         该列表对应的地图图标
	 */
	public static List<ItemMark> getMarksByBuildingMarks(List<BuildingMark> buildingMarks){
		List<ItemMark> marks = new ArrayList<ItemMark>();
		for(int i = 0; i < buildingMarks.size(); i++){
			BuildingMark buildingMark = buildingMarks.get(i);
			String name = buildingMark.getBuilding().getName();
			String description = buildingMark.getBuilding().getDescription();
			ItemMark mark = new ItemMark(buildingMark, name, description, 0);
			marks.add(mark);
		}
		return marks;
	}
	
	/**
	 * 
	 */
	public static List<ItemMark> getMarksByFacilityMarks(List<FacilityMark> facilityMarks){
		List<ItemMark> marks = new ArrayList<ItemMark>();
		for(int i = 0; i < facilityMarks.size(); i++){
			FacilityMark facilityMark = facilityMarks.get(i);
			BuildingMark buildingMark = facilityMark.getBuildingMark();
			String name = facilityMark.getFacility().getName();
			String description = "";
			int type = facilityMark.getFacility().getType();
			ItemMark mark = new ItemMark(buildingMark, name, description, type);
			marks.add(mark);
		}
		return marks;
	}
}
