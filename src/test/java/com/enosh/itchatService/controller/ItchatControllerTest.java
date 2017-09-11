package com.enosh.itchatService.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.enosh.itchatService.service.EBookGeneraterService;
import com.enosh.itchatService.service.ShareNoteService;
import com.enosh.itchatService.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(ItchatController.class)
public class ItchatControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean ShareNoteService shareNoteService;
	@MockBean private EBookGeneraterService eBookGeneraterService;
	@MockBean private UserService userService;
	
//	@Before
//	public void init() {
//		this.mockMvc = MockMvcBuilders.standaloneSetup(new ItchatController()).build();
//	}
//	
	@Test
    public void testSayHelloWorld() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/save")
        		.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
        		.param("text", "good day!")
        		.param("nickName", "Enosh~Huang"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
        
    }
}
