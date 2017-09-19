package com.enosh.itchatService.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enosh.itchatService.App;
import com.enosh.itchatService.itext.pdf.ItextFonts;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

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

//	@Bean("itextFonts")
//	public ItextFonts getItextFonts() {
//		ItextFonts itextFonts = null;
//		try {
//			BaseFont bf = BaseFont.createFont(System.getProperty("itext.font.path"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return itextFonts;
//	}

}
