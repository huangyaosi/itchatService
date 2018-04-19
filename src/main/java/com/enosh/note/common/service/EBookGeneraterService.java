package com.enosh.note.common.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.enosh.note.common.model.MailEntity;
import com.enosh.note.config.bean.MailSenderConfig;
import com.enosh.note.config.bean.MailTemplates;
import com.enosh.note.config.bean.PdfConfig;
import com.enosh.note.dispatcher.KeyMethodMapping;
import com.enosh.note.itext.pdf.ItextFonts;
import com.enosh.note.itext.pdf.PageHelper;
import com.enosh.note.model.Note;
import com.enosh.note.model.NoteType;
import com.enosh.note.model.ShareNote;
import com.enosh.note.model.User;
import com.enosh.note.model.service.NoteService;
import com.enosh.note.model.service.NoteTypeService;
import com.enosh.note.model.service.ShareNoteService;
import com.enosh.note.model.service.UserService;
import com.enosh.note.utils.DateTimeUtils;
import com.enosh.note.utils.ThreadLocalUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.tool.xml.XMLWorkerHelper;

@Service
@KeyMethodMapping
public class EBookGeneraterService {
    
    @Autowired ShareNoteService shareNoteService;
    @Autowired PdfConfig pdfConfig;
    @Autowired UserService userService;
    @Autowired MailSenderService mailSenderService;
    @Autowired MailSenderConfig mailSenderConfig;
    @Autowired NoteTypeService noteTypeService;
    @Autowired NoteService noteService;
    @Autowired MailTemplates mailTemplates;
    
    public File createPdfForShareNote(String nickName, String fromMonth, String toMonth) {
    	List<ShareNote> shareNotes = shareNoteService.findByNickNameAndMonth(nickName, fromMonth, toMonth);
    	if(shareNotes == null || shareNotes.size() <=0) return null;
		String monthStr = "";
		if (fromMonth.trim().equals(toMonth.trim())) {
			monthStr = fromMonth;
		} else {
			monthStr = fromMonth + " -- " + toMonth;
		}
    	String dir = pdfConfig.getDirectoryPath() + monthStr + "_" + nickName + ".pdf";
    	
    	String title = pdfConfig.getShareNoteTitle() + "(" + monthStr + " " + nickName + ")";
        return createPdfForShareNote(shareNotes, dir, title);
    }
    
    public File createPdfForShareNoteDuringDate(String nickName, String fromDate, String toDate) {
    	List<ShareNote> shareNotes = shareNoteService.findByNickNameAndDuringDate(nickName, fromDate, toDate);
    	if(shareNotes == null || shareNotes.size() <=0) return null;
		String dateStr = "";
		if (fromDate.trim().equals(toDate.trim())) {
			dateStr = fromDate;
		} else {
			dateStr = fromDate + " - " + toDate;
		}
    	String dir = pdfConfig.getDirectoryPath() + dateStr + "_" + nickName + ".pdf";
    	
    	String title = pdfConfig.getShareNoteTitle() + "(" + dateStr + " " + nickName + ")";
    	
    	return createPdfForShareNote(shareNotes, dir, title);
    }
    
