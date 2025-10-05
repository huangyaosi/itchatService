package com.enosh.itchatService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class ShareNote extends AbsEntity{
	
	private static final long serialVersionUID = -3177988852853904490L;

	@Column(columnDefinition="Text")
	private String text;
	
	@ManyToOne
	private User user;
	
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}	
	
}
