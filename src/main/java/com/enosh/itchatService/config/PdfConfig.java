package com.enosh.itchatService.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("pdf")
public class PdfConfig {
	private String shareNoteTitle;
	private String directoryPath;
	private String frontPath;
	public String getShareNoteTitle() {
		return shareNoteTitle;
	}
	public void setShareNoteTitle(String shareNoteTitle) {
		this.shareNoteTitle = shareNoteTitle;
	}
	public String getDirectoryPath() {
		return directoryPath;
	}
	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}
	public String getFrontPath() {
		return frontPath;
	}
	public void setFrontPath(String frontPath) {
		this.frontPath = frontPath;
	}
	
}
