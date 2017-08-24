package com.enosh.itchatService.dao;

import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.enosh.itchatService.model.Note;
import com.enosh.itchatService.model.NoteType;
import com.enosh.itchatService.model.ShareNote;
import com.enosh.itchatService.model.User;
import com.enosh.itchatService.utils.DateTimeUtils;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
//@TestPropertySource(locations = "classpath:config-${spring.profiles.active}.properties")
public class RepositoryTest {
	@Autowired private TestEntityManager entityManager;
	
	@Autowired private ShareNoteRepository shareNoteRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private NoteRepository noteRepository;
	
	@Test
    public void testshareNoteRepository() throws Exception {
		ShareNote shareNote = new ShareNote();
		Date date = new Date();
		shareNote.setNickName("Enosh");
		shareNote.setCreationDate(date);
		shareNote.setModificationDate(date);
		shareNote.setText("good day!");
		this.entityManager.persist(shareNote);
		entityManager.flush();
		
		List<ShareNote> shareNotes = shareNoteRepository.findByNickNameAndDate("Enosh", DateTimeUtils.toStr(date));
		Assertions.assertThat(shareNotes).isNotNull();
		Assertions.assertThat(shareNotes.isEmpty()).isFalse();
		Assertions.assertThat(shareNotes.get(0).getText()).isEqualTo("good day!");
	}
	
	@Test
    public void testUserRepository() throws Exception {
		String name = "Enosh";
		String email1 = "hcw123@sina.com";
		String email2 = "hcw124@sina.com";
		User user = new User(name);
		user.addEmail(email1);
		user.addEmail(email2);
		this.entityManager.persist(user);
		entityManager.flush();
		
		User u1 = userRepository.findByUsername(name);
		User u2 = userRepository.findByEmail(email1, email1);
		User u3 = userRepository.findByEmail(email2, email2);
		Assertions.assertThat(u1).isNotNull();
		Assertions.assertThat(u1.getUsername()).isEqualTo(name);
		Assertions.assertThat(u2).isNotNull();
		Assertions.assertThat(u2.getPrimaryEmail()).isEqualTo(email1);
		Assertions.assertThat(u3).isNotNull();
		Assertions.assertThat(u3.getOtherEmails()).isEqualTo(email2);
	}
	
	@Test
    public void testNoteRepository() throws Exception {
		String name = "Enosh";
		String email1 = "hcw123@sina.com";
		String email2 = "hcw124@sina.com";
		User user = new User(name);
		user.addEmail(email1);
		user.addEmail(email2);
		this.entityManager.persist(user);
		
		String tag = "世界因你而不同";
		String alias= "sj";
		NoteType noteType = new NoteType();
		noteType.setUser(user);
		noteType.setTag(tag);
		noteType.setAlias(alias);
		this.entityManager.persist(noteType);
		
		String text = "You are good at what you love.";
		Note note = new Note();
		note.setUser(user);
		note.setCreationDate(new Date());
		note.setModificationDate(new Date());
		note.setNoteType(noteType);
		note.setText(text);
		this.entityManager.persist(note);
		this.entityManager.flush();
		
		noteRepository.findByCreationDate(user, DateTimeUtils.toStr(new Date(), DateTimeUtils.DATE_MASK));
		List<Note> usetNotes = noteRepository.findByUser(user);
		Assertions.assertThat(usetNotes.get(0).getText()).isEqualTo(text);
		Assertions.assertThat(usetNotes.get(0).getNoteType().getTag()).isEqualTo(tag);
	}
}
