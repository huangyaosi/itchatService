package com.enosh.itchatService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.enosh.itchatService.dao.UserRepository;
import com.enosh.itchatService.model.User;

@Service
public class UserService extends AbsService<User>{

	@Autowired UserRepository userRepository;
	
	@Override
	public UserRepository getDAO() {
		return userRepository;
	}

	public User findByEmailOrName(String email, String name) {
		User user = getDAO().findByEmail(email, email);
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
	
	public String createUser(String userName, String email) {
		User user = getDAO().findByUsername(userName);
		if(user == null) {
			user = new User(userName);
			user.addEmail(email);
			save(user);
		}
		return "Create user successfully for " + userName;
	}
}
