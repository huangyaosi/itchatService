package com.enosh.itchatService.common;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.enosh.itchatService.dispatcher.ThreadLocalUtils;

@Component
@Order(value=2)
public class LocalVariableInitFilter implements MessageFilter{

	@Override
	public void doFilter(MailMessage msg, MessageFilterChain filterChain) {
		ThreadLocalUtils.setMessage(msg);
		filterChain.doFilter(msg);
	}

}
