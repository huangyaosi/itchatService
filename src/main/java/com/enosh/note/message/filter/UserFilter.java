package com.enosh.note.message.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.enosh.note.common.model.MailMessage;
import com.enosh.note.model.User;
import com.enosh.note.model.service.UserService;
import com.enosh.note.utils.ThreadLocalUtils;

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
