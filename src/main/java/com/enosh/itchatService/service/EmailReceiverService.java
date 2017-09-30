package com.enosh.itchatService.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.enosh.itchatService.common.MailMessage;
import com.enosh.itchatService.common.MessageHander;
import com.enosh.itchatService.common.concurrency.ActiveQueue;
import com.enosh.itchatService.config.MailReceiverConfig;
import com.enosh.itchatService.dispatcher.KeyToMethodDispatch;
import com.enosh.itchatService.utils.DateTimeUtils;

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
//	@Autowired ActiveQueue messageQueue;
	@Autowired MessageHander messageHander;
	
	public static final Pattern EMAIL_PATTERN_1 = Pattern.compile("\\<(.*@.*)\\>");
	public static final Pattern SUBJECT_PATTERN_1 = Pattern.compile("(.*)[:|：](.*)[:|：](.*)[:|：](.*)");
	public static final Pattern SUBJECT_PATTERN_2 = Pattern.compile("(.*)[:|：](.*)[:|：](.*)");
	public static final Pattern SUBJECT_PATTERN_3 = Pattern.compile("(.*)[:|：](.*)");
	
	private Comparator dateComparator;
	
	@Scheduled(cron = "0 */20 * * * *")
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
			messageHander.process(messages);
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
	
	public static ActiveQueue.Processor createMessageProcesser() {
		return new ActiveQueue.Processor() {
			@Override
			public void process(Object obj) {
				
			}
		};
	}
	
	public static void main(String[] args) {

   }
}