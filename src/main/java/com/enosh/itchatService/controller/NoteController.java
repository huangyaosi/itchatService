package com.enosh.itchatService.controller;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enosh.itchatService.dispatcher.ThreadLocalUtils;
import com.enosh.itchatService.model.User;
import com.enosh.itchatService.service.EBookGeneraterService;
import com.enosh.itchatService.service.NoteTypeService;
import com.enosh.itchatService.service.ShareNoteService;
import com.enosh.itchatService.service.UserService;

@RestController
public class NoteController {
	
	@Autowired private ShareNoteService shareNoteService;
	@Autowired private EBookGeneraterService eBookGeneraterService;
	@Autowired private UserService userService;
	@Autowired private NoteTypeService noteTypeService;
	
	@RequestMapping("/save")
    public String saveText(@RequestParam(required=true)String text, 
    		@RequestParam(required=true) String nickName,
    		@RequestParam(required=false) String date) {
		shareNoteService.createShareNote(text, nickName, date, null);
        return "Save note successfully !";    
    }
	
	@RequestMapping("/generatePdf")
    public ResponseEntity<Resource> generatePdf(@RequestParam(required = true)String nickName, 
    		@RequestParam(required = true)String fromMonth,
    		@RequestParam(required = true)String toMonth) {
		
        File file = eBookGeneraterService.createPdfForShareNote(nickName, fromMonth, toMonth);
        if(file == null) return null;
        
        HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.set("Content-disposition", "attachment; filename=" + file.getName());
		FileSystemResource fileSystemResource = new FileSystemResource(file);
		
        return new ResponseEntity<Resource>(fileSystemResource, headers, HttpStatus.OK);
    }
	
	@RequestMapping("/batchGenerateShareNoteByWeek")
    public void batchGenerateShareNoteByWeek(@RequestParam(required = true)String fromDate,
    		@RequestParam(required = true)String toDate) {
		
        eBookGeneraterService.batchGenerateShareNoteByWeek(fromDate, toDate);
    }
	
	@RequestMapping("/createUser")
    public String createUser(@RequestParam(required = true)String userName,
    		@RequestParam(required = false)String email) {
		User user = userService.findByUsername(userName);
		if(user == null) return null;
		
		ThreadLocalUtils.init(user, new Date(), "");
		return userService.createUser(userName, email);
    }
	
	@RequestMapping("/getBookNote")
	public ResponseEntity<Resource> getBookNote(@RequestParam(required = true)String userName, 
    		@RequestParam(required = true)String tags) {
		
		User user = userService.findByUsername(userName);
		if(user == null) return null;
		
		ThreadLocalUtils.init(user, new Date(), "");
		
		File file = eBookGeneraterService.generateNote(tags);
		if(file == null) return null;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.set("Content-disposition", "attachment; filename=" + file.getName());
		FileSystemResource fileSystemResource = new FileSystemResource(file);
		
        return new ResponseEntity<Resource>(fileSystemResource, headers, HttpStatus.OK);
	}
	
	@RequestMapping("/createTag")
    public String createTag(@RequestParam(required=true)String username, 
    		@RequestParam(required=true) String bookTag,
    		@RequestParam(required=true) String alias,
    		@RequestParam(required=false) String description) {
		User user = userService.findByUsername(username);
		ThreadLocalUtils.init(user, new Date(), description);
		noteTypeService.createNoteType(bookTag, alias);
        return "Save note successfully !";    
    }
}
