package com.enosh.itchatService.service;

import java.io.File;

public interface MailSenderService {
	public void sendEmailWithAttachment(String to, String subject, String content, File file);
}
