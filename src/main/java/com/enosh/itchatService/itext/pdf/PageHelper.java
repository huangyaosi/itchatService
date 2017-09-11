package com.enosh.itchatService.itext.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class PageHelper extends PdfPageEventHelper{
	private Font font;
	private String footer;
	public PageHelper(Font font, String footer){
		this.font = font;
		this.footer = footer;
	}
	
	@Override
	public void onEndPage(PdfWriter writer,Document document) {
		PdfContentByte cb = writer.getDirectContent();
        Phrase f = new Phrase(footer, font);
        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, f, document.right()-10, document.bottom() - 10, 0);
	}
}
