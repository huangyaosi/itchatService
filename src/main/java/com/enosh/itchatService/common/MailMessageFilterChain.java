package com.enosh.itchatService.common;

import java.util.ArrayList;
import java.util.List;

public class MailMessageFilterChain implements MessageFilterChain {
	
	private List<MessageFilter> filters;
	int index;
	
	public MailMessageFilterChain(List<MessageFilter> filters){
		this.filters = filters;
	}
	
	@Override
	public void doFilter(MailMessage msg) {
		MessageFilter filter = getNext();
		if(filter != null) {
			filter.doFilter(msg, this);
		}
	}

	@Override
	public MailMessageFilterChain addFilter(MessageFilter filter) {
		if(filters == null) {
			filters = new ArrayList<MessageFilter>();
		}
		filters.add(filter);
		return this;
	}
	
	protected MessageFilter getNext() {
		if(index + 1 > filters.size()) {
			return null;
		}
		MessageFilter filter = filters.get(index);
		index++;
		return filter;
	}
}
