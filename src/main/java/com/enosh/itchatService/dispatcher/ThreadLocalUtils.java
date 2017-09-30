package com.enosh.itchatService.dispatcher;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.enosh.itchatService.common.MailMessage;
import com.enosh.itchatService.model.User;

public class ThreadLocalUtils {
	private final static String CURRENT_USER = "currentUser";
	private final static String SEND_DATE = "sendDate";
	private final static String MAIL_CONTENT = "mailContent";
	private final static String MAIL_MESSAGE = "message";
	
	private static ThreadLocal<Map<String, Object>> localMap = new ThreadLocal<Map<String, Object>>();
	
	public static void init(User user, Date sendDate, String mailContent) {
		Map<String, Object> map = getMap();
		map.put(CURRENT_USER, user);
		map.put(SEND_DATE, sendDate);
		map.put(MAIL_CONTENT, mailContent);
	}
	
	public static void init(User user, Date sendDate, MailMessage msg) {
		Map<String, Object> map = getMap();
		map.put(CURRENT_USER, user);
		map.put(SEND_DATE, sendDate);
		map.put(MAIL_MESSAGE, msg);
	}
	
	public static User getCurrentUser() {
		Map<String, Object> map = getMap();
		return (User)map.get(CURRENT_USER);
	}
	
	public static void setCurrentUser(User user) {
		Map<String, Object> map = getMap();
		map.put(CURRENT_USER, user);
	}
	
	public static Date getMsgSendDate() {
		MailMessage msg = getMessage();
		return msg.getSendDate();
	}
	
	public static MailMessage getMessage() {
		Map<String, Object> map = getMap();
		return (MailMessage)map.get(MAIL_MESSAGE);
	}
	
	public static void setMessage(MailMessage msg) {
		Map<String, Object> map = getMap();
		map.put(MAIL_MESSAGE, msg);
	}
	
	public static String getMailContent() {
		MailMessage msg = getMessage();
		return msg != null ? msg.getContent() : null;
	}
	
	public static Map<String, Object> getMap() {
		Map<String, Object> map = (Map<String, Object>)localMap.get();
		if(map == null) {
			map = new HashMap<String, Object>();
			localMap.set(map);
		}
		return map;
	}
}
	