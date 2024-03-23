package org.digijava.module.message.jobs;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.message.dbentity.AmpEmail;
import org.digijava.module.message.util.AmpMessageUtil;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.util.List;

/**
 * This Job is used to delete emails  from AmpEmails table, 
 * that were delivered to all recipients   
 * @author Dare
 *
 */
public class ClearEmailsJob extends ConnectionCleaningJob implements StatefulJob { 
    
    @Override 
    public void executeInternal(JobExecutionContext context) throws JobExecutionException{      
        Session session = PersistenceManager.getSession();
        List<AmpEmail> emailsForRemoval = AmpMessageUtil.loadSentEmails(session);
        if (emailsForRemoval != null && emailsForRemoval.size() > 0) {
            for (AmpEmail ampEmail : emailsForRemoval) {
                session.delete(ampEmail);
            }
        }
    }

}
