package com.enosh.itchatService.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.enosh.itchatService.config.MailSenderConfig;
import com.enosh.itchatService.config.PdfConfig;
import com.enosh.itchatService.model.Note;
import com.enosh.itchatService.model.NoteType;
import com.enosh.itchatService.model.ShareNote;
import com.enosh.itchatService.model.User;
import com.enosh.itchatService.utils.DateTimeUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

@Service
public class ItextService {
    
    @Autowired ShareNoteService shareNoteService;
    @Autowired PdfConfig pdfConfig;
    @Autowired UserService userService;
    @Autowired MailSenderService mailSenderService;
    @Autowired MailSenderConfig mailSenderConfig;
    @Autowired NoteTypeService noteTypeService;
    @Autowired NoteService noteService;
    
    public File createPdf(String nickName, String fromMonth, String toMonth) {
    	List<ShareNote> shareNotes = shareNoteService.findByNickNameAndMonth(nickName, fromMonth, toMonth);
    	if(shareNotes == null || shareNotes.size() <=0) return null;
		String monthStr = "";
		if (fromMonth.trim().equals(toMonth.trim())) {
			monthStr = fromMonth;
		} else {
			monthStr = fromMonth + " - " + toMonth;
		}
    	String dir = pdfConfig.getDirectoryPath() + monthStr + "_" + nickName + ".pdf";
    	File file = new File(dir);
        file.getParentFile().mkdirs();
        Document document = new Document();
        try {
			PdfWriter.getInstance(document, new FileOutputStream(dir));
			document.open();
	        BaseFont bf;
	        bf = BaseFont.createFont(pdfConfig.getFrontPath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
	        Font titleFont = new Font(bf, 20f, Font.NORMAL);
	        Font dateFont = new Font(bf, 8f, Font.BOLDITALIC);
	        Font contentFont = new Font(bf, 12f, Font.NORMAL);
	        
	        CustomDashedLineSeparator separator = new CustomDashedLineSeparator();
	        separator.setDash(5);
	        separator.setGap(8);
	        separator.setLineWidth(1);
	        Chunk linebreak = new Chunk(separator);
	        
	        //title
	        String title = pdfConfig.getShareNoteTitle() + "(" + monthStr + " " + nickName + ")";
	        Paragraph titleParagrah = new Paragraph(title, titleFont);
	        titleParagrah.setAlignment(Paragraph.ALIGN_CENTER);
	        Chapter chapter = new Chapter(titleParagrah, 1);
	        chapter.setNumberDepth(0);
	 
	        for (ShareNote shareNote : shareNotes) {
	        	Chunk dateChunk = new Chunk(DateTimeUtils.toStr(shareNote.getCreationDate()), dateFont);
	        	dateChunk.setBackground(BaseColor.GREEN);
	        	Paragraph dateSection = new Paragraph(dateChunk);
	        	dateSection.setAlignment(Paragraph.ALIGN_RIGHT);
	        	Paragraph content = new Paragraph(shareNote.getText(), contentFont);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
    }
    
    public File createPdf(User user, String fromMonth, String toMonth) {
    	return createPdf(user.getUsername(), fromMonth, toMonth);
    }
    
    public File createPdfForNote(String userName, String bookTagOrAlias, Document document) {
    	User user = userService.findByUsername(userName);
    	if(user == null) return null;
    	
    	NoteType noteType = noteTypeService.findByUserAndEqTagOrAlias(user, bookTagOrAlias);
    	if(noteType == null) return null;
    	
    	if(document == null)  {
    		document = new Document();
    		String dir = pdfConfig.getDirectoryPath() + noteType.getTag() + "_" + user.getUsername() + ".pdf";
    		File file = new File(dir);
            file.getParentFile().mkdirs();
    		try {
    			PdfWriter.getInstance(document, new FileOutputStream(dir));
    			document.open();
    			createPdfForNote(document, user, noteType);
    			document.close();
    			return file;
    		} catch (FileNotFoundException | DocumentException e) {
    			e.printStackTrace();
    		}
    	}
    	createPdfForNote(document, user, noteType);
    	return null;
    }
    
    public void createPdfForNote(Document document, User user, NoteType noteType) {
    	
    	List<Note> notes = noteService.findByUserAndNoteType(user, noteType);
    	
    	String dir = pdfConfig.getDirectoryPath() + noteType.getTag() + "_" + user.getUsername() + ".pdf";
        
        try {
			PdfWriter.getInstance(document, new FileOutputStream(dir));
			document.open();
			
	        BaseFont bf;
			bf = BaseFont.createFont(pdfConfig.getFrontPath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
	        Font titleFont = new Font(bf, 20f, Font.NORMAL);
	        Font dateFont = new Font(bf, 8f, Font.BOLDITALIC);
	        Font contentFont = new Font(bf, 12f, Font.NORMAL);
	        
	        CustomDashedLineSeparator separator = new CustomDashedLineSeparator();
	        separator.setDash(5);
	        separator.setGap(8);
	        separator.setLineWidth(1);
	        Chunk linebreak = new Chunk(separator);
	        
	        //title
	        String title = noteType.getTag() + "(" + user.getUsername() + ")";
	        Paragraph titleParagrah = new Paragraph(title, titleFont);
	        titleParagrah.setAlignment(Paragraph.ALIGN_CENTER);
	        Chapter chapter = new Chapter(titleParagrah, 1);
	        chapter.setNumberDepth(0);
	        
	        for (Note note : notes) {
	    		Chunk dateChunk = new Chunk(DateTimeUtils.toStr(note.getCreationDate()), dateFont);
	        	dateChunk.setBackground(BaseColor.GREEN);
	        	Paragraph dateSection = new Paragraph(dateChunk);
	        	dateSection.setAlignment(Paragraph.ALIGN_RIGHT);
	        	Paragraph content = new Paragraph(note.getText(), contentFont);
	        	chapter.add(dateSection);
	        	chapter.add(content);
	        	chapter.add(new Paragraph(linebreak));
			}
	        document.add(chapter);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
    	
    }
    
    // every the next month's first date at 8:00 to generate share note and email for all.
	@Scheduled(cron = "0 0 8 1 * ?")
//	@Scheduled(cron = "*/40 * * * * *")
    public void batchCreatePdf() {
		System.out.println("Start generate shareNote and email for all...");
    	Iterable<User> users = userService.getDAO().findAll();
    	List<User> userList = new ArrayList<User>();
    	for (User user : users) {
			if(!StringUtils.isEmpty(user.getPrimaryEmail())) userList.add(user);
		}
    	
    	String month = DateTimeUtils.toStr(DateTimeUtils.getLastMonthTheDate(), DateTimeUtils.YEAR_MONTH_MASK);
    	for (User user : userList) {
    		File file = createPdf(user, month, month);
    		if(file != null && file.exists()) {
    			String content = "";
    			String subject = user.getUsername() + " " + month + mailSenderConfig.getSubjectPdf();
    			mailSenderService.sendEmailWithAttachment(user.getPrimaryEmail(), subject, content, file);
    		}
		}
    }
    
//	@Scheduled(cron = "0 0 8 * * *")
	public void createPdfForNote() {
		Iterable<User> users = userService.getDAO().findAll();
    	List<User> userList = new ArrayList<User>();
    	for (User user : users) {
    		List<NoteType> noteTypes = noteTypeService.findByUserAndCompletedAndGenereated(user, true, false);
			if(!StringUtils.isEmpty(user.getPrimaryEmail())) userList.add(user);
		}
	}
	
    class CustomDashedLineSeparator extends DottedLineSeparator {
        protected float dash = 5;
        protected float phase = 2.5f;
     
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
    
	class PageBackgroundHelper extends PdfPageEventHelper {
		private Image image;
		private float startPosition; 
		public PageBackgroundHelper(Image image){
			this.image = image;
		}
		
		@Override
		public void onEndPage(PdfWriter writer,Document document) {
			try {
				float width = document.getPageSize().getWidth();
		        float height = document.getPageSize().getHeight();
				writer.getDirectContentUnder().addImage(image, width, 0, 0, height, 0, 0);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		
//		@Override
//		public void onParagraph(PdfWriter writer,Document document,float paragraphPosition) {
//			this.startPosition = paragraphPosition;
//	    }
//		
//		@Override
//		public void onParagraphEnd(PdfWriter writer, Document document, float paragraphPosition) {
//			try {
//				float width = document.right() - document.left();
//		        float height = startPosition - paragraphPosition;
//				writer.getDirectContentUnder().addImage(image, width, 0, 0, height, startPosition, paragraphPosition);
//			} catch (DocumentException e) {
//				e.printStackTrace();
//			}
//		}
	}
    
    public void createPdfTest(String dest) throws IOException, DocumentException {
    	String imagePath = "C:\\Users\\hcunwei\\Library\\tem\\t4.jpg";
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPageEvent endPageEvent = new PageBackgroundHelper(Image.getInstance(imagePath));
        writer.setPageEvent(endPageEvent);
        
        BaseFont bf = BaseFont.createFont("C:/Users/hcunwei/Documents/Resource/Tools/Font/ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font chapterFont = new Font(bf, 12f);
        Font paragraphFont = new Font(bf, 8f);
//        Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLDITALIC);
//        Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
       
        Chapter chapter = new Chapter(1);
        chapter.setNumberDepth(0);
        for (int i = 0; i < 50; i++) {
        	Chunk chunk = new Chunk("嗯嗯, 天天都是新的，\n今天开心不开心都无所谓\n但超越了感受。", chapterFont);
            Paragraph p = new Paragraph(chunk);
            p.setIndentationLeft(20);
            p.setIndentationRight(20);
        	chapter.add(p);
		}
        chapter.add(new Paragraph("This is the paragraph", paragraphFont));
        document.add(chapter);
        
        document.close();
    }
    
    public static void main(String[] args) throws IOException, DocumentException {
    	String dest = "C:/Users/hcunwei/Library/tem/chapter_title.pdf";
        File file = new File(dest);
        file.getParentFile().mkdirs();
        new ItextService().createPdfTest(dest);
    }
}
