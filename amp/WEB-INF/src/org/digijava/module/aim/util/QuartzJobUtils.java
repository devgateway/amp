package org.digijava.module.aim.util;

import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.QuartzJobForm;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QuartzJobUtils {
    private static Logger logger = LoggerFactory.getLogger(QuartzJobUtils.class);

    private static SimpleDateFormat sdf = new SimpleDateFormat(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT) + " HH:mm:ss");

    public static ArrayList<QuartzJobForm> getAllJobs() {
        Scheduler sched = getScheduler();
        try {
            ArrayList<QuartzJobForm> qrtzCol = new ArrayList<QuartzJobForm>();

            for (String trgGroup : sched.getTriggerGroupNames()) {
                for (TriggerKey triggerKey : sched.getTriggerKeys(GroupMatcher.triggerGroupEquals(trgGroup))) {
                    Trigger exTrg = sched.getTrigger(triggerKey);
                    if (exTrg != null) {
                        QuartzJobForm job = new QuartzJobForm();
                        boolean manualTrg = false;
                        job.setName(exTrg.getJobKey().getName());
                        job.setGroupName(exTrg.getJobKey().getGroup());
                        job.setTriggerGroupName(exTrg.getKey().getGroup());
                        job.setTriggerName(exTrg.getKey().getName());
                        if (exTrg.getStartTime().equals(exTrg.getFinalFireTime()) && exTrg.getKey().getGroup().equals("manualTriggers")) {
                            manualTrg = true;
                        }
                        job.setManualJob(manualTrg);
                        if (exTrg instanceof CronTrigger) {
                            CronTrigger cTrg = (CronTrigger) exTrg;
                            String expString = cTrg.getCronExpression();
                            String[] dates = expString.split(" ");
                            String hour, minute;
                            if (!dates[2].equals("*") && Integer.parseInt(dates[2]) < 10) {
                                hour = "0" + dates[2];
                            } else {
                                hour = dates[2];
                            }
                            if (Integer.parseInt(dates[1]) < 10) {
                                minute = "0" + dates[1];
                            } else {
                                minute = dates[1];
                            }
                            if (!dates[3].equals("?")) {
                                job.setTriggerType(QuartzJobForm.MONTHLY);
                                job.setExeTimeH(hour);
                                job.setExeTimeM(minute);
                                job.setDayOfMonth(Integer.parseInt(dates[3]));
                            } else {
                                if (!dates[5].equals("?") && !dates[5].equals("*")) {
                                    job.setTriggerType(QuartzJobForm.WEEKLY);
                                    job.setDayOfWeek(Integer.parseInt(dates[5]));
                                    job.setExeTimeH(hour);
                                    job.setExeTimeM(minute);
                                } else {
                                    if (!hour.equals("*")) {
                                        job.setExeTimeH(hour);
                                        job.setExeTimeM(minute);
                                    }
                                }
                            }
                        } else {
                            if (exTrg instanceof SimpleTrigger) {
                                SimpleTrigger sTrg = (SimpleTrigger) exTrg;
                                long dif = sTrg.getRepeatInterval() / 1000;
                                Long hours = dif / (60 * 60);
                                if (hours > 0) {
                                    job.setExeTimeH(hours.toString());
                                    job.setTriggerType(QuartzJobForm.HOURLY);
                                } else {
                                    Long minutes = dif / 60;
                                    if (minutes > 0) {
                                        job.setTriggerType(QuartzJobForm.MINUTELY);
                                        job.setExeTimeM(minutes.toString());
                                    } else {
                                        Long seconds = dif;
                                        job.setTriggerType(QuartzJobForm.EVERY_SECOND);
                                        job.setExeTimeS(seconds.toString());
                                    }
                                }
                            }
                        }
                        JobDetail jd = sched.getJobDetail(exTrg.getJobKey());
                        if (manualTrg && sched.getTriggerState(exTrg.getKey()) == Trigger.TriggerState.COMPLETE) {
                            sched.unscheduleJob(exTrg.getKey());
                        }
                        job.setClassFullname(jd.getJobClass().getName());
                        if (exTrg.getEndTime() != null)
                            job.setEndDateTime(sdf.format(exTrg.getEndTime()));
                        if (exTrg.getFinalFireTime() != null)
                            job.setFinalFireDateTime(sdf.format(exTrg.getFinalFireTime()));
                        if (exTrg.getNextFireTime() != null)
                            job.setNextFireDateTime(sdf.format(exTrg.getNextFireTime()));
                        if (exTrg.getPreviousFireTime() != null)
                            job.setPrevFireDateTime(sdf.format(exTrg.getPreviousFireTime()));
                        if (exTrg.getStartTime() != null)
                            job.setStartDateTime(sdf.format(exTrg.getStartTime()));
                        if (sched.getTriggerState(exTrg.getKey()) == Trigger.TriggerState.PAUSED) {
                            job.setPaused(true);
                        } else {
                            job.setPaused(false);
                        }
                        qrtzCol.add(job);
                    }
                }
            }
            return qrtzCol;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static QuartzJobForm getJobByClassFullname(String classFullname) {
        ArrayList<QuartzJobForm> jobsCol = getAllJobs();
        if (jobsCol != null) {
            for (QuartzJobForm job : jobsCol) {
                if (classFullname.equals(job.getClassFullname())) {
                    return job;
                }
            }
        }
        return null;
    }

    public static QuartzJobForm getJobByName(String name) {
        ArrayList<QuartzJobForm> jobsCol = getAllJobs();
        if (jobsCol != null) {
            for (QuartzJobForm job : jobsCol) {
                if (name.equals(job.getName())) {
                    return job;
                }
            }
        }
        return null;
    }

    public static void addJob(QuartzJobForm job) throws Exception {
        Scheduler sched = getScheduler();

        Class<? extends Job> cls = (Class<? extends Job>) Class.forName(job.getClassFullname());

        JobDetail newJob = JobBuilder.newJob(cls)
                .withIdentity(job.getName(), job.getGroupName())
                .build();

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity(job.getTriggerName(), job.getTriggerGroupName())
                .forJob(job.getName(), job.getGroupName());

        switch (job.getTriggerType()) {
            case QuartzJobForm.EVERY_SECOND:
                triggerBuilder.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(Integer.valueOf(job.getExeTimeS())));
                break;
            case QuartzJobForm.MINUTELY:
                triggerBuilder.withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(Integer.valueOf(job.getExeTimeM())));
                break;
            case QuartzJobForm.HOURLY:
                triggerBuilder.withSchedule(SimpleScheduleBuilder.repeatHourlyForever(Integer.valueOf(job.getExeTimeH())));
                break;
            default:
                int h = Integer.valueOf(job.getExeTimeH());
                int m = Integer.valueOf(job.getExeTimeM());

                switch (job.getTriggerType()) {
                    case QuartzJobForm.DAILY:
                        triggerBuilder.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(h, m));
                        break;
                    case QuartzJobForm.WEEKLY:
                        triggerBuilder.withSchedule(CronScheduleBuilder.weeklyOnDayAndHourAndMinute(job.getDayOfWeek(), h, m));
                        break;
                    case QuartzJobForm.MONTHLY:
                        triggerBuilder.withSchedule(CronScheduleBuilder.monthlyOnDayAndHourAndMinute(job.getDayOfMonth(), h, m));
                        break;
                    default:
                        triggerBuilder.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(h, m));
                        break;
                }
                break;
        }

        if (job.getStartDateTime() != null && !job.getStartDateTime().isEmpty()) {
            Date startTime = sdf.parse(job.getStartDateTime() + " " + job.getStartH() + ":" + job.getStartM() + ":00");
            triggerBuilder.startAt(startTime);
        }

        if (job.getEndDateTime() != null && !job.getEndDateTime().isEmpty()) {
            Date endTime = sdf.parse(job.getEndDateTime() + " " + job.getEndH() + ":" + job.getEndM() + ":00");
            triggerBuilder.endAt(endTime);
        }

        Trigger trigger = triggerBuilder.build();

        sched.scheduleJob(newJob, trigger);
    }


    public static void pauseAll() {
        Scheduler sched = getScheduler();
        try {
            sched.pauseAll();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void resumeAll() {
        Scheduler sched = getScheduler();
        try {
            sched.resumeAll();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void pauseJob(String name) {
        QuartzJobForm job = getJobByName(name);
        pauseJob(job);
    }

    public static void pauseJob(QuartzJobForm job) {
        Scheduler sched = getScheduler();
        try {
            sched.pauseJob(JobKey.jobKey(job.getName(), job.getGroupName()));
            sched.pauseTrigger(TriggerKey.triggerKey(job.getTriggerName(), job.getTriggerGroupName()));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void runJobIfNotPaused(String name) {
        QuartzJobForm job = getJobByName(name);
        if (job != null && !job.isPaused()) {
            runJob(job);
        }
    }

    public static void runJob(String name) {
        QuartzJobForm job = getJobByName(name);
        runJob(job);
    }

    public static void runJob(QuartzJobForm job) {
        Scheduler sched = getScheduler();
        try {
            sched.triggerJob(JobKey.jobKey(job.getName(), job.getGroupName()));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void reScheduleJob(QuartzJobForm job) {
        try {
            deleteJob(job.getName());
            addJob(job);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void resumeJob(String name) {
        QuartzJobForm job = getJobByName(name);
        resumeJob(job);
    }

    public static void resumeJob(QuartzJobForm job) {
        Scheduler sched = getScheduler();
        try {
            sched.resumeTrigger(TriggerKey.triggerKey(job.getTriggerName(), job.getTriggerGroupName()));
            sched.resumeJob(JobKey.jobKey(job.getName(), job.getGroupName()));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void deleteJob(String name) {
        QuartzJobForm job = getJobByName(name);
        deleteJob(job);
    }

    public static void deleteJob(QuartzJobForm job) {
        Scheduler sched = getScheduler();
        try {
            sched.deleteJob(JobKey.jobKey(job.getName(), job.getGroupName()));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Scheduler getScheduler() {
        try {
            SchedulerFactory schedFact = new StdSchedulerFactory();
            Scheduler sched = schedFact.getScheduler();
            return sched;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public QuartzJobUtils() {
    }
}
