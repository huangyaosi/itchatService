package com.enosh.note.model.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enosh.note.common.model.MailEntity;
import com.enosh.note.common.service.MailSenderService;
import com.enosh.note.config.bean.MailTemplates;
import com.enosh.note.dispatcher.KeyMethodMapping;
import com.enosh.note.model.NoteType;
import com.enosh.note.model.User;
import com.enosh.note.model.dao.NoteTypeRepository;
import com.enosh.note.utils.Strings;
import com.enosh.note.utils.ThreadLocalUtils;

@Service
@KeyMethodMapping
public class NoteTypeService extends AbsService<NoteType>{

	@Autowired NoteTypeRepository noteTypeRepository;
	@Autowired MailSenderService mailSenderService;
	@Autowired MailTemplates mailTemplates;
	
	@Override
	public NoteTypeRepository getDAO() {
		return noteTypeRepository;
	}

	@KeyMethodMapping("key.to.method.create-book-tag")
	public void createNoteType(String tag, String alias) {
		User user = ThreadLocalUtils.getCurrentUser();
		String description = ThreadLocalUtils.getMailContent();
		NoteType noteType = getDAO().findByTagOrAlias(user, tag, alias);
		
		Map<String, String> map = new HashMap<String, String>();
		MailEntity mailEntity = new MailEntity(user.getPrimaryEmail());
		mailEntity.setMap(map);
		
		if(noteType != null) {
			mailEntity.setTemplate(mailTemplates.getNoteTypeCreateFailed());
		} else {
			noteType = new NoteType();
			noteType.setUser(user);
			noteType.setTag(tag);
			noteType.setAlias(alias);
			noteType.setDescription(description);
			save(noteType);
			mailEntity.setTemplate(mailTemplates.getNoteTypeCreateSucceed());
		}
		
		List<NoteType> noteTypes = getDAO().findByUser(user);
		if(noteTypes != null && noteTypes.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (NoteType n : noteTypes) {
				sb.append(n.getTag()).append(Strings.COLON_DELIMITER).append(n.getAlias()).append("\r\n");
			}
			
			map.put("username", user.getUsername());
			map.put("noteTypes", sb.toString());
			map.put("tag", tag + "/" + alias);
			mailSenderService.sendEmail(mailEntity);
		}
		
	}
	
	@KeyMethodMapping("key.to.method.get-existed-note-type")
	public void getExisted() {
		User user = ThreadLocalUtils.getCurrentUser();
		
		Map<String, String> map = new HashMap<String, String>();
		MailEntity mailEntity = new MailEntity(user.getPrimaryEmail());
		mailEntity.setMap(map);
		
		mailEntity.setTemplate(mailTemplates.getNoteTypeList());
		List<NoteType> noteTypes = getDAO().findByUser(user);
		if(noteTypes != null && noteTypes.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (NoteType n : noteTypes) {
				sb.append(n.getTag()).append(Strings.COLON_DELIMITER).append(n.getAlias()).append("\r\n");
			}
			
			map.put("username", user.getUsername());
			map.put("noteTypes", sb.toString());
			mailSenderService.sendEmail(mailEntity);
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
	
	public List<NoteType> findByUser(User user) {
		return getDAO().findByUser(user);
	}
	
	public List<NoteType> findByUserAndEqTagOrAliasIn(User user, Collection<String> tags) {
		return getDAO().findByUserAndEqTagOrAliasIn(user, tags);
	}

}
