package com.enosh.itchatService.dispatcher;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.enosh.itchatService.model.User;

public class ThreadLocalUtils {
	private final static String CURRENT_USER = "currentUser";
	private final static String SEND_DATE = "sendDate";
	private final static String MAIL_CONTENT = "mailContent";
	
	private static ThreadLocal<Map<String, Object>> localMap = new ThreadLocal<Map<String, Object>>();
	
	public static void init(User user, Date sendDate, String mailContent) {
		Map<String, Object> map = getMap();
		map.put(CURRENT_USER, user);
		map.put(SEND_DATE, sendDate);
		map.put(MAIL_CONTENT, mailContent);
	}
	public static User getCurrentUser() {
		Map<String, Object> map = getMap();
		return (User)map.get(CURRENT_USER);
	}
	public static Date getSendDate() {
		Map<String, Object> map = getMap();
		return (Date)map.get(SEND_DATE);
	}
	public static String getMailContent() {
		Map<String, Object> map = getMap();
		return (String)map.get(MAIL_CONTENT);
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
	