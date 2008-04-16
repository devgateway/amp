package org.digijava.module.message.helper;

import java.util.Calendar;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.event.*;

import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.message.dbentity.SystemMessage;
import org.digijava.module.message.util.AmpMessageUtil;

public class AmpMessageWorker {
	public static void createAmpMessageForActivityCreation (Long ampActivityId,Long teamId) throws AimException,Exception {
		Long teamLeaderId=TeamMemberUtil.getTeamHead(teamId).getAmpTeamMemId();
		SystemMessage message=new SystemMessage();
		message.setName("Created New Activity");
		message.setDescription("Created New Activity with Id= "+ampActivityId);		
		message.setReceiverType("TM"); //team Member
		message.setReceiverId(teamLeaderId);
		message.setSelectedTeamId(teamId); //So the member(if he is in 2 or more teams) will see a message only for current team 
		message.setSenderType(MessageConstants.SENDER_TYPE_ACTIVITY); 
		message.setSenderId(ampActivityId);
		message.setPriorityLevel(MessageConstants.PRIORITY_LEVEL_NORMAL); // by default priority level of such system message is Normal
		message.setRead(false);
		Calendar cal=Calendar.getInstance();
		message.setCreationDate(cal.getTime());
		AmpMessageUtil.saveOrUpdateMessage(message);
		sendMail();
	}
	
	/* Bean Properties */
	public static Properties props = null;
	public static Session session = null;

//	static {
//		/*	Setting Properties for STMP host */
//		props = System.getProperties();
//		props.put("mail.smtp.host", "mail.yourisp.com");
//		session = Session.getDefaultInstance(props, null);
//	}

	public static void sendMail() throws Exception {
		props = System.getProperties();
		props.put("mail.smtp.host", "mail.yahoo.com");
		session = Session.getDefaultInstance(props, null);
		try {				
			MimeMessage message = new MimeMessage(session);
			message.setRecipient(Message.RecipientType.TO, 
				new InternetAddress("dareroinishvili@gmail.com"));
			message.setFrom(new InternetAddress("skvisha@yahoo.com"));
			message.setSubject("hola amigo");
			message.setText("hello dear I");
			Transport.send(message);
		} catch (MessagingException e) {
			throw new Exception(e.getMessage());
		}
	}	
}
