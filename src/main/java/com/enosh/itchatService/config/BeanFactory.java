package com.enosh.itchatService.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enosh.itchatService.App;
import com.enosh.itchatService.common.MessageFilter;
import com.enosh.itchatService.common.concurrency.ActiveQueue;
import com.enosh.itchatService.service.EmailReceiverService;

import freemarker.template.TemplateExceptionHandler;

@Configuration
public class BeanFactory {
	@Autowired private ApplicationContext context;
	
	@Bean("freemarkerConfig")
	public freemarker.template.Configuration getFreeMakerConfiguration() {
		freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_26);
		configuration.setClassForTemplateLoading(App.class, "/templates");
		configuration.setDefaultEncoding("UTF-8");
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
		return configuration;
	}

	@Bean("threadPool")
	public ExecutorService getThreadPool() {
		int corePoolSize = Runtime.getRuntime().availableProcessors();
		BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(200);
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(corePoolSize+1, corePoolSize+2, 3000, TimeUnit.MILLISECONDS, workQueue);
		threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		return threadPool;
	}
	
	@Bean("messageQueue")
	public ActiveQueue getMessageQueue() {
		ActiveQueue queue = new ActiveQueue(EmailReceiverService.createMessageProcesser(), 1);
		queue.setExecutor(getThreadPool());
		return queue;
	}
	
}
