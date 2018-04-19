package com.enosh.note.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.enosh.note.model.ShareNote;

@Repository
public interface ShareNoteRepository extends AbsRepository<ShareNote>{
	
	@Query("select s from ShareNote s where nickName=?1 order by to_char(creationDate,'YYYYMMDD') asc")
	public List<ShareNote> findByNickNameOrderByCreationDateAsc(String nickName);
	
	@Query("select s from ShareNote s where nickName=?1 and to_char(creationDate,'YYYYMM')>=?2 and to_char(creationDate,'YYYYMM')<=?3 order by to_char(creationDate,'YYYYMMDD') asc")
	public List<ShareNote> findByNickNameAndMonth(String nickName, String fromMonth, String toMonth);
	
	@Query("select s from ShareNote s where nickName=?1 and to_char(creationDate,'YYYY-MM-DD')=?2")
	public List<ShareNote> findByNickNameAndDate(String nickName, String date);
	
	@Query("select s from ShareNote s where nickName=?1 and to_char(creationDate,'YYYYMMDD')>=?2 and to_char(creationDate,'YYYYMMDD')<=?3 order by to_char(creationDate,'YYYYMMDD') asc")
	public List<ShareNote> findByNickNameAndDuringDate(String nickName, String fromDate, String toDate);
	
}
