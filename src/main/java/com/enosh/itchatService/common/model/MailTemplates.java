package com.enosh.itchatService.common.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:mail-templates.properties")
@ConfigurationProperties("mail.template")
public class MailTemplates {
	
	private final MailTemplate noteTypeCreateFailed = new MailTemplate(); 
	private final MailTemplate noteTypeCreateSucceed = new MailTemplate(); 

	public MailTemplate getNoteTypeCreateFailed() {
		return noteTypeCreateFailed;
	}

	public MailTemplate getNoteTypeCreateSucceed() {
		return noteTypeCreateSucceed;
	}
}
