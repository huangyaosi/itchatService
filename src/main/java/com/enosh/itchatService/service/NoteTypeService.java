package com.enosh.itchatService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enosh.itchatService.dao.NoteTypeRepository;
import com.enosh.itchatService.dispatcher.KeyMethodMapping;
import com.enosh.itchatService.dispatcher.ThreadLocalUtils;
import com.enosh.itchatService.model.NoteType;
import com.enosh.itchatService.model.User;

@Service
@KeyMethodMapping
public class NoteTypeService extends AbsService<NoteType>{

	@Autowired NoteTypeRepository noteTypeRepository;
	
	@Override
	public NoteTypeRepository getDAO() {
		return noteTypeRepository;
	}

	@KeyMethodMapping("key.to.method.create-book-tag")
	public void createNoteType(String tag, String alias) {
		User user = ThreadLocalUtils.getCurrentUser();
		String description = ThreadLocalUtils.getMailContent();
		NoteType noteType = getDAO().findByTagOrAlias(user, tag, alias);
		if(noteType != null) {
			//TODO:发送相关的邮件
		} else {
			noteType = new NoteType();
			noteType.setUser(user);
			noteType.setTag(tag);
			noteType.setAlias(alias);
			noteType.setDescription(description);
			save(noteType);
		}
	}
	public NoteType findByTagOrAlias(User user, String tag, String alias) {
		return getDAO().findByTagOrAlias(user, tag, alias);
	}
	
	public NoteType findByUserAndEqTagOrAlias(User user, String tagOrAlias) {
		return getDAO().findByUserAndEqTagOrAlias(user, tagOrAlias);
	}
	
	public List<NoteType> findByUserAndCompletedAndGenereated(User user, boolean completed, boolean genereated) {
		return getDAO().findByUserAndCompletedAndGenereated(user, completed, genereated);
	}

}
