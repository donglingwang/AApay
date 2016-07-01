package com.bishe.aapay.dto;

import java.io.Serializable;

public class Category implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String categoryName;
	private int partentId;
	private int type;
	public Category() {
		
	}
	public Category(String categoryName) {
		this.categoryName = categoryName;
		this.partentId = 0;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public int getPartentId() {
		return partentId;
	}
	public void setPartentId(int partentId) {
		this.partentId = partentId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
