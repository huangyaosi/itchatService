package com.enosh.note.message.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.enosh.note.common.model.MailMessage;
import com.enosh.note.utils.ThreadLocalUtils;

@Component
@Order(value=2)
public class LocalVariableInitFilter implements MessageFilter{

	@Override
	public void doFilter(MailMessage msg, MessageFilterChain filterChain) {
		ThreadLocalUtils.setMessage(msg);
		filterChain.doFilter(msg);
	}

}
