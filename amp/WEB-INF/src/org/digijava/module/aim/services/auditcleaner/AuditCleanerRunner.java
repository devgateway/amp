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
    private final String jobDaily = "AuditCheckTime";
    private final String trgDaily = "CheckDeleteTime";
    private final String trgGroupName = "triggerGroup-s1";
    private Class classJob = AuditCleanerJob.class;
    private final static long ONE_DAY = 1000 * 60 * 60 * 24;
    private final static long TEAN_DAYS = 1000 * 60 * 60 * 24 * 10;
    private final static long FIVE_MIN = 1000 * 60 * 5;

    private static Logger logger = Logger.getLogger(AuditCleanerRunner.class);

    private Long ctime = System.currentTimeMillis();

    public void stop() {
        try {
            scheduler.pauseTrigger(trgName, trgGroupName);
            scheduler.pauseTrigger("CheckDeleteTime", trgGroupName);
            logger.info("Audit Loggs Cleaner Stoped.....................");
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
                simpleTrigger.setStartTime(new Date(ctime + TEAN_DAYS));
                simpleTrigger.setRepeatInterval(TEAN_DAYS);
                simpleTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
                scheduler.scheduleJob(jobDetail, simpleTrigger);
                startMessageJob();

                if (scheduler.getTrigger(trgName, trgGroupName).getNextFireTime() != null) {
                    AuditCleaner.getInstance().setNextcleanup(new Date(scheduler.getTrigger(trgName, trgGroupName).getNextFireTime().getTime()));
                } else {
                    AuditCleaner.getInstance().setLastcleanup(new Date(0L));
                }
                if (scheduler.getTrigger(trgName, trgGroupName).getPreviousFireTime() != null) {
                    AuditCleaner.getInstance().setLastcleanup(new Date(scheduler.getTrigger(trgName, trgGroupName).getPreviousFireTime().getTime()));
                } else {
                    AuditCleaner.getInstance().setLastcleanup(new Date(0L));
                }

                logger.info("Audit Loggs Cleaner Started Next CleanUp: "
                        + AuditCleaner.getInstance().getNextcleanup()
                        .toString()
                        + " *** Last CleanUp: "
                        + AuditCleaner.getInstance().getLastcleanup()
                        .toString());

            } else {
                //Check if the main trigger (TimeToDeleteLoggs) is stored
                if (scheduler.getTrigger(trgName, trgGroupName)!=null){
                    if (scheduler.getTrigger(trgName, trgGroupName).getNextFireTime() != null) {
                        AuditCleaner.getInstance().setNextcleanup(new Date(scheduler.getTrigger(trgName, trgGroupName).getNextFireTime().getTime()));
                    } else {
                        AuditCleaner.getInstance().setLastcleanup(new Date(0L));
                    }

                    scheduler.resumeTrigger(trgName, trgGroupName);
                    if (scheduler.getJobDetail(jobDaily, jobGroupName) == null) {
                        startMessageJob();
                    } else {
                        scheduler.resumeTrigger(trgDaily, trgGroupName);
                    }

                    logger.info("Audit Loggs Cleaner is Allredy Configured Next CleanUp: "
                            + AuditCleaner.getInstance().getNextcleanup()
                            .toString());
                }else{
                    scheduler.deleteJob(jobName, jobGroupName);
                    jobDetail = new JobDetail(jobName, jobGroupName, classJob);
                    simpleTrigger = new SimpleTrigger(trgName, trgGroupName);
                    simpleTrigger.setStartTime(new Date(ctime + TEAN_DAYS));
                    simpleTrigger.setRepeatInterval(TEAN_DAYS);
                    simpleTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
                    scheduler.scheduleJob(jobDetail, simpleTrigger);
                    if (scheduler.getJobDetail(jobDaily, jobGroupName) == null){
                        startMessageJob();
                    }
                    if (scheduler.getTrigger(trgName, trgGroupName).getNextFireTime() != null) {
                        AuditCleaner.getInstance().setNextcleanup(new Date(scheduler.getTrigger(trgName, trgGroupName).getNextFireTime().getTime()));
                    } else {
                        AuditCleaner.getInstance().setLastcleanup(new Date(0L));
                    }
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void startMessageJob() {
        try {
            jobDetail = new JobDetail(jobDaily, jobGroupName,
                    AuditCleanerMsgJob.class);
            simpleTrigger = new SimpleTrigger(trgDaily, trgGroupName);
            simpleTrigger.setStartTime(new Date(ctime + FIVE_MIN));
            simpleTrigger.setRepeatInterval(ONE_DAY);
            simpleTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
            scheduler.scheduleJob(jobDetail, simpleTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}