package com.enosh.note.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.enosh.note.utils.Strings;

@Entity
@Table(name = "users")
public class User extends AbsEntity{

	private static final long serialVersionUID = 2669151702877244522L;
	
	private String username;
	
//	private String nicknames;
	
	private String password;
	
	private String primaryEmail;
	
	private String otherEmails;
	
	public User() {}
	
	public User(String userName) {
		this.username = userName;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
//	public String getNicknames() {
//		return nicknames;
//	}
//	
//	public void setNicknames(String nicknames) {
//		this.nicknames = nicknames;
//	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPrimaryEmail() {
		return primaryEmail;
	}
	
	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}
	
	public String getOtherEmails() {
		return otherEmails;
	}
	
	public void setOtherEmails(String otherEmails) {
		this.otherEmails = otherEmails;
	}
	
	public void addEmail(String email) {
		if(Strings.isEmpty(email)) return;
		
		if(Strings.isEmpty(primaryEmail)) {
			primaryEmail = email;
			return;
		} else if(primaryEmail.equals(email)) {
			return;
		}
		
		if(Strings.isEmpty(this.otherEmails)) {
			otherEmails = email;
		} else if(otherEmails.indexOf(email) == -1){
			otherEmails = otherEmails + "," + email;
		}
	}
//	
//	public void addNickname(String nickName) {
//		if(StringUtils.isEmpty(nickName) || this.username.equals(nickName)) return;
//		
//		if(StringUtils.isEmpty(this.nicknames)) {
//			this.nicknames = nickName;
//		} else if(nicknames.indexOf(nickName) == -1){
//			this.nicknames = this.nicknames + "," + nickName;
//		}
//	}
}
