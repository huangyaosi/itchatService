package com.enosh.itchatService.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enosh.itchatService.dao.ShareNoteRepository;
import com.enosh.itchatService.model.ShareNote;
import com.enosh.itchatService.utils.DateTimeUtils;
import com.enosh.itchatService.utils.StringUtils;

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
		
		List<ShareNote> shareNotes = getDAO().findByNickNameAndDate(nickName, DateTimeUtils.toStr(date));
		boolean alreadyExist = false;
		for (ShareNote shareNote : shareNotes) {
			double percent = StringUtils.similarity(text, shareNote.getText());
			System.out.println(percent);
			if(percent >= 0.8d){
				alreadyExist = true;
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
}
