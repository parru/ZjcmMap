package com.map.core.map.mark;

import java.io.Serializable;

import com.map.model.building.BuildingMark;

public class ItemMark implements Serializable {
    /**  **/
    private static final long serialVersionUID = 1L;
    private BuildingMark buildingMark;
    private String name;
    private String description;
    private int type;
    private boolean isChosen = false;  //给Item是否被选择,如果被选择，则在地图上显示地图信息
    
    public ItemMark(){};
    public ItemMark(BuildingMark buildingMark, String name, String description, int type){
    	this.buildingMark = buildingMark;
    	this.name = name;
    	this.description = description;
    	this.type = type;
    }
	
	public BuildingMark getBuildingMark() {
		return buildingMark;
	}

	public int getType() {
		return type;
	}

	public void setBuildingMark(BuildingMark buildingMark) {
		this.buildingMark = buildingMark;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void chooseThisItem(){
	    this.isChosen = true;
	}
	
	public boolean isChosen(){
	    return this.isChosen;
	}
}
