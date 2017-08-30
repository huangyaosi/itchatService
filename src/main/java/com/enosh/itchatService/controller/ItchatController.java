package com.enosh.itchatService.controller;

import java.io.File;

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

import com.enosh.itchatService.service.ItextService;
import com.enosh.itchatService.service.ShareNoteService;
import com.enosh.itchatService.service.UserService;

@RestController
public class ItchatController {
	
	@Autowired private ShareNoteService shareNoteService;
	@Autowired private ItextService itextService;
	@Autowired private UserService userService;
	
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
		
        File file = itextService.createPdf(nickName, fromMonth, toMonth);
        if(file == null) return null;
        
        HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.set("Content-disposition", "attachment; filename=" + file.getName());
		FileSystemResource fileSystemResource = new FileSystemResource(file);
		
        return new ResponseEntity<Resource>(fileSystemResource, headers, HttpStatus.OK);
    }
	
	@RequestMapping("/createUser")
    public String createUser(@RequestParam(required = true)String userName,
    		@RequestParam(required = false)String email) {
		
		return userService.createUser(userName, email);
    }
	
	@RequestMapping("/getBookNote")
	public ResponseEntity<Resource> getBookNote(@RequestParam(required = true)String userName, 
    		@RequestParam(required = true)String bookTagOrAlias) {
		
		File file = itextService.createPdfForNote(userName, bookTagOrAlias, null);
		if(file == null) return null;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.set("Content-disposition", "attachment; filename=" + file.getName());
		FileSystemResource fileSystemResource = new FileSystemResource(file);
		
        return new ResponseEntity<Resource>(fileSystemResource, headers, HttpStatus.OK);
	}
}
