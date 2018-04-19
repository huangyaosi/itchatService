package com.enosh.note.config.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("pdf")
public class PdfConfig {
	private String shareNoteTitle;
	private String noteTitle;
	private String directoryPath;
	private String frontPath;
	private String footer;
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
	public String getNoteTitle() {
		return noteTitle;
	}
	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}
	public String getFooter() {
		return footer;
	}
	public void setFooter(String footer) {
		this.footer = footer;
	}
}
