package com.enosh.itchatService.itext.pdf;

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

public class ItextFonts {
	static BaseFont bf = null;
	public static Font SHARE_NOTE_TITLE_FONT;
	public static Font SHARE_NOTE_DATE_FONT;
	public static Font SHARE_NOTE_CONTENT_FONT;
	public static Font SHARE_NOTE_FOOTER_FONT;
	
	public static Font NOTE_TITLE_FONT;
	public static Font NOTE_DATE_FONT;
	public static Font NOTE_CONTENT_FONT;
	public static Font NOTE_FOOTER_FONT;
	static {
		try {
			bf = BaseFont.createFont(System.getProperty("itext.font.path"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			SHARE_NOTE_TITLE_FONT = new Font(bf, 20f, Font.NORMAL);
			SHARE_NOTE_DATE_FONT = new Font(bf, 8f, Font.BOLDITALIC);
			SHARE_NOTE_CONTENT_FONT = new Font(bf, 12f, Font.NORMAL);
			SHARE_NOTE_FOOTER_FONT = new Font(bf, 7f, Font.ITALIC);
			
			NOTE_TITLE_FONT = new Font(bf, 20f, Font.NORMAL);
			NOTE_DATE_FONT = new Font(bf, 6f, Font.BOLDITALIC);
			NOTE_CONTENT_FONT = new Font(bf, 12f, Font.NORMAL);
			NOTE_FOOTER_FONT = new Font(bf, 6f, Font.ITALIC);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
