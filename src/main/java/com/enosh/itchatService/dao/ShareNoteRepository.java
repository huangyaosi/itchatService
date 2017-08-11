package com.enosh.itchatService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.enosh.itchatService.model.ShareNote;

@Repository
public interface ShareNoteRepository extends AbsRepository<ShareNote>{
	public List<ShareNote> findByNickNameOrderByCreationDateAsc(String nickName);
	
	@Query("select s from ShareNote s where nickName=?1 and to_char(creationDate,'YYYYMM')=?2")
	public List<ShareNote> findByNickNameAndMonth(String nickName, String month);
	
	@Query("select s from ShareNote s where nickName=?1 and to_char(creationDate,'YYYY-MM-DD')=?2")
	public List<ShareNote> findByNickNameAndDate(String nickName, String date);
}
