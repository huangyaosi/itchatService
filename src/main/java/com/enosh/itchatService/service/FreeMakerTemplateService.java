package com.enosh.itchatService.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class FreeMakerTemplateService {
	
	@Autowired Configuration freemarkerConfig;
	
	public String formatTemplate(String templateStr, Map<String, ? extends Object> map) {
		String result = "";
		try {
			Template template = freemarkerConfig.getTemplate(templateStr);
			result = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return result;
	}

}
