package com.enosh.itchatService.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enosh.itchatService.dao.ShareNoteRepository;
import com.enosh.itchatService.model.ShareNote;
import com.enosh.itchatService.utils.DateTimeUtils;
import com.enosh.itchatService.utils.StringUtils;
import com.itextpdf.text.DocumentException;

@Service
public class ShareNoteService extends AbsService<ShareNote>{

	@Autowired private ShareNoteRepository shareNoteRepository;
	
	public List<ShareNote> findByNickName(String nickName, String month) {
		return shareNoteRepository.findByNickNameAndMonth(nickName, month);
	}

	@Override
	public ShareNoteRepository getDAO() {
		return shareNoteRepository;
	}
	
	public void createShareNote(String text, String nickName, String dateStr) {
		Date date = !StringUtils.isEmpty(dateStr) ? DateTimeUtils.toDate(dateStr) : new Date();
		
		saveNote(text, nickName, date);
	}
	
	public void createShareNoteFromMail(String text, String nickName, Date date) {
		
		if(StringUtils.isEmpty(text) || StringUtils.isEmpty(nickName) || date == null) return;
		
		saveNote(text, nickName, date);
	}
	
	private void saveNote(String text, String nickName, Date date) {
		List<ShareNote> shareNotes = getDAO().findByNickNameAndDate(nickName, DateTimeUtils.toStr(date));
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
			shareNote.setNickName(nickName);
			shareNote.setText(text);
			shareNote.setCreationDate(new Date());
			
			shareNote.setModificationDate(date);
			save(shareNote);
		}
	}
	
	 public static void main(String[] args) throws IOException, DocumentException {
	    	String text = "你的形象就心满意足了。 (诗篇 17:14-15 和合本)";
	    	String shareNotePatternStr = "[\\(|（|【].*+\\d{1,3}\\s*[:|：]\\s*\\d{1,3}\\s*-?\\s*\\d{0,3}.*[\\)|）|】]";
	    	Pattern shareNotePattern = Pattern.compile(shareNotePatternStr);
	    	Matcher m = shareNotePattern.matcher(text);
	    	if(m.find()) {
	    		System.out.println(text);
	    	}
	       
	 }
}
