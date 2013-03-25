package org.digijava.module.message.jobs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.internet.InternetAddress;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.admin.util.hibernate.SeparateSessionManager;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.message.dbentity.AmpEmail;
import org.digijava.module.message.dbentity.AmpEmailReceiver;
import org.digijava.module.message.helper.MessageConstants;
import org.digijava.module.message.util.AmpMessageUtil;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class SendEmailsJob implements StatefulJob{
	private static Logger logger = Logger.getLogger(SendEmailsJob.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		List<Long> receiversIds=AmpMessageUtil.loadReceiversIdsToGetEmails();
		SeparateSessionManager sessionManager	= null;
		try {
			sessionManager	= new SeparateSessionManager();
			sessionManager.openSession(); 
					
			if(receiversIds!=null && receiversIds.size()>0){
				AmpEmail email=null;
				for (Long rec : receiversIds) {				
					InternetAddress[] ito;
					try {
						AmpEmailReceiver receiver=AmpMessageUtil.getAmpEmailReceiverUsingSession(rec, sessionManager.getSession());
						ito = new InternetAddress[] {new InternetAddress(receiver.getAddress())};					
						//email=AmpMessageUtil.getAmpEmail(rec.getEmail().getId()) ;
						email=receiver.getEmail();
			            boolean asHtml = true;
			            boolean log = true;
			            boolean rtl = false;
			            logger.info("Enter DG Email manager for email" );
			            DgEmailManager.sendMail(ito, email.getSender(), null, null, email.getSubject(), email.getBody(), "UTF8", asHtml, log, rtl);
			            logger.info("Finished sending emails");
			            //update receiver status state to sent
			            receiver.setStatus(MessageConstants.SENT_STATUS);
			            sessionManager.getSession().update(receiver);
			            logger.info("Finished changing messages status in database");
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}			
			}
		
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		finally {
			if (sessionManager != null)
				sessionManager.closeSession();
		}
	}

}
