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

import com.enosh.itchatService.service.FinanceOfBroService;
import com.enosh.itchatService.service.ItextService;
import com.enosh.itchatService.service.ShareNoteService;

@RestController
public class ItchatController {
	
	@Autowired private ShareNoteService shareNoteService;
	@Autowired private FinanceOfBroService financeOfBroService;
	@Autowired private ItextService itextService;
	
	@RequestMapping("/save")
    public String saveText(@RequestParam(required=true)String text, 
    		@RequestParam(required=true) String nickName,
    		@RequestParam(required=false) String date) {
		shareNoteService.createShareNote(text, nickName, date);
        return "Save note successfully !";    
    }
	
	@RequestMapping("/updateFinance")
    public String updateFinance(@RequestParam(required=true)int remain, 
    		@RequestParam(required=true)int lastRemain,
    		@RequestParam(required=true)String name,
    		@RequestParam(required=true)String description) {
		
		financeOfBroService.createNew(remain, lastRemain, name, description);
        return "Save finance successfully !";    
    }
	
	@RequestMapping("/getLatestFinance")
    public String getLatestFinance() {
        return financeOfBroService.getLatestFinanceJson();    
    }
	
	@RequestMapping("/generatePdf")
    public ResponseEntity<Resource> generatePdf(@RequestParam(required = true)String nickName, 
    		@RequestParam(required = true)String month) {
		
        File file = itextService.createPdf(nickName, month);
        if(file == null) return null;
        
        HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.set("Content-disposition", "attachment; filename=" + file.getName());
		FileSystemResource fileSystemResource = new FileSystemResource(file);
		
        return new ResponseEntity<Resource>(fileSystemResource, headers, HttpStatus.OK);
    }
}
