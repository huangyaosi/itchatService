package com.enosh.itchatService.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

@Entity
public class NoteType extends AbsEntity{
	
	@ManyToOne
	private User user;
	
	private String tag;
	
	private String alias;
	
	private String description;
	
	@Type(type = "true_false")
	private boolean completed;
	
	@Type(type = "true_false")
	private boolean genereated;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
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

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isGenereated() {
		return genereated;
	}

	public void setGenereated(boolean genereated) {
		this.genereated = genereated;
	}
}
