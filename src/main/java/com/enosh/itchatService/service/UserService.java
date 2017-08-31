package com.enosh.itchatService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.enosh.itchatService.config.MailReceiverConfig;
import com.enosh.itchatService.dao.UserRepository;
import com.enosh.itchatService.dispatcher.KeyMethodMapping;
import com.enosh.itchatService.dispatcher.ThreadLocalUtils;
import com.enosh.itchatService.model.User;

@Service
@KeyMethodMapping
public class UserService extends AbsService<User>{

	@Autowired UserRepository userRepository;
	@Autowired MailReceiverConfig mailReceiverConfig;
	
	@Override
	public UserRepository getDAO() {
		return userRepository;
	}

	public User findByUsername(String username) {
		return getDAO().findByUsername(username);
	}
	
	public User findByEmailOrName(String email, String name) {
		User user = findByEmail(email);
		if(user == null && !StringUtils.isEmpty(name)) {
			user = getDAO().findByUsername(name);
			if(user != null) {
				user.addEmail(email);
				save(user);
			}
		}
		
		return user;
	}
	
	public User findOrCreateUserByName(String name) {
		User user = getDAO().findByUsername(name);
		if(user == null) {
			user = new User(name);
			save(user);
		}
		return user;
	}
	
	public User findByEmail(String email) {
		return getDAO().findByEmail(email);
	}
	
	//only super admin can create new account.
	@KeyMethodMapping("key.to.method.create-user")
	public String createUser(String userName, String email) {
		
		User user = getDAO().findByUsername(userName);
		
		if(mailReceiverConfig.getAdminEmail().equals(ThreadLocalUtils.getCurrentUser().getPrimaryEmail())) {
			user = user == null ? new User(userName) : user;
			user.setPrimaryEmail(email);
		} else {
			if(user != null) {
				user.addEmail(email);
			}
		}
		save(user);
		
		return "Create user successfully for " + userName;
	}
	
	
}
