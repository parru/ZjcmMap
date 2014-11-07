package com.cucmap.data.building;

import java.io.Serializable;

/*
 * 
 */
public class Building implements Serializable {
	/**  **/
	private static final long serialVersionUID = 1L;
	private int id;
    private String name;
    private int number;
    private String alias;
    private String description;
    
    public Building(){}
    
    public Building(int id, String name, int number, String alias, String description){
    	this.id = id;
    	this.name = name;
    	this.number = number;
    	this.alias = alias;
    	this.description = description;
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
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
