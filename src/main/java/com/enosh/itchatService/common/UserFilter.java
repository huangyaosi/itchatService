package com.enosh.itchatService.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.enosh.itchatService.dispatcher.ThreadLocalUtils;
import com.enosh.itchatService.model.User;
import com.enosh.itchatService.service.UserService;

@Component
@Order(value=1)
public class UserFilter implements MessageFilter{
	@Autowired UserService userService;
	
	@Override
	public void doFilter(MailMessage msg, MessageFilterChain filterChain) {
		User user = userService.findByEmail(msg.getFromUserName());
		if(user != null) {
			ThreadLocalUtils.setCurrentUser(user);
			filterChain.doFilter(msg);
		}
	}

}
