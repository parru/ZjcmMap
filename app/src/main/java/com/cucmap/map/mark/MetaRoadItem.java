package com.cucmap.map.mark;

import java.io.Serializable;

import com.cucmap.data.position.Position;

/**
 * 道路上点的元数据，用来定义道路的基本信息
 * 道路上的实体可以抽象成一系列坐标点
 * 每一个坐标点由坐标点的位置、编号、描述以及一些建议信息
 * @author Administrator
 */
public class MetaRoadItem implements Serializable {
	
	/** **/
	private static final long serialVersionUID = 1L;
	private Position position;
	private int item_num;
	private String name;
	private String suggestion;

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public int getItem_num() {
		return item_num;
	}

	public void setItem_num(int item_num) {
		this.item_num = item_num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
}