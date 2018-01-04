package com.enosh.itchatService.common;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;

import com.enosh.itchatService.utils.Strings;

public class MailMessage {
	
	public static final Pattern EMAIL_PATTERN_1 = Pattern.compile("\\<(.*@.*)\\>");
	public static final Pattern SUBJECT_PATTERN_1 = Pattern.compile("(.*)[:|：](.*)[:|：](.*)[:|：](.*)");
	public static final Pattern SUBJECT_PATTERN_2 = Pattern.compile("(.*)[:|：](.*)[:|：](.*)");
	public static final Pattern SUBJECT_PATTERN_3 = Pattern.compile("(.*)[:|：](.*)");
	
	private Message msg;
	private String methodKey;
	private Object[] args = null;
	private boolean read;
	
	public MailMessage(Message msg) {
		this.msg = msg;
		try {
			String subject = msg.getSubject();
			if(!Strings.isEmpty(subject)){
				Matcher matcher = SUBJECT_PATTERN_1.matcher(subject);
				if(matcher.find()) {
					args = new Object[3];
					methodKey = matcher.group(1);
					args[0] = matcher.group(2);
					args[1] = matcher.group(3);
					args[2] = matcher.group(4);
				} else if((matcher = SUBJECT_PATTERN_2.matcher(subject)).find()) {
					args = new Object[2];
					methodKey = matcher.group(1);
					args[0] = matcher.group(2);
					args[1] = matcher.group(3);
				} else if((matcher = SUBJECT_PATTERN_3.matcher(subject)).find()) {
					args = new Object[1];
					methodKey = matcher.group(1);
					args[0] = matcher.group(2);
				} else if(subject.length() <= 6){
					methodKey = subject;
				}
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}
	public String getFromUserName() {
		String from = null;
		try {
			Address[] fromAddress = msg.getFrom();
			from = fromAddress[0].toString();
			Matcher matchter = EMAIL_PATTERN_1.matcher(from);
			if (matchter.find()) {
				return matchter.group(1);
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return from;

	}
	
	public String getContent(){
		String messageContent = "";
		try {
			if (msg.isMimeType("text/plain") || msg.isMimeType("text/html")) {
				try {
					Object content = msg.getContent();
					if (content != null) {
						messageContent = content.toString();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (msg.isMimeType("multipart/*")) {
				try {
					MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();
					messageContent = getTextFromMimeMultipart(mimeMultipart);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		setRead(true);
		return Strings.toText(messageContent);
	}
	
	private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
		String result = "";
		int count = mimeMultipart.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/plain") || bodyPart.isMimeType("text/html")) {
				result = (String) bodyPart.getContent();
				if (!Strings.isEmpty(result))
					break;
			} else if (bodyPart.getContent() instanceof MimeMultipart) {
				result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
			}
		}
		return result;
	}
	
	public Date getSendDate() {
		Date date = null;
		try {
			date = msg.getSentDate();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		if(date == null) date = new Date();
		return date;
	}
	public Message getMsg() {
		return msg;
	}
	public void setMsg(Message msg) {
		this.msg = msg;
	}
	public String getMethodKey() {
		return methodKey;
	}
	public void setMethodKey(String methodKey) {
		this.methodKey = methodKey;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
}
