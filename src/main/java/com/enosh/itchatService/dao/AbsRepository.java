package com.enosh.itchatService.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbsRepository<T> extends CrudRepository<T, String>{

}
