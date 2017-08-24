package com.enosh.itchatService.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enosh.itchatService.App;

import freemarker.template.TemplateExceptionHandler;

@Configuration
public class BeanFactory {
	
	@Bean("freemarkerConfig")
	public freemarker.template.Configuration getFreeMakerConfiguration() {
		freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_26);
		configuration.setClassForTemplateLoading(App.class, "/templates");
		configuration.setDefaultEncoding("UTF-8");
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
		return configuration;
	}

}
