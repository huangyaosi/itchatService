package com.enosh.itchatService.common;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import jakarta.mail.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageHander {
	@Autowired private List<MessageFilter> messageFilters;
	@Autowired private ExecutorService threadPool;
	
	public void process(MailMessage msg, CountDownLatch endGate) {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				MailMessageFilterChain filterChain = new MailMessageFilterChain(messageFilters);
				try {
					filterChain.doFilter(msg);
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					if(!msg.isRead()) msg.getContent();
					endGate.countDown();
				}
			}
			
		};
		threadPool.submit(task);
	}
	
	public void process(Message[] messages) {
		final CountDownLatch endGate = new CountDownLatch(messages.length);
		
		for (int i = 0; i < messages.length; i++) {
			Message msg = messages[i];
			process(new MailMessage(msg), endGate);
		}
		
		try {
			endGate.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
