package com.enosh.note.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.enosh.note.common.model.MailEntity;
import com.enosh.note.common.service.MailSenderService;
import com.enosh.note.config.bean.MailReceiverConfig;
import com.enosh.note.dispatcher.KeyMethodMapping;
import com.enosh.note.model.User;
import com.enosh.note.model.dao.UserRepository;
import com.enosh.note.utils.ThreadLocalUtils;

@Service
@KeyMethodMapping
public class UserService extends AbsService<User>{

	@Autowired UserRepository userRepository;
	@Autowired MailReceiverConfig mailReceiverConfig;
	@Autowired MailSenderService mailSenderService;
	
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
		mailSenderService.sendEmail(new MailEntity(user.getPrimaryEmail(),"Creat user succeed!", "Create user " + user.getUsername() + " succeed, welcome to use it. "));
		return "Create user successfully for " + userName;
	}
	
	@KeyMethodMapping("key.to.method.update-email")
	public void updateEmail(String email) {
		User user = ThreadLocalUtils.getCurrentUser();
		String oldEmail = user.getPrimaryEmail();
		user.setPrimaryEmail(email);
		user.addEmail(oldEmail);
		save(user);
		mailSenderService.sendEmail(new MailEntity(oldEmail,"Update new email succeed", "Update new email succeed, now you can use you new email " + email));
	}
}
