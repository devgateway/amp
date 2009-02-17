package org.dgfoundation.amp.error.quartz;

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
 * @author Arty
 * 
 */
public class ECSRunner {

	private SchedulerFactory schedulerFactory;
	private Scheduler scheduler;
	private JobDetail jobDetail;
	private SimpleTrigger simpleTrigger;
	private final String jobName = "SyncErrorsWithECS";
	private final String jobGroupName = "ampServices";
	private final String trgName = "TimeToSync";
	private final String jobDaily = "SyncCheckTime"; 
	private final String trgDaily = "SyncECSTime";
	private final String trgGroupName = "triggerGroup-s1";
	private Class classJob = ECSJob.class;
	private final static long ONE_DAY = 1000 * 60 * 60 * 24;
	private final static long TEAN_DAYS = 1000 * 60 * 60 * 24 * 10;
	private final static long FIVE_MIN = 1000 * 60 * 5;

	private static Logger logger = Logger.getLogger(ECSRunner.class);
	public final  static long DELAY_MIN_TIME = FIVE_MIN;
	public final  static long DELAY_MAX_TIME = ONE_DAY; // servers should report at least once a day
	public static long DELAY_TIME = FIVE_MIN;

	private Long ctime = System.currentTimeMillis();

	public void stop() {
		try {
			schedulerFactory = new StdSchedulerFactory();
			scheduler = schedulerFactory.getScheduler();
			scheduler.pauseTrigger(trgName, trgGroupName);
			//scheduler.pauseTrigger("CheckDeleteTime", trgGroupName);
			logger.info("Sync ECS Stoped.....................");
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void launch() {


		try {
			schedulerFactory = new StdSchedulerFactory();
			scheduler = schedulerFactory.getScheduler();
			//Check if the main job (DeleteAuditLoggs)is stored add a new job if it's not there
			if (scheduler.getJobDetail(jobName, jobGroupName) == null) {
				jobDetail = new JobDetail(jobName, jobGroupName, classJob);
				simpleTrigger = new SimpleTrigger(trgName, trgGroupName);
				simpleTrigger.setStartTime(new Date(ctime + DELAY_TIME));
				simpleTrigger.setRepeatInterval(DELAY_TIME);
				
				simpleTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
				scheduler.scheduleJob(jobDetail, simpleTrigger);
				//startSyncJob();
				
				if (scheduler.getTrigger(trgName, trgGroupName).getNextFireTime() != null) {
					ECS.getInstance().setNextcleanup(new Date(scheduler.getTrigger(trgName, trgGroupName).getNextFireTime().getTime()));
				} else {
					ECS.getInstance().setLastcleanup(new Date(0L));
				}
				if (scheduler.getTrigger(trgName, trgGroupName).getPreviousFireTime() != null) {
					ECS.getInstance().setLastcleanup(new Date(scheduler.getTrigger(trgName, trgGroupName).getPreviousFireTime().getTime()));
				} else {
					ECS.getInstance().setLastcleanup(new Date(0L));
				}
				
				logger.info("Error SyncECS Started Next CleanUp: "
						+ ECS.getInstance().getNextcleanup()
								.toString()
						+ " *** Last Sync: "
						+ ECS.getInstance().getLastcleanup()
								.toString());

			} else {
				//Check if the main trigger (TimeToDeleteLoggs) is stored
				if (scheduler.getTrigger(trgName, trgGroupName)!=null){
					if (scheduler.getTrigger(trgName, trgGroupName).getNextFireTime() != null) {
						ECS.getInstance().setNextcleanup(new Date(scheduler.getTrigger(trgName, trgGroupName).getNextFireTime().getTime()));
					} else {
						ECS.getInstance().setLastcleanup(new Date(0L));
					}

					scheduler.resumeTrigger(trgName, trgGroupName);
					if (scheduler.getJobDetail(jobDaily, jobGroupName) == null) {
						//startSyncJob();
					} else {
						scheduler.resumeTrigger(trgDaily, trgGroupName);
					}

					logger.info("Error SyncECS is Allredy Configured Next CleanUp: "
								+ ECS.getInstance().getNextcleanup()
										.toString());
				}else{
					scheduler.deleteJob(jobName, jobGroupName);
					jobDetail = new JobDetail(jobName, jobGroupName, classJob);
					simpleTrigger = new SimpleTrigger(trgName, trgGroupName);
					simpleTrigger.setStartTime(new Date(ctime + DELAY_TIME));
					simpleTrigger.setRepeatInterval(DELAY_TIME);
					simpleTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
					scheduler.scheduleJob(jobDetail, simpleTrigger);
					if (scheduler.getJobDetail(jobDaily, jobGroupName) == null){
						//startSyncJob();
					}
					if (scheduler.getTrigger(trgName, trgGroupName).getNextFireTime() != null) {
						ECS.getInstance().setNextcleanup(new Date(scheduler.getTrigger(trgName, trgGroupName).getNextFireTime().getTime()));
					} else {
						ECS.getInstance().setLastcleanup(new Date(0L));
					}
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	/*
	public void startSyncJob() {
		try {
			jobDetail = new JobDetail(jobDaily, jobGroupName,
					ECSMsgJob.class);
			simpleTrigger = new SimpleTrigger(trgDaily, trgGroupName);
			simpleTrigger.setStartTime(new Date(ctime + FIVE_MIN));
			simpleTrigger.setRepeatInterval(ONE_DAY);
			simpleTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
			scheduler.scheduleJob(jobDetail, simpleTrigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	*/
}
