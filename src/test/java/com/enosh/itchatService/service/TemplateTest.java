package com.enosh.itchatService.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TemplateTest {
	@Autowired FreeMakerTemplateService freeMakerTemplateService;
	@Autowired private MailSenderService mailSenderService;
	
	@Test
	public void testTemplateFormat(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", "mother");
		map.put("month", "201708");
		String result = freeMakerTemplateService.formatTemplate("pdfAttachment.ftl", map);
		Assertions.assertThat(result).contains("mother");
	}
}
