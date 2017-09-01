package com.enosh.itchatService.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.MapKeyTemporal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enosh.itchatService.dao.ShareNoteRepository;
import com.enosh.itchatService.dispatcher.KeyMethodMapping;
import com.enosh.itchatService.dispatcher.ThreadLocalUtils;
import com.enosh.itchatService.model.ShareNote;
import com.enosh.itchatService.model.User;
import com.enosh.itchatService.utils.DateTimeUtils;
import com.enosh.itchatService.utils.StringUtils;
import com.itextpdf.text.DocumentException;

import freemarker.template.utility.StringUtil;

@Service
@KeyMethodMapping
public class ShareNoteService extends AbsService<ShareNote>{

	@Autowired private ShareNoteRepository shareNoteRepository;
	@Autowired private UserService userService;
	
	public List<ShareNote> findByNickNameAndMonth(String nickName, String fromMonth, String toMonth) {
		return shareNoteRepository.findByNickNameAndMonth(nickName, fromMonth, toMonth);
	}

	public List<ShareNote> findByNickNameAndDate(String nickName, String date) {
		return shareNoteRepository.findByNickNameAndDate(nickName, date);
	}
	
	@Override
	public ShareNoteRepository getDAO() {
		return shareNoteRepository;
	}
	
	public void createShareNote(String text, String nickName, String dateStr, User user) {
		Date date = !StringUtils.isEmpty(dateStr) ? DateTimeUtils.toDate(dateStr) : new Date();
		
		saveNote(text, date, user);
	}
	
	public void createShareNote() {
		User user = ThreadLocalUtils.getCurrentUser();
		Date sendDate = ThreadLocalUtils.getSendDate();
		String text = ThreadLocalUtils.getMailContent();
		if(!StringUtils.isEmpty(text)) saveNote(text, sendDate, user);
	}
	
	@KeyMethodMapping("key.to.method.create-share-note-for-others")
	public void createShareNote(String userName, String date) {
		User user = userService.findByUsername(userName);
		
		Date sendDate = null;
		if(StringUtils.isEmpty(date)) {
			sendDate = new Date();
		} else {
			sendDate = DateTimeUtils.toDate(date, DateTimeUtils.DATE_MASK);
		}
		String text = ThreadLocalUtils.getMailContent();
		
		if(user != null && !StringUtils.isEmpty(text)) saveNote(text, sendDate, user);
	}
	
	public void createShareNoteFromMail(String text, Date date, User user) {
		
		if(StringUtils.isEmpty(text) || user == null || date == null) return;
		
		saveNote(text, date, user);
	}
	
	private void saveNote(String text, Date date, User user) {
		List<ShareNote> shareNotes = getDAO().findByNickNameAndDate(user.getUsername(), DateTimeUtils.toStr(date));
		boolean alreadyExist = false;
		text = text.trim();
		for (ShareNote shareNote : shareNotes) {
			double percent = StringUtils.similarity(text, shareNote.getText());
			System.out.println(percent);
			if(percent >= 0.8d){
				alreadyExist = true;
				shareNote.setText(text);
				save(shareNote);
				return;
			}
		}
		if(!alreadyExist) {
			ShareNote shareNote = new ShareNote();
			shareNote.setNickName(user.getUsername());
			shareNote.setText(text);
			shareNote.setCreationDate(new Date());
			
			shareNote.setModificationDate(date);
			shareNote.setUser(user);
			save(shareNote);
		}
	}
	
	 public static void main(String[] args) throws IOException, DocumentException {
	       
	 }
}
