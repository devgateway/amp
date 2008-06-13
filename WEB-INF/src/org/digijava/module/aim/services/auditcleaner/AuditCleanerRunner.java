package org.digijava.module.aim.services.auditcleaner;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 
 * @author Diego Dimunzio
 * 
 */
public class AuditCleanerRunner {

	private SchedulerFactory schedulerFactory;
	private Scheduler scheduler;
	private JobDetail jobDetail;
	private SimpleTrigger simpleTrigger;
	private final String jobName = "DeleteAuditLoggs";
	private final String jobGroupName = "ampServices";
	private final String trgName = "TimeToDeleteLoggs";
	private final String trgGroupName = "triggerGroup-s1";
	private Class classJob = AuditCleanerJob.class;
	private final static long TEAN_DAYS = 1000 * 60 * 60 * 24 * 10;
	private static Logger logger = Logger.getLogger(AuditCleanerRunner.class);

	public void stop() {
		try {
			scheduler.deleteJob(jobName, jobGroupName);
			logger.info("Audit Loggs Cleaner Stoped.....................");
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void launch() {
		Long ctime = System.currentTimeMillis();

		try {
			schedulerFactory = new StdSchedulerFactory();
			scheduler = schedulerFactory.getScheduler();
			if (scheduler.getJobDetail(jobName, jobGroupName) == null) {
				jobDetail = new JobDetail(jobName, jobGroupName, classJob);
				simpleTrigger = new SimpleTrigger(trgName, trgGroupName);
				simpleTrigger.setStartTime(new Date(TEAN_DAYS));
				simpleTrigger.setRepeatInterval(TEAN_DAYS);
				simpleTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
				scheduler.scheduleJob(jobDetail, simpleTrigger);
				logger.info("Audit Loggs Cleaner Started");
			} else {
				logger
						.info("Audit Loggs Cleaner is Allredy Configured.....................");
			}

		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
