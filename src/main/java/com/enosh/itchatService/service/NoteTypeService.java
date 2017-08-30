package com.enosh.itchatService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enosh.itchatService.dao.NoteTypeRepository;
import com.enosh.itchatService.model.NoteType;
import com.enosh.itchatService.model.User;

@Service
public class NoteTypeService extends AbsService<NoteType>{

	@Autowired NoteTypeRepository noteTypeRepository;
	
	@Override
	public NoteTypeRepository getDAO() {
		return noteTypeRepository;
	}

	public void createNoteType(User user, String tag, String alias, String description) {
		NoteType noteType = getDAO().findByTagOrAlias(user, tag, alias);
		if(noteType != null) {
			//TODO:发送相关的邮件
		} else {
			noteType = new NoteType();
			noteType.setUser(user);
			noteType.setTag(tag);
			noteType.setAlias(alias);
			save(noteType);
		}
	}
	public NoteType findByTagOrAlias(User user, String tag, String alias) {
		return getDAO().findByTagOrAlias(user, tag, alias);
	}
	
	public NoteType findByUserAndEqTagOrAlias(User user, String tagOrAlias) {
		return getDAO().findByUserAndEqTagOrAlias(user, tagOrAlias);
	}
}
