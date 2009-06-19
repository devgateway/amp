package org.digijava.module.aim.services.auditcleaner;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * @author Diego Dimunzio
 * 
 */
public class AuditCleanerJob implements Job {
	private static Logger logger = Logger.getLogger(AuditCleanerJob.class);
	/**
	 * 
	 */
	public void execute(JobExecutionContext contex)
			throws JobExecutionException {

		String deletetime = FeaturesUtil
				.getGlobalSettingValue("Automatic Audit Logger Cleanup");
		logger.info("Starting to delete audit logs older than " + deletetime
				+ " days.............................");
		try {

			AuditLoggerUtil.DeleteLogsByPeriod(deletetime);
			logger
					.info("Delete Audit logs finished...................................................");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
