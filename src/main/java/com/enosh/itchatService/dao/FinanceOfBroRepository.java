package com.enosh.itchatService.dao;

import org.springframework.stereotype.Repository;

import com.enosh.itchatService.model.FinanceOfBro;
@Repository
public interface FinanceOfBroRepository extends AbsRepository<FinanceOfBro>{
	
	public FinanceOfBro findTopByOrderByCreationDateDesc();
}
