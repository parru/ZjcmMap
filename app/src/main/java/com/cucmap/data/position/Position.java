package com.cucmap.data.position;

import java.io.Serializable;

/*
 * 
 */
public class Position implements Serializable {
	private int id;
    private double longitude;
    private double latitude;
    private String name;
    
    public Position(){}
    public Position(int id, double longitude,double latitude,String name){
    	this.id = id;
    	this.longitude = longitude;
    	this.latitude = latitude;
    	this.name = name;
    }
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
}
