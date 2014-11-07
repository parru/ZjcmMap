package com.cucmap.map.mark;

import java.io.Serializable;

import com.cucmap.data.position.Position;

/**
 * 建筑类型实体的元数据
 * 建筑类型实体包括大厦、部门、操场、风景等地图上的实体
 * 地图上的实体可以抽象出实体的描述、范围、中心点、最近的路口等基本信息
 * 地图上实体的范围是一系列的点集，使用List存储
 * 地图上实体的最近路口是一系列的点集，使用List存储
 * @author Administrator
 */
public class MetaBuildingItem implements Serializable{
	//
	private Position position;
    private String name;
    private String description;
    private int type;
    private String alias;
    
	public Position getPosition() {
		return position;
	}
	
	public void setPosition(Position position) {
		this.position = position;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
}
