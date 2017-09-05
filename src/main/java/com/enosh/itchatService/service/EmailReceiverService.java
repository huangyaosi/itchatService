package com.enosh.itchatService.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.enosh.itchatService.config.MailReceiverConfig;
import com.enosh.itchatService.dispatcher.KeyToMethodDispatch;
import com.enosh.itchatService.dispatcher.ThreadLocalUtils;
import com.enosh.itchatService.model.User;
import com.enosh.itchatService.utils.DateTimeUtils;
import com.enosh.itchatService.utils.StringUtils;

/**
* This program just for get e-mail messages from a POP3/IMAP server
*
* @author Enosh
*
*/
@Service
public class EmailReceiverService {
	@Autowired UserService userService;
	@Autowired MailReceiverConfig mailReceiverConfig;
	@Autowired KeyToMethodDispatch dispacher;
	
	public static final Pattern EMAIL_PATTERN_1 = Pattern.compile("\\<(.*@.*)\\>");
	public static final Pattern SUBJECT_PATTERN_1 = Pattern.compile("(.*)[:|：](.*)[:|：](.*)");
	public static final Pattern SUBJECT_PATTERN_2 = Pattern.compile("(.*)[:|：](.*)");
	
	private Comparator dateComparator;
	
	@Scheduled(cron = "0 */30 * * * *")
//	@Scheduled(cron = "*/40 * * * * *")
	public void pollNewEmails() {
		System.out.println(DateTimeUtils.toStr(new Date(), DateTimeUtils.DATE_TIME_MASK) + "-------start search new email...");
		Properties properties = new Properties();
		properties.put(String.format("mail.%s.host", mailReceiverConfig.getProtocol()), mailReceiverConfig.getHost());
		properties.put(String.format("mail.%s.port", mailReceiverConfig.getProtocol()), mailReceiverConfig.getPort());

		Session session = Session.getDefaultInstance(properties);

		try {
			// connects to the message store
			Store store = session.getStore(mailReceiverConfig.getProtocol());
			store.connect(mailReceiverConfig.getUsername(), mailReceiverConfig.getPassword());

			// opens the inbox folder
			Folder folderInbox = store.getFolder("INBOX");
			folderInbox.open(Folder.READ_WRITE);

			// fetches new messages from server
			// Message[] messages = folderInbox.getMessages();
			Message[] messages = folderInbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
			
			Arrays.sort(messages, getdateComparator());
			for (int i = 0; i < messages.length; i++) {
				Message msg = messages[i];
				String subject = msg.getSubject();
				Address[] fromAddress = msg.getFrom();
				String from = getFromUserName(fromAddress[0].toString());
				User user = userService.findByEmail(from);
				if(user == null) continue;
				
				ThreadLocalUtils.init(user, msg.getSentDate(), getContent(msg));
				
				String key = "";
				Object[] args = null;
				if(!StringUtils.isEmpty(subject)){
					Matcher matcher = SUBJECT_PATTERN_1.matcher(subject);
					if(matcher.find()) {
						args = new Object[2];
						key = matcher.group(1);
						args[0] = matcher.group(2);
						args[1] = matcher.group(3);
					} else if((matcher = SUBJECT_PATTERN_2.matcher(subject)).find()) {
						args = new Object[1];
						key = matcher.group(1);
						args[0] = matcher.group(2);
					} else if(subject.length() <= 6){
						key = subject;
					}
				}
				dispacher.dispatchRequest(key, args);

			}
//			folderInbox.setFlags(messages, new Flags(Flags.Flag.SEEN), true);
			// disconnect
			folderInbox.close(false);
			store.close();
		} catch (NoSuchProviderException ex) {
			System.out.println("No provider for protocol: " + mailReceiverConfig.getProtocol());
			ex.printStackTrace();
		} catch (MessagingException ex) {
			System.out.println("Could not connect to the message store");
			ex.printStackTrace();
		}
	}

	private Comparator getdateComparator() {
		if(dateComparator == null){
			dateComparator = new Comparator<Message>() {
				public int compare(Message o1, Message o2) {
					try {
						Date date1= o1.getSentDate();
						Date date2= o2.getSentDate();
						if(date1 != null && date2 != null) {
							if(date1.before(date2)) return -1;
						}
						
					} catch (MessagingException e) {
						e.printStackTrace();
					}
					return 1;
				}
			};
		}
		return dateComparator;
	}
	
	private String getFromUserName(String from) {
		Matcher matchter = EMAIL_PATTERN_1.matcher(from);
		if (matchter.find()) {
			return matchter.group(1);
		}
		return from;

	}
   
	private String getContent(Message msg){
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
		return messageContent;
	}
	
	private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
		String result = "";
		int count = mimeMultipart.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/plain")) {
				result = (String) bodyPart.getContent();
				if (!StringUtils.isEmpty(result))
					break;
			} else if (bodyPart.getContent() instanceof MimeMultipart) {
				result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
			}
		}
		return result;
	}

	public static void main(String[] args) {
       // for POP3
       //String protocol = "pop3";
       //String host = "pop.gmail.com";
       //String port = "995";

       // for IMAP
//		EmailReceiverService receiver = new EmailReceiverService();
//		receiver.setProtocol("imap");
//		receiver.setHost("imap.sina.com");
//		receiver.setPort("143");
//		
//		receiver.setUserName("huangcunwei0701@sina.com");
//		receiver.setPassword("ai");
		
		
//		Properties properties = new Properties();
//		properties.put(String.format("mail.%s.host", "imap"), "imap.sina.com");
//		properties.put(String.format("mail.%s.port", "imap"), 143);
//
//		Session session = Session.getDefaultInstance(properties);
//		Store store;
//		try {
//			store = session.getStore("imap");
//			store.connect("huangcunwei0701@sina.com", "");
//			Folder folderInbox = store.getFolder("INBOX");
//			folderInbox.open(Folder.READ_WRITE);
//			Message[] messages = folderInbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
//			for (Message message : messages) {
//				Date sendDate = message.getSentDate();
//				String subject = message.getSubject();
//				System.out.print("subject1 : " + subject);
//				System.out.println("sendDate1 : " + DateTimeUtils.toStr(sendDate, DateTimeUtils.DATE_TIME_MASK));
//			}
//			Arrays.sort(messages, new Comparator<Message>() {
//				public int compare(Message o1, Message o2) {
//					try {
//						Date date1= o1.getSentDate();
//						Date date2= o2.getSentDate();
//						if(date1 != null && date2 != null) {
//							if(date1.before(date2)) return -1;
//						}
//						
//					} catch (MessagingException e) {
//						e.printStackTrace();
//					}
//					return 1;
//				}
//			});
//			for (Message message : messages) {
//				Date sendDate = message.getSentDate();
//				String subject = message.getSubject();
//				System.out.print("subject2 : " + subject);
//				System.out.println("sendDate2: " + DateTimeUtils.toStr(sendDate, DateTimeUtils.DATE_TIME_MASK));
//			}
//		} catch (NoSuchProviderException e1) {
//			e1.printStackTrace();
//		} catch (MessagingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

   }
}