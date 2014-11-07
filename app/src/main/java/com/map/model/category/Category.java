package com.map.model.category;

public class Category {
    private String name;
    private int type;
    private String mark;
    
    public Category(){}
    public Category(String name,int type,String mark){
    	this.name = name;
    	this.type = type;
    	this.mark = mark;
    }
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
    
}
