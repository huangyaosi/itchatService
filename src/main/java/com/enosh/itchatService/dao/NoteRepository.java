package com.enosh.itchatService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.enosh.itchatService.model.Note;
import com.enosh.itchatService.model.NoteType;
import com.enosh.itchatService.model.User;

@Repository
public interface NoteRepository extends AbsRepository<Note>{
	
	public List<Note> findByNoteType(NoteType noteType);
	
	public List<Note> findByUser(User user);
	
	@Query("select n from Note n where n.user =?1 and to_char(n.creationDate, 'YYYYMMDD')=?2")
	public List<Note> findByCreationDate(User user, String creationDate);
	
}
