package com.enosh.itchatService.common;

public interface MessageFilterChain {
	public void doFilter(MailMessage msg);
	public MessageFilterChain addFilter(MessageFilter filter);
}
