package org.digijava.module.aim.services.auditcleaner;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
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
	
	@Override public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			executeInternal(context);
		}
		finally {
			PersistenceManager.endSessionLifecycle();
		}
	}
	
	/**
	 * 
	 */
	public void executeInternal(JobExecutionContext contex) throws JobExecutionException {
		String deletetime = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AUTOMATIC_AUDIT_LOGGER_CLEANUP);
		logger.info("Starting to delete audit logs older than " + deletetime + " days.............................");
		AuditLoggerUtil.deleteLogsByPeriod(deletetime);
		logger.info("Delete Audit logs finished...................................................");
	}

}
