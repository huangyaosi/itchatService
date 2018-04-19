package com.enosh.note.model.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enosh.note.model.Note;
import com.enosh.note.model.NoteType;
import com.enosh.note.model.User;
import com.enosh.note.model.dao.NoteRepository;
import com.enosh.note.utils.DateTimeUtils;
import com.enosh.note.utils.Strings;
import com.enosh.note.utils.ThreadLocalUtils;

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
		if(Strings.isEmpty(text)) return;
		List<Note> notes = getDAO().findByCreationDate(user, DateTimeUtils.toStr(new Date(), DateTimeUtils.DATE_MASK));
		if(notes != null) {
			for (Note note : notes) {
				double percent = Strings.similarity(text, note.getText());
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
