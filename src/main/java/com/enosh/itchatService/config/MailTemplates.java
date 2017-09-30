package com.enosh.itchatService.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.enosh.itchatService.common.MailTemplate;

@Configuration
@PropertySource("classpath:mail-templates.properties")
@ConfigurationProperties("mail.template")
public class MailTemplates {
	
	private final MailTemplate noteTypeCreateFailed = new MailTemplate(); 
	private final MailTemplate noteTypeCreateSucceed = new MailTemplate();
	private final MailTemplate shareNoteWeeklyGeneration = new MailTemplate();
	private final MailTemplate noteGeneration = new MailTemplate();
	
	public MailTemplate getNoteTypeCreateFailed() {
		return noteTypeCreateFailed;
	}

	public MailTemplate getNoteTypeCreateSucceed() {
		return noteTypeCreateSucceed;
	}

	public MailTemplate getShareNoteWeeklyGeneration() {
		return shareNoteWeeklyGeneration;
	}

	public MailTemplate getNoteGeneration() {
		return noteGeneration;
	}
	
}
