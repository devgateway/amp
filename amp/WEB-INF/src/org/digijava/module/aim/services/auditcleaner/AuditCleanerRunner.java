package org.digijava.module.aim.services.auditcleaner;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class AuditCleanerRunner {

    private SchedulerFactory schedulerFactory;
    private Scheduler scheduler;
    private JobDetail jobDetail;
    private Trigger simpleTrigger;
    private final String jobName = "DeleteAuditLoggs";
    private final String jobGroupName = "ampServices";
    private final String trgName = "TimeToDeleteLoggs";
    private final String jobDaily = "AuditCheckTime";
    private final String trgDaily = "CheckDeleteTime";
    private final String trgGroupName = "triggerGroup-s1";
    private Class<? extends Job> classJob = AuditCleanerJob.class;
    private final static long ONE_DAY = 1000 * 60 * 60 * 24;
    private final static long TEN_DAYS = 1000 * 60 * 60 * 24 * 10;
    private final static long FIVE_MINUTES = 1000 * 60 * 5;

    private static Logger logger = LoggerFactory.getLogger(AuditCleanerRunner.class);

    private Long ctime = System.currentTimeMillis();

    public void stop() {
        try {
            scheduler.pauseTrigger(new TriggerKey(trgName, trgGroupName));
            scheduler.pauseTrigger(new TriggerKey(trgDaily, trgGroupName));
            logger.info("Audit Loggs Cleaner Stopped.....................");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void launch() {

        try {
            schedulerFactory = new StdSchedulerFactory();
            scheduler = schedulerFactory.getScheduler();
            // Check if the main job (DeleteAuditLoggs) is stored, add a new job if it's not there
            if (scheduler.getJobDetail(new JobKey(jobName, jobGroupName)) == null) {
                jobDetail = JobBuilder.newJob(classJob)
                        .withIdentity(jobName, jobGroupName)
                        .build();
                simpleTrigger = TriggerBuilder.newTrigger()
                        .withIdentity(trgName, trgGroupName)
                        .startAt(new Date(ctime + TEN_DAYS))
                        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInMilliseconds(TEN_DAYS)
                                .repeatForever())
                        .build();
                scheduler.scheduleJob(jobDetail, simpleTrigger);
                startMessageJob();

                if (scheduler.getTrigger(new TriggerKey(trgName, trgGroupName)).getNextFireTime() != null) {
                    AuditCleaner.getInstance().setNextcleanup(new Date(scheduler.getTrigger(new TriggerKey(trgName, trgGroupName)).getNextFireTime().getTime()));
                } else {
                    AuditCleaner.getInstance().setLastcleanup(new Date(0L));
                }
                if (scheduler.getTrigger(new TriggerKey(trgName, trgGroupName)).getPreviousFireTime() != null) {
                    AuditCleaner.getInstance().setLastcleanup(new Date(scheduler.getTrigger(new TriggerKey(trgName, trgGroupName)).getPreviousFireTime().getTime()));
                } else {
                    AuditCleaner.getInstance().setLastcleanup(new Date(0L));
                }

                logger.info("Audit Loggs Cleaner Started Next CleanUp: " +
                        AuditCleaner.getInstance().getNextcleanup().toString() +
                        " *** Last CleanUp: " +
                        AuditCleaner.getInstance().getLastcleanup().toString());

            } else {
                // Check if the main trigger (TimeToDeleteLoggs) is stored
                if (scheduler.getTrigger(new TriggerKey(trgName, trgGroupName)) != null) {
                    if (scheduler.getTrigger(new TriggerKey(trgName, trgGroupName)).getNextFireTime() != null) {
                        AuditCleaner.getInstance().setNextcleanup(new Date(scheduler.getTrigger(new TriggerKey(trgName, trgGroupName)).getNextFireTime().getTime()));
                    } else {
                        AuditCleaner.getInstance().setLastcleanup(new Date(0L));
                    }

                    scheduler.resumeTrigger(new TriggerKey(trgName, trgGroupName));
                    if (scheduler.getJobDetail(new JobKey(jobDaily, jobGroupName)) == null) {
                        startMessageJob();
                    } else {
                        scheduler.resumeTrigger(new TriggerKey(trgDaily, trgGroupName));
                    }

                    logger.info("Audit Loggs Cleaner is Already Configured Next CleanUp: " +
                            AuditCleaner.getInstance().getNextcleanup().toString());
                } else {
                    scheduler.deleteJob(new JobKey(jobName, jobGroupName));
                    jobDetail = JobBuilder.newJob(classJob)
                            .withIdentity(jobName, jobGroupName)
                            .build();
                    simpleTrigger = TriggerBuilder.newTrigger()
                            .withIdentity(trgName, trgGroupName)
                            .startAt(new Date(ctime + TEN_DAYS))
                            .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInMilliseconds(TEN_DAYS)
                                    .repeatForever())
                            .build();
                    scheduler.scheduleJob(jobDetail, simpleTrigger);
                    if (scheduler.getJobDetail(new JobKey(jobDaily, jobGroupName)) == null) {
                        startMessageJob();
                    }
                    if (scheduler.getTrigger(new TriggerKey(trgName, trgGroupName)).getNextFireTime() != null) {
                        AuditCleaner.getInstance().setNextcleanup(new Date(scheduler.getTrigger(new TriggerKey(trgName, trgGroupName)).getNextFireTime().getTime()));
                    } else {
                        AuditCleaner.getInstance().setLastcleanup(new Date(0L));
                    }
                }
            }
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void startMessageJob() {
        try {
            jobDetail = JobBuilder.newJob(AuditCleanerMsgJob.class)
                    .withIdentity(jobDaily, jobGroupName)
                    .build();
            simpleTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(trgDaily, trgGroupName)
                    .startAt(new Date(ctime + FIVE_MINUTES))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInMilliseconds(ONE_DAY)
                            .repeatForever())
                    .build();
            scheduler.scheduleJob(jobDetail, simpleTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}

