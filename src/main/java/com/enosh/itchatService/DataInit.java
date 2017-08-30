package com.enosh.itchatService;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.enosh.itchatService.model.Note;
import com.enosh.itchatService.model.NoteType;
import com.enosh.itchatService.model.User;
import com.enosh.itchatService.service.NoteService;
import com.enosh.itchatService.service.NoteTypeService;
import com.enosh.itchatService.service.ShareNoteService;
import com.enosh.itchatService.service.UserService;

@Component("dataInit")
public class DataInit {
	
	@Autowired JdbcTemplate jdbcTemplate;
	@Autowired ShareNoteService shareNoteService;
	@Autowired NoteTypeService noteTypeService;
	@Autowired NoteService noteService;
	@Autowired UserService userService;
	
//	@PostConstruct
//	public void init() {
//		List<Map<String, Object>> rows = jdbcTemplate.queryForList("select * from public.share_note where "
//				+ " to_char(creation_date,'YYYYMMDD')>='20170822' and to_char(creation_date,'YYYYMMDD')<='20170824' and user_id='4aa2bc45-27ee-4cb9-b91d-4304c4840403'");
//		NoteType ntOne = null;
//		for(NoteType nt : noteTypeService.getDAO().findAll()) {
//			ntOne = nt;
//		}
//		User user = userService.getDAO().findByUsername("存伟");
//		for (Map<String, Object> map : rows) {
//			Note note = new Note();
//			note.setUser(user);
//			note.setNoteType(ntOne);
//			note.setText((String)map.get("text"));
//			Date date = (Date)map.get("creation_date");
//			note.setCreationDate(date);
//			note.setModificationDate(date);
//			noteService.save(note);
//		}
//	}
	
	
}
