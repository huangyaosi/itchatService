package com.enosh.note.common.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.enosh.note.config.bean.MailTemplate;

public class MailEntity {

	private String to;
	private String subject;
	private MailTemplate template;
	private String content;
	private List<File> attachments;
	private Map<String, String> map;
	public MailEntity(String to, String subject, String content) {
		this.to = to;
		this.subject = subject;
		this.content = content;
	}
	public MailEntity(String to, MailTemplate template) {
		this.to = to;
		this.template = template;
	}
	public MailEntity(String to) {
		this(to, null);
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public MailTemplate getTemplate() {
		return template;
	}
	public void setTemplate(MailTemplate template) {
		this.template = template;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<File> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<File> attachments) {
		this.attachments = attachments;
	}
	public void addAttachments(File file) {
		if(attachments == null) {
			attachments = new ArrayList<File>();
		}
		attachments.add(file);
	}
	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	
}
