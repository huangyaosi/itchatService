package com.enosh.note.config.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("mail.receiver")
public class MailReceiverConfig {
	private String protocol;
	private String host;
	private String port;
	private String username;
	private String password;
	private String spiritualShareKeyword;
	private String adminEmail;
	private String createTagKeyword;
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSpiritualShareKeyword() {
		return spiritualShareKeyword;
	}
	public void setSpiritualShareKeyword(String spiritualShareKeyword) {
		this.spiritualShareKeyword = spiritualShareKeyword;
	}
	public String getAdminEmail() {
		return adminEmail;
	}
	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}
	public String getCreateTagKeyword() {
		return createTagKeyword;
	}
	public void setCreateTagKeyword(String createTagKeyword) {
		this.createTagKeyword = createTagKeyword;
	}
	
}
