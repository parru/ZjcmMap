package com.cucmap.data.facility;

import java.io.Serializable;

import com.cucmap.data.building.BuildingMark;

/*
 * 
 * @author Pingfu
 */
public class FacilityMark implements Serializable {
    private Facility facility;
    private BuildingMark buildingMark;
        
    public FacilityMark(Facility facility, BuildingMark buildingMark){
        this.facility = facility;
        this.buildingMark = buildingMark;
    }
	public Facility getFacility() {
		return facility;
	}
	public void setFacility(Facility facility) {
		this.facility = facility;
	}
	public BuildingMark getBuildingMark() {
		return buildingMark;
	}
	public void setBuildingMark(BuildingMark buildingMark) {
		this.buildingMark = buildingMark;
	}
}
