package com.enosh.itchatService.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ShareNote extends AbsEntity{
	
	private static final long serialVersionUID = -3177988852853904490L;

	@Column(columnDefinition="Text")
	private String text;
	
	private String nickName;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}	
}
