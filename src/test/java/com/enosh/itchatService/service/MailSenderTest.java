package com.enosh.itchatService.service;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.enosh.itchatService.service.MailSenderService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MailSenderTest {
	@Autowired private MailSenderService mailSenderService;
	
	@Test
	public void sendEmailWithAttachment() {
		String to = "hcunwei@rs.com";
		String subject = "testing";
		String content = "hello";
		
		mailSenderService.sendNormalEmail(to, subject, content);
	}
}
