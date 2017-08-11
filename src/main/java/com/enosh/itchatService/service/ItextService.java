package com.enosh.itchatService.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enosh.itchatService.config.PdfConfig;
import com.enosh.itchatService.model.ShareNote;
import com.enosh.itchatService.utils.DateTimeUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

@Service
public class ItextService {
    
    @Autowired ShareNoteService shareNoteService;
    @Autowired PdfConfig pdfConfig;
    
    public File createPdf(String nickName, String month) {
    	List<ShareNote> shareNotes = shareNoteService.findByNickName(nickName, month);
    	if(shareNotes == null || shareNotes.size() <=0) return null;
    	String dir = pdfConfig.getDirectoryPath() + DateTimeUtils.toStr(new Date()) + "_" + nickName + ".pdf";
    	File file = new File(dir);
        file.getParentFile().mkdirs();
        Document document = new Document();
        try {
			PdfWriter.getInstance(document, new FileOutputStream(dir));
			document.open();
	        BaseFont bf;
//	        String path = new File(".").getCanonicalPath();
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
	        String title = pdfConfig.getShareNoteTitle() + "(" + month + " " + nickName + ")";
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
    
    public void createPdfTest(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        BaseFont bf = BaseFont.createFont("C:/Users/hcunwei/Documents/Resource/Tools/Font/ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font chapterFont = new Font(bf, 12f);
        Font paragraphFont = new Font(bf, 8f);
//        Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLDITALIC);
//        Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
        Chunk chunk = new Chunk("嗯嗯", chapterFont);
        Chapter chapter = new Chapter(new Paragraph(chunk), 1);
        chapter.setNumberDepth(0);
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
