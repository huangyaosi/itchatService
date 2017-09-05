package com.enosh.itchatService.service;

import com.enosh.itchatService.dao.AbsRepository;

public abstract class AbsService<T> {
	public abstract AbsRepository<T> getDAO();
	
	public void save(T t) {
		getDAO().save(t);
	}
	
	public void delete(T t) {
		getDAO().delete(t);
	}
	
	public void get(String id) {
		getDAO().findOne(id);
	}
	
	public Iterable<T> findAll() {
		return getDAO().findAll();
	}
}
