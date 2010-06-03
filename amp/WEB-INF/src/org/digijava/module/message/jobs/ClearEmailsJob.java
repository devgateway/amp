package org.digijava.module.message.jobs;

import java.util.List;

import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.message.dbentity.AmpEmail;
import org.digijava.module.message.util.AmpMessageUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 * This Job is used to delete emails  from AmpEmails table, 
 * that were delivered to all recipients   
 * @author Dare
 *
 */
public class ClearEmailsJob implements StatefulJob{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		/**
		 * SELECT a1.email_Id FROM amp_email_receiver a1 join amp_email_receiver a2 on a1.email_id=a2.email_id 
		 * where a1.status="failed"  group by a1.email_id 
		 */
		List <AmpEmail> emailsForRemoval=AmpMessageUtil.loadSentEmails();
		if(emailsForRemoval!=null && emailsForRemoval.size()>0){
			for (AmpEmail ampEmail : emailsForRemoval) {
				DbUtil.delete(ampEmail);
			}
		}
		
	}

}
