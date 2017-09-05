package com.enosh.itchatService.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.enosh.itchatService.config.MailSenderConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MailSenderTest {
	@Autowired private MailSenderService mailSenderService;
	@Autowired private JavaMailSender mailSender;
	@Autowired MailSenderConfig mailSenderConfig;
	
	@Test
	public void sendEmailWithAttachment() throws MessagingException {
		String to = "hcunwei@rs.com";
		String subject = "testing";
		String content = "hello";
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom(mailSenderConfig.getFromPdf());
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(content);
//		mailSender.send(message);
	}
}
