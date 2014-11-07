package com.cucmap.data.road;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.cucmap.data.position.Position;

/*
 * 道路的抽象数据类型，与数据库的road表结构关系不大
 * @author Pingfu 
 */
public class RoadMark implements Serializable {
	private Road road;
	private Position beginPosition;
    private Position endPosition;
    
    public RoadMark(Road road, Position beginPosition,Position endPosition){
    	this.road = road;
    	this.beginPosition = beginPosition;
    	this.endPosition = endPosition;
    }
    
	public Road getRoad() {
		return road;
	}
	
	public void setRoad(Road road) {
		this.road = road;
	}
	
	public Position getBeginPosition() {
		return beginPosition;
	}

	public void setBeginPosition(Position beginPosition) {
		this.beginPosition = beginPosition;
	}

	public Position getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(Position endPosition) {
		this.endPosition = endPosition;
	}	
}
