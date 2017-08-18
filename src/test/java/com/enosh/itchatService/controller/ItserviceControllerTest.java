package com.enosh.itchatService.controller;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.enosh.itchatService.App;
import com.enosh.itchatService.model.ShareNote;
import com.enosh.itchatService.service.ShareNoteService;
import com.enosh.itchatService.utils.DateTimeUtils;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {App.class})
public class ItserviceControllerTest {
	private MockMvc mockMvc;
	
	@Autowired ShareNoteService shareNoteService;
	
	@Before
	public void init() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(new ItchatController()).build();
	}
	
	@Test
    public void testSayHelloWorld() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/save")
        		.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
        		.param("text", "good day!")
        		.param("nickName", "Enosh~Huang"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        
        List<ShareNote> shareNote = shareNoteService.findByNickNameAndDate("Enosh~Huang", DateTimeUtils.toStr(new Date()));
        Assert.assertEquals(1, shareNote.size());
        Assert.assertEquals("Enosh~Huang", shareNote.get(0).getNickName());
        Assert.assertEquals("good day!", shareNote.get(0).getText());

    }
}
