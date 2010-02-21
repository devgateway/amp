package org.digijava.module.message.jobs;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class ActivityVersionDeletionJob implements StatefulJob {

	private static Logger logger = Logger.getLogger(ActivityVersionDeletionJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			logger.warn("ActivityVersionDeletionJob started.");
			ActivityVersionUtil.deleteOldActivityVersions();
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
}