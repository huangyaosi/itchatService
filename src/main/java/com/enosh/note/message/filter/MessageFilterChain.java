package com.enosh.note.message.filter;

import com.enosh.note.common.model.MailMessage;

public interface MessageFilterChain {
	public void doFilter(MailMessage msg);
	public MessageFilterChain addFilter(MessageFilter filter);
}
