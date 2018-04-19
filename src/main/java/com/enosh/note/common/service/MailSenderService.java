package com.enosh.note.common.service;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.enosh.note.common.model.MailEntity;
import com.enosh.note.config.bean.MailSenderConfig;
import com.enosh.note.config.bean.MailTemplate;
import com.enosh.note.utils.Strings;

@Service
public class MailSenderService{

	@Autowired private JavaMailSender mailSender;
	@Autowired private MailSenderConfig mailSenderConfig;
	@Autowired private FreeMakerTemplateService freeMakerTemplateService;
    
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
	
	public void sendEmail(MailEntity mailEntity) {
		String content = "";
		String to = "";
		String subject = "";
		
		if(Strings.isEmpty(mailEntity.getTo())) {
			//TODO:
		}
		
		MailTemplate template = mailEntity.getTemplate();
		if(template != null && !Strings.isEmpty(template.getTemplate())) {
			content = freeMakerTemplateService.formatTemplate(template.getTemplate(), mailEntity.getMap());
			subject = !Strings.isEmpty(template.getSubject()) ? template.getSubject() : mailEntity.getSubject();
		} else {
			content = mailEntity.getContent();
			subject = mailEntity.getSubject();
		}
		
		MimeMessage message = mailSender.createMimeMessage();
		try {
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        helper.setFrom(mailSenderConfig.getFromPdf());
	        helper.setTo(mailEntity.getTo());
	        helper.setSubject(subject);
	        helper.setText(content);

	        if(mailEntity.getAttachments() != null && mailEntity.getAttachments().size() > 0) {
	        	for (File attachment : mailEntity.getAttachments()) {
	        		helper.addAttachment(attachment.getName(), attachment);
				}
	        }
	        System.out.println("send email to " + mailEntity.getTo() + "......");
	        mailSender.send(message);
	    } catch (MessagingException e) {
	    	e.printStackTrace();
	    }
		
	}
}
