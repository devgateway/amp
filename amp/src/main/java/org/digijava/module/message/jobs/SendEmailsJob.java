package org.digijava.module.message.jobs;

import org.apache.log4j.Logger;
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

import javax.mail.internet.InternetAddress;
import java.util.List;

public class SendEmailsJob extends ConnectionCleaningJob implements StatefulJob { 
    
    private static Logger logger = Logger.getLogger(SendEmailsJob.class);
    
    @Override 
    public void executeInternal(JobExecutionContext context) throws JobExecutionException{      
        List<Long> receiversIds = AmpMessageUtil.loadReceiversIdsToGetEmails();
        for (Long rec : receiversIds) {
            try {
                sendEmailToReceiver(rec);
            }
            catch (Exception e) {
                logger.error("cound not send an email to receiver " + rec, e);
            }
        }
        logger.info("Finished changing messages status in database");
    }

    /**
     * sends the email configured by the AmpEmailReceiver instance with an id of receiverId
     * @param receiverId
     * @throws Exception
     */
    protected void sendEmailToReceiver(long receiverId) throws Exception {
        Session session = PersistenceManager.getSession();
        AmpEmailReceiver receiver = AmpMessageUtil.getAmpEmailReceiver(receiverId);
        InternetAddress[] ito = new InternetAddress[] { new InternetAddress(receiver.getAddress())};
        AmpEmail email = receiver.getEmail();
        boolean asHtml = true;
        boolean log = true;
        boolean rtl = false;
        logger.info("Enter DG Email manager for email");
        DgEmailManager.sendMail(ito, email.getSender(), null, null, email.getSubject(), email.getBody(), "UTF8", asHtml, log, rtl);
        logger.info("Email sent");
        //update receiver status state to sent
        receiver.setStatus(MessageConstants.SENT_STATUS);
        session.update(receiver);
    }
}
