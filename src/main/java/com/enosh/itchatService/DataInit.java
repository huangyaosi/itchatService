package com.enosh.itchatService;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.enosh.itchatService.model.ShareNote;
import com.enosh.itchatService.service.ShareNoteService;

@Component("dataInit")
public class DataInit {
	
	@Autowired JdbcTemplate jdbcTemplate;
	@Autowired ShareNoteService shareNoteService;
	
	@PostConstruct
	public void init() {
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("select * from public.note");
		for (Map<String, Object> map : rows) {
			ShareNote note = new ShareNote();
			note.setNickName((String)map.get("user_name"));
			note.setText((String)map.get("note"));
			Date date = (Date)map.get("creation_time");
			note.setCreationDate(date);
			note.setModificationDate(date);
			shareNoteService.save(note);
		}
	}
}