    public File createPdfForShareNote(List<ShareNote> shareNotes, String filePath, String title) {
    	if(shareNotes == null || shareNotes.size() <=0) return null;
    	File file = new File(filePath);
        file.getParentFile().mkdirs();
        Document document = new Document();
        try {
			PdfWriter.getInstance(document, new FileOutputStream(filePath));
			document.open();
	        
	        CustomDashedLineSeparator separator = new CustomDashedLineSeparator(5f, 2.5f);
	        separator.setDash(5);
	        separator.setGap(8);
	        separator.setLineWidth(1);
	        Chunk linebreak = new Chunk(separator);
	        
	        //title
	        Paragraph titleParagrah = new Paragraph(title, ItextFonts.SHARE_NOTE_TITLE_FONT);
	        titleParagrah.setAlignment(Paragraph.ALIGN_CENTER);
	        Chapter chapter = new Chapter(titleParagrah, 1);
	        chapter.setNumberDepth(0);
	 
	        for (ShareNote shareNote : shareNotes) {
	        	Chunk dateChunk = new Chunk(DateTimeUtils.toStr(shareNote.getCreationDate()), ItextFonts.SHARE_NOTE_DATE_FONT);
	        	dateChunk.setBackground(BaseColor.GREEN);
	        	Paragraph dateSection = new Paragraph(dateChunk);
	        	dateSection.setAlignment(Paragraph.ALIGN_RIGHT);
	        	Paragraph content = new Paragraph(shareNote.getText(), ItextFonts.SHARE_NOTE_CONTENT_FONT);
	        	chapter.add(dateSection);
	        	chapter.add(content);
	        	chapter.add(new Paragraph(linebreak));
			}
	        
	        document.add(chapter);
	        document.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return file;
    }
    public File genereatePdfForShareNote(User user, String fromMonth, String toMonth) {
    	return createPdfForShareNote(user.getUsername(), fromMonth, toMonth);
    }
    
    public void createPdfForNote(Document document, User user, NoteType noteType) {
    	
    	List<Note> notes = noteService.findByUserAndNoteType(user, noteType);
    	
        try {
	        //title
	        String title = noteType.getTag();
	        Paragraph titleParagrah = new Paragraph(title, ItextFonts.NOTE_TITLE_FONT);
	        titleParagrah.setAlignment(Paragraph.ALIGN_CENTER);
	        Chapter chapter = new Chapter(titleParagrah, 1);
	        chapter.setNumberDepth(0);
	        
	        for (Note note : notes) {
	    		Chunk dateChunk = new Chunk("----" + DateTimeUtils.toStr(note.getCreationDate()), ItextFonts.NOTE_DATE_FONT);
	        	Paragraph dateSection = new Paragraph(dateChunk);
	        	dateSection.setAlignment(Paragraph.ALIGN_RIGHT);
	        	Paragraph content = new Paragraph(note.getText(), ItextFonts.NOTE_CONTENT_FONT);
	        	chapter.add(content);
	        	chapter.add(dateSection);
			}
	        document.add(chapter);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
    	
    }
//    public List<Element> parseHTML(String text) {
//    	List<Element> elements = new ArrayList<>();
//    	if(!StringUtils.isEmpty(text) && text.indexOf("div") > -1) {
//    		org.jsoup.nodes.Document doc = Jsoup.parse(text);
//			try {
//				List<Element> es = HTMLWorker.parseToList(new StringReader(doc.outerHtml()), null);
//	            for (Element e : es) {
//	                Element ele = (Element) e;
//	                for (Chunk Chunk : ele.getChunks()) {
//	    				if(Chunk != null && Chunk.getContent()!=null) {
//	    					elements.add(Chunk);
//	    				}
//	    			}
//	            }
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//    	} else {
//    		elements.add(new Chunk(text));
//    	}
//    	
//    	return elements;
//    }
    // every the next month's first date at 8:00 to generate share note and email for all.
    // every Saturday
//	@Scheduled(cron = "0 0 8 1 * ?")
    @Scheduled(cron = "0 30 7 ? * 7")
    public void batchCreatePdfForShareNote() {
		System.out.println("Start generate shareNote and email for all...");
    	Iterable<User> users = userService.findAll();
    	List<User> userList = new ArrayList<User>();
    	for (User user : users) {
			if(!StringUtils.isEmpty(user.getPrimaryEmail())) userList.add(user);
		}
    	
    	String month = DateTimeUtils.toStr(DateTimeUtils.getLastMonthTheDate(), DateTimeUtils.YEAR_MONTH_MASK);
    	for (User user : userList) {
    		File file = genereatePdfForShareNote(user, month, month);
    		if(file != null && file.exists()) {
    			String content = "";
    			String subject = user.getUsername() + " " + month + mailSenderConfig.getSubjectPdf();
    			mailSenderService.sendEmailWithAttachment(user.getPrimaryEmail(), subject, content, file);
    		}
		}
    }
    
    public void batchGenerateShareNoteByWeek(String fromDate, String toDate) {
		System.out.println("Start generate weekly share note and email for all...");
    	Iterable<User> users = userService.findAll();
    	List<User> userList = new ArrayList<User>();
    	for (User user : users) {
			if(!StringUtils.isEmpty(user.getPrimaryEmail())) userList.add(user);
		}
    	
    	for (User user : userList) {
    		File file = createPdfForShareNoteDuringDate(user.getUsername(), fromDate, toDate);
    		if(file != null && file.exists()) {
    			MailEntity mailEntity = new MailEntity(user.getPrimaryEmail(), mailTemplates.getShareNoteWeeklyGeneration());
    			Map<String, String> map = new HashMap<String, String>();
    			map.put("username", user.getUsername());
    			map.put("fromDate", fromDate);
    			map.put("toDate", toDate);
    			mailEntity.setMap(map);
    			mailEntity.addAttachments(file);
    			mailSenderService.sendEmail(mailEntity);
    		}
		}
    }
    
	@KeyMethodMapping("key.to.method.generate-personal-note")
	public File generateNote(String books) {
		
		File file = null;
		List<NoteType> noteTypes = null;
		User user = ThreadLocalUtils.getCurrentUser();
		if(StringUtils.isEmpty(books)) {
			noteTypes = noteTypeService.findByUser(user);
		} else {
			String[] tagArr = books.split(" ");
			List<String> tagList =  new ArrayList<String>(Arrays.asList(tagArr));
			noteTypes = noteTypeService.findByUserAndEqTagOrAliasIn(user, tagList);
		}
		
		if(!StringUtils.isEmpty(user.getPrimaryEmail())) {
			Document document = new Document();
    		String dir = pdfConfig.getDirectoryPath() + pdfConfig.getNoteTitle() + "_" + user.getUsername() + ".pdf";
    		file = new File(dir);
            file.getParentFile().mkdirs();
    		try {
    			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dir));
    	        
    			PageHelper pageHelper = new PageHelper(ItextFonts.NOTE_FOOTER_FONT, pdfConfig.getFooter() + user.getUsername());
    			writer.setPageEvent(pageHelper);
    			document.open();
    			for (NoteType noteType : noteTypes) {
    				createPdfForNote(document, user, noteType);
    			}
    			document.close();
    		} catch (DocumentException | IOException e) {
    			e.printStackTrace();
    		}
		}
		
		if(file != null) {
			MailEntity mailEntity = new MailEntity(user.getPrimaryEmail(), mailTemplates.getNoteGeneration());
			Map<String, String> map = new HashMap<String, String>();
			map.put("username", user.getUsername());
			mailEntity.setMap(map);
			mailEntity.addAttachments(file);
			mailSenderService.sendEmail(mailEntity);
		}
		return file;
	}
	
