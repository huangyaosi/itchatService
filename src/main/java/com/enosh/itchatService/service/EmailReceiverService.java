package com.enosh.itchatService.service;

import java.io.IOException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
	@Autowired ShareNoteService shareNoteService;
	@Autowired UserService userService;
	
	@Value("${mail.receiver.protocol}")
	private String protocol;
	
	@Value("${mail.receiver.host}")
	private String host;
	
	@Value("${mail.receiver.port}")
	private String port;
	
	@Value("${mail.receiver.username}")
	private String userName;
	
	@Value("${mail.receiver.password}")
	private String password;
	
	@Value("${mail.receiver.keyword}")
	private String keyword;
	
	@Value("${admin.email}")
	private String adminEmail;
	
	public static final Pattern EMAIL_PATTERN_1 = Pattern.compile("\\<(.*@.*)\\>");
	
	@Scheduled(cron = "0 0 */1 * * *")
//	@Scheduled(cron = "*/40 * * * * *")
	public void seeNewEmails() {
		System.out.println("start search new email..");
		Properties properties = new Properties();
		properties.put(String.format("mail.%s.host", getProtocol()), getHost());
		properties.put(String.format("mail.%s.port", getProtocol()), getPort());

		Session session = Session.getDefaultInstance(properties);

		try {
			// connects to the message store
			Store store = session.getStore(protocol);
			store.connect(userName, password);

			// opens the inbox folder
			Folder folderInbox = store.getFolder("INBOX");
			folderInbox.open(Folder.READ_WRITE);

			// fetches new messages from server
			// Message[] messages = folderInbox.getMessages();
			Message[] messages = folderInbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
			for (int i = 0; i < messages.length; i++) {
				Message msg = messages[i];
				String subject = msg.getSubject();
				Address[] fromAddress = msg.getFrom();
				String from = getFromUserName(fromAddress[0].toString());
				String name = "";
				Date sentDate = msg.getSentDate() == null ? new Date() : msg.getSentDate();
				String messageContent = "";
				if(!StringUtils.isEmpty(subject) && subject.startsWith(getKeyword())) {
					String splitStr = "";
					if(subject.indexOf("：") > -1){
						splitStr = "：";
					} else {
						splitStr = ":";
					}
					String[] arr = subject.split(splitStr);
					if (arr.length >= 2) {
						name = arr[1];
						if (arr.length == 3) {
							sentDate = DateTimeUtils.toDate((arr[2]), DateTimeUtils.DATE_MASK);
						}
					}
				}
				
				User user = null;
				
				if(adminEmail.trim().equals(from.trim())) {
					if(!StringUtils.isEmpty(name)) {
						user = userService.findOrCreateUserByName(name);
					} else {
						user = userService.findByEmailOrName(from, name);
					}
				} else {
					user = userService.findByEmailOrName(from, name);
				}
				
				if(user == null) continue;
				
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

				// print out details of each message
				System.out.println("Message #" + (i + 1) + ":");
				System.out.println("\t From: " + from);
				System.out.println("\t Sent Date: " + DateTimeUtils.toStr(sentDate));
				System.out.println("\t Message: " + messageContent);
				shareNoteService.createShareNoteFromMail(messageContent, sentDate, user);

			}
//			folderInbox.setFlags(messages, new Flags(Flags.Flag.SEEN), true);
			// disconnect
			folderInbox.close(false);
			store.close();
		} catch (NoSuchProviderException ex) {
			System.out.println("No provider for protocol: " + protocol);
			ex.printStackTrace();
		} catch (MessagingException ex) {
			System.out.println("Could not connect to the message store");
			ex.printStackTrace();
		}
	}

	private String getFromUserName(String from) {
		Matcher matchter = EMAIL_PATTERN_1.matcher(from);
		if (matchter.find()) {
			return matchter.group(1);
		}
		return from;

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

	public String getProtocol() {
		return protocol;
	}
	
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getPort() {
		return port;
	}
	
	public void setPort(String port) {
		this.port = port;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
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
        String str = "灵修：高雄";
        System.out.println(str.indexOf("："));
        String arr[] = str.split("：");
        for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}
   }
}