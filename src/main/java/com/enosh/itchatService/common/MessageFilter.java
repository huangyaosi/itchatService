package com.enosh.itchatService.common;

public interface MessageFilter {
	public void doFilter(MailMessage msg, MessageFilterChain filterChain);
}
