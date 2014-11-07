package com.map.model.road;

public class Road {
	private int id;
    private String name;
    private double weight;
    private int type;
    private String code;
    
    public Road(){}
    public Road(int id, String name, double weight, int type, String code){
    	this.id = id;
    	this.name = name;
    	this.weight = weight;
    	this.type = type;
    	this.code = code;
    }
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
