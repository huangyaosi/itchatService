package com.enosh.note.model.dao;

import org.springframework.data.jpa.repository.Query;

import com.enosh.note.model.User;

public interface UserRepository extends AbsRepository<User>{
	
	public User findByUsername(String username);
	
	@Query("select u from User u where u.primaryEmail=?1 or u.otherEmails like ?1")
	public User findByEmail(String email);
}
