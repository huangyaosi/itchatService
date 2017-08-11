package com.enosh.itchatService.model;

import javax.persistence.Entity;

@Entity
public class FinanceOfBro extends AbsEntity{
	private static final long serialVersionUID = 1622467375099255291L;
	private int remain;
	private int lastRemain;
	private String description;
	private String name;
	
	public int getRemain() {
		return remain;
	}
	public void setRemain(int remain) {
		this.remain = remain;
	}
	public int getLastRemain() {
		return lastRemain;
	}
	public void setLastRemain(int lastRemain) {
		this.lastRemain = lastRemain;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
