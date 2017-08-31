package com.enosh.itchatService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.enosh.itchatService.model.NoteType;
import com.enosh.itchatService.model.User;

@Repository
public interface NoteTypeRepository extends AbsRepository<NoteType>{
	
	public NoteType findByUserAndTag(User user, String tag);
	
	public NoteType findByUserAndAlias(User user, String alias);
	
	@Query("select n from NoteType n where n.user=?1 and (n.tag=?2 or n.alias=?2)")
	public NoteType findByUserAndEqTagOrAlias(User user, String tagOrAlias);
	
	@Query("select n from NoteType n where n.user=?1 and (n.tag=?2 or (n.alias=?3 and n.alias is not null))")
	public NoteType findByTagOrAlias(User user, String tag, String alias);
	
	public List<NoteType> findByUserAndCompletedAndGenereated(User user, boolean completed, boolean genereated);
}
