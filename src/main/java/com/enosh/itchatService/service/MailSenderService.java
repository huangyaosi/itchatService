package com.enosh.itchatService.service;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.enosh.itchatService.config.MailSenderConfig;

@Service
public class MailSenderService{

	@Autowired
    private JavaMailSender mailSender;

	@Autowired MailSenderConfig mailSenderConfig;
    
	public void sendEmailWithAttachment(String to, String subject, String content, File file) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        helper.setFrom(mailSenderConfig.getFromPdf());
	        helper.setTo(to);
	        helper.setSubject(subject);
	        helper.setText(content);

	        FileSystemResource fileRes = new FileSystemResource(file);
	        String fileName = file.getName();
	        helper.addAttachment(fileName, fileRes);
	        mailSender.send(message);
	    } catch (MessagingException e) {
	    	
	    }
	}
	
	@Scheduled(cron = "*/40 * * * * *")
	public void mailTest() {
		System.out.println("start test ...");
		String to = "hcunwei@rs.com";
		String subject = "testing";
		String content = "hello";
		sendNormalEmail(to, subject, content);
		System.out.println("end test ...");
	}
	
	public void sendNormalEmail(String to, String subject, String content) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        helper.setFrom(mailSenderConfig.getFromPdf());
	        helper.setTo(to);
	        helper.setSubject(subject);
	        helper.setText(content);

	        mailSender.send(message);
	    } catch (MessagingException e) {
	    	e.printStackTrace();
	    }
	}

}
