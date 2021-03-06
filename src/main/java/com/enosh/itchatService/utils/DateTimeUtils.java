package com.enosh.itchatService.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {
	
	public static final String DATE_TIME_MASK = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_MASK_WITH_MINUS = "yyyy-MM-dd";
	public static final String DATE_MASK = "yyyyMMdd";
	public static final String YEAR_MONTH_MASK = "yyyyMM";
	public static final String YEAR_MASK = "yyyy";
	
	public static Date toDate(String text) {
		SimpleDateFormat format = new SimpleDateFormat(DATE_MASK_WITH_MINUS);
		try {
			return format.parse(text);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date toDate(String text, String mask) {
		SimpleDateFormat format = new SimpleDateFormat(mask);
		try {
			return format.parse(text);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String toStr(Date date) {
		if(date == null) return "";
		SimpleDateFormat format = new SimpleDateFormat(DATE_MASK_WITH_MINUS);
		return format.format(date);
	}
	
	public static String toStr(Date date, String mask) {
		if(date == null) return "";
		SimpleDateFormat format = new SimpleDateFormat(mask);
		return format.format(date);
	}
	
	public static Date getLastMonthTheDate() {
		
		Calendar cal = Calendar.getInstance();
    	int thisMonth = cal.get(Calendar.MONTH);
    	cal.set(Calendar.MONTH, thisMonth-1);
    	
    	return cal.getTime();
	}
	
	public static void main(String args[]) {
		System.out.println(toStr(new Date(0)));
	}
}
