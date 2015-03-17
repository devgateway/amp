package org.digijava.module.message.jobs;

import java.util.List;

import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.persistence.PersistenceManager;
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
        Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();
		} catch (DgException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
            List<Long> receiversIds = AmpMessageUtil.loadReceiversIdsToGetEmails(session);
            if (receiversIds != null && receiversIds.size() > 0) {
                AmpEmail email = null;
                for (Long rec : receiversIds) {
                    InternetAddress[] ito;
                    try {
                        AmpEmailReceiver receiver = AmpMessageUtil.getAmpEmailReceiverUsingSession(rec, session);
                        ito = new InternetAddress[]{new InternetAddress(receiver.getAddress())};
                        //email=AmpMessageUtil.getAmpEmail(rec.getEmail().getId()) ;
                        email = receiver.getEmail();
                        boolean asHtml = true;
                        boolean log = true;
                        boolean rtl = false;
                        logger.info("Enter DG Email manager for email");
                        DgEmailManager.sendMail(ito, email.getSender(), null, null, email.getSubject(), email.getBody(), "UTF8", asHtml, log, rtl);
                        logger.info("Finished sending emails");
                        //update receiver status state to sent
                        receiver.setStatus(MessageConstants.SENT_STATUS);
                        session.update(receiver);
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
            }
            logger.info("Finished changing messages status in database");
        } catch (Exception e1) {
            logger.error(e1);
        } finally {
        	PersistenceManager.cleanupSession(session);
        }
    }

}
