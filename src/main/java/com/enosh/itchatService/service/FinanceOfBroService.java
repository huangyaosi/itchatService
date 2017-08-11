package com.enosh.itchatService.service;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enosh.itchatService.dao.FinanceOfBroRepository;
import com.enosh.itchatService.model.FinanceOfBro;
@Service
public class FinanceOfBroService extends AbsService<FinanceOfBro>{

	@Autowired private FinanceOfBroRepository financeOfBroRepository;
	
	@Override
	public FinanceOfBroRepository getDAO() {
		return financeOfBroRepository;
	}

	public void createNew(int remain, int lastRemain, String name, String description) {
		FinanceOfBro financeOfBro = new FinanceOfBro();
		financeOfBro.setName(name);
		financeOfBro.setRemain(remain);
		financeOfBro.setLastRemain(lastRemain);
		financeOfBro.setDescription(description);
		financeOfBro.setCreationDate(new Date());
		financeOfBro.setModificationDate(new Date());
		save(financeOfBro);
	}
	
	public FinanceOfBro findLastestOne() {
		return getDAO().findTopByOrderByCreationDateDesc();
	}
	
	public String getLatestFinanceJson() {
		FinanceOfBro f = findLastestOne();
		JSONObject jo = new JSONObject();
		try {
			jo.put("remain", f.getRemain());
			jo.put("lastRemain", f.getRemain());
			jo.put("description", f.getDescription());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo.toString();
		
	}
}
