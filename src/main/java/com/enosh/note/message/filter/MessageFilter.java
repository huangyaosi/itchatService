package com.enosh.note.message.filter;

import com.enosh.note.common.model.MailMessage;

public interface MessageFilter {
	public void doFilter(MailMessage msg, MessageFilterChain filterChain);
}
