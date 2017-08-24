package com.enosh.itchatService.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("mail.sender")
public class MailSenderConfig {
	private String fromPdf;
	private String subjectPdf;
	public String getFromPdf() {
		return fromPdf;
	}
	public void setFromPdf(String fromPdf) {
		this.fromPdf = fromPdf;
	}
	public String getSubjectPdf() {
		return subjectPdf;
	}
	public void setSubjectPdf(String subjectPdf) {
		this.subjectPdf = subjectPdf;
	}
	
}
