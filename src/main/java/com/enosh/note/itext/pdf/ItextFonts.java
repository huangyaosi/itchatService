package com.enosh.note.itext.pdf;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

public class ItextFonts {
	static BaseFont bf = null;
	static {
		try {
			System.out.println("font path : " + System.getProperty("itext.font.path"));
			bf = BaseFont.createFont(System.getProperty("itext.font.path"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public final static Font SHARE_NOTE_TITLE_FONT = new Font(bf, 20f, Font.NORMAL);
	public final static Font SHARE_NOTE_DATE_FONT = new Font(bf, 8f, Font.BOLDITALIC);
	public final static Font SHARE_NOTE_CONTENT_FONT = new Font(bf, 12f, Font.NORMAL);
	public final static Font SHARE_NOTE_FOOTER_FONT = new Font(bf, 7f, Font.ITALIC);
	
	public final static Font NOTE_TITLE_FONT = new Font(bf, 20f, Font.NORMAL);
	public final static Font NOTE_DATE_FONT = new Font(bf, 6f, Font.BOLDITALIC);
	public final static Font NOTE_CONTENT_FONT = new Font(bf, 12f, Font.NORMAL);
	public final static Font NOTE_FOOTER_FONT = new Font(bf, 6f, Font.ITALIC);
}