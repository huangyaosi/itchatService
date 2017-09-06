package com.enosh.itchatService.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enosh.itchatService.dao.NoteRepository;
import com.enosh.itchatService.dispatcher.ThreadLocalUtils;
import com.enosh.itchatService.model.Note;
import com.enosh.itchatService.model.NoteType;
import com.enosh.itchatService.model.User;
import com.enosh.itchatService.utils.DateTimeUtils;
import com.enosh.itchatService.utils.StringUtils;

@Service
public class NoteService extends AbsService<Note>{

	@Autowired NoteRepository noteRepository;
	@Autowired NoteTypeService noteTypeService;
	@Autowired UserService userService;
	
	@Override
	public NoteRepository getDAO() {
		return noteRepository;
	}

	public boolean createNote(String key) {
		boolean succeed = false;
		User user = ThreadLocalUtils.getCurrentUser();
		NoteType noteType = noteTypeService.findByUserAndEqTagOrAlias(user, key);
		String text = ThreadLocalUtils.getMailContent();
		if( noteType != null) {
			createNote(user, noteType, text);
			succeed = true;
		} 
		return succeed;
	}
	
	public void createNote(User user, NoteType noteType, String text) {
		if(StringUtils.isEmpty(text)) return;
		List<Note> notes = getDAO().findByCreationDate(user, DateTimeUtils.toStr(new Date(), DateTimeUtils.DATE_MASK));
		if(notes != null) {
			for (Note note : notes) {
				double percent = StringUtils.similarity(text, note.getText());
				System.out.println(percent);
				if(percent >= 0.9d){
					note.setText(text);
					save(note);
					return;
				}
			}
		}
		
		Note note = new Note();
		note.setUser(user);
		note.setCreationDate(new Date());
		note.setModificationDate(new Date());
		note.setNoteType(noteType);
		note.setText(text);
		save(note);
	}
	
	public List<Note> findByUserAndNoteType(User user, NoteType noteType) {
		return getDAO().findByUserAndNoteTypeOrderByCreationDate(user, noteType);
	}
}
