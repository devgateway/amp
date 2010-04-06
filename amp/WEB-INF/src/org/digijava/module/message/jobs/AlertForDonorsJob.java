

package org.digijava.module.message.jobs;

import org.apache.log4j.Logger;
import org.digijava.module.message.triggers.AlertForDonorsTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;


public class AlertForDonorsJob implements StatefulJob {

	private static Logger logger = Logger.getLogger(AlertForDonorsJob.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			 new AlertForDonorsTrigger();
		} catch (Exception e) {
			logger.error("error", e);
		}
	}
}
