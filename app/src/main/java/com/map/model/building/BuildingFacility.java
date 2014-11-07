package com.map.model.building;

import java.io.Serializable;

/*
 * 
 */
public class BuildingFacility implements Serializable {
    private int building_id;
    private int facility_id;
    
    public BuildingFacility(){}
    public BuildingFacility(int building_id, int facility_id){
    	this.building_id = building_id;
    	this.facility_id = facility_id;
    }
    
	public int getBuilding_id() {
		return building_id;
	}
	public void setBuilding_id(int building_id) {
		this.building_id = building_id;
	}
	public int getFacility_id() {
		return facility_id;
	}
	public void setFacility_id(int facility_id) {
		this.facility_id = facility_id;
	}
    
    
}
