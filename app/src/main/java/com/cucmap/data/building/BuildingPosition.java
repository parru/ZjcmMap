package com.cucmap.data.building;

import java.io.Serializable;

/*
 * 
 */
public class BuildingPosition  implements Serializable{
    private int building_id;
    private int point_id;
    private int type;
    
    public BuildingPosition(){}
    
    public BuildingPosition(int building_id, int point_id, int type){
    	this.building_id = building_id;
    	this.point_id = point_id;
    	this.type = type;
    }

	public int getBuilding_id() {
		return building_id;
	}

	public void setBuilding_id(int building_id) {
		this.building_id = building_id;
	}

	public int getPoint_id() {
		return point_id;
	}

	public void setPoint_id(int point_id) {
		this.point_id = point_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}    
}
