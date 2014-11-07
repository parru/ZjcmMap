package com.map.model.road;

public class RoadPosition {
    private int road_id;
    private int pos_id;
    private int start;
    
    public RoadPosition(){}
    public RoadPosition(int road_id, int pos_id, int start){
    	this.road_id = road_id;
    	this.pos_id = pos_id;
    	this.start = start;
    }
	public int getRoad_id() {
		return road_id;
	}
	
	public void setRoad_id(int road_id) {
		this.road_id = road_id;
	}
	
	public int getPos_id() {
		return pos_id;
	}
	
	public void setPos_id(int pos_id) {
		this.pos_id = pos_id;
	}
	
	public int getStart(){
		return this.start;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
	
    
    
}