	@KeyMethodMapping("key.to.method.generate-share-note-for-all")
	public void generateShareNoteForAll(String fromMonth, String toMonth) {
		System.out.println("Trigger by email. Start generate shareNote and email for all...");
    	Iterable<User> users = userService.findAll();
    	List<User> userList = new ArrayList<User>();
    	
    	for (User user : users) {
			if(!StringUtils.isEmpty(user.getPrimaryEmail())) userList.add(user);
		}
    	
    	for (User user : userList) {
    		File file = genereatePdfForShareNote(user, fromMonth, toMonth);
    		if(file != null && file.exists()) {
    			String content = "Please don't reply, thanks";
    			String subject = getSubject(user, fromMonth, toMonth);
    			mailSenderService.sendEmailWithAttachment(user.getPrimaryEmail(), subject, content, file);
    		}
		}
	}
	
	@KeyMethodMapping("key.to.method.generate-share-note-for-one")
	public void generateShareNoteForOne(String userName, String fromMonth, String toMonth) {
		System.out.println("Trigger by email. Start generate shareNote for..." + userName);
    	User user = userService.findByUsername(userName);
    	if(user != null && !StringUtils.isEmpty(user.getPrimaryEmail())) {
    		File file = genereatePdfForShareNote(user, fromMonth, toMonth);
    		if(file != null && file.exists()) {
    			String content = "Please don't reply, thanks";
    			String subject = getSubject(user, fromMonth, toMonth);
    			mailSenderService.sendEmailWithAttachment(user.getPrimaryEmail(), subject, content, file);
    		}
    	}
	}
	
	@KeyMethodMapping("key.to.method.generate-personal-share-note")
	public void generatePersonalShareNote(String fromMonth, String toMonth) {
    	User user = ThreadLocalUtils.getCurrentUser();
    	System.out.println("Trigger by email. Start generate personal shareNote for..." + user.getUsername());
    	if(user != null && !StringUtils.isEmpty(user.getPrimaryEmail())) {
    		File file = genereatePdfForShareNote(user, fromMonth, toMonth);
    		if(file != null && file.exists()) {
    			String content = "Please don't reply, thanks";
    			String subject = getSubject(user, fromMonth, toMonth);
    			mailSenderService.sendEmailWithAttachment(user.getPrimaryEmail(), subject, content, file);
    		}
    	}
	}
	
	public String getSubject(User user, String fromMonth, String toMonth) {
		String subject = "";
		if(!fromMonth.equals(toMonth)) {
			subject = user.getUsername() + " " + fromMonth + "-" + toMonth + mailSenderConfig.getSubjectPdf();
		} else {
			subject = user.getUsername() + " " + fromMonth + mailSenderConfig.getSubjectPdf();
		}
		return subject;
	}
	
    class CustomDashedLineSeparator extends DottedLineSeparator {
        private float dash = 5;
        private float phase = 2.5f;
     
        public CustomDashedLineSeparator(float dash, float phase) {
        	this.dash = dash;
        	this.phase = phase;
        }
        public float getDash() {
            return dash;
        }
     
        public float getPhase() {
            return phase;
        }
     
        public void setDash(float dash) {
            this.dash = dash;
        }
     
        public void setPhase(float phase) {
            this.phase = phase;
        }
     
        public void draw(PdfContentByte canvas,
            float llx, float lly, float urx, float ury, float y) {
            canvas.saveState();
            canvas.setLineWidth(lineWidth);
            canvas.setLineDash(dash, gap, phase);
            drawLine(canvas, llx, urx, y);
            canvas.restoreState();
        }
    }
    
    public static void main(String[] args) throws IOException, DocumentException {
		String dest = "C:/Users/hcunwei/Library/tem/t.pdf";
		String source = "C:/Users/hcunwei/Library/tem/t.html";
		Document document = new Document();
		// step 2
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
		// step 3
		writer.setTagged();
		document.open();
		// step 4
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(source));
		// step 5
		document.close();
    }
}
