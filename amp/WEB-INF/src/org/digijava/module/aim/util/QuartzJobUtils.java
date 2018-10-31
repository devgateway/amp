package org.digijava.module.aim.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.QuartzJobForm;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

public class QuartzJobUtils {
    private static Logger logger = Logger.getLogger(QuartzJobUtils.class);

    private static SimpleDateFormat sdf = new SimpleDateFormat(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT) + " HH:mm:ss");

    public static ArrayList<QuartzJobForm> getAllJobs() {
        Scheduler sched = getScheduler();
        try {
            ArrayList<QuartzJobForm> qrtzCol = new ArrayList<QuartzJobForm>();

            String[] trgGroupNames = sched.getTriggerGroupNames();
            for (int i = 0; i < trgGroupNames.length; i++) {
                String[] trgNames = sched.getTriggerNames(trgGroupNames[i]);
                if (trgNames != null) {
                    for (int j = 0; j < trgNames.length; j++) {
                        Trigger exTrg = sched.getTrigger(trgNames[j], trgGroupNames[i]);
                        if (exTrg != null) {

                            QuartzJobForm job = new QuartzJobForm();
                            /**
                             * variable to determine whether trigger is manual
                             * or not, we use this variable in
                             * quartzJobManager.jsp to paint in red manaully run
                             * trigger...
                             */
                            boolean manualTrg = false;
                            job.setName(exTrg.getJobName());
                            job.setGroupName(exTrg.getJobGroup());
                            job.setTriggerGroupName(exTrg.getGroup());
                            job.setTriggerName(exTrg.getName());
                            // checking whether trigger was run manually
                            if (exTrg.getStartTime().equals(exTrg.getFinalFireTime()) && exTrg.getGroup().equals(sched.DEFAULT_MANUAL_TRIGGERS)) {
                                manualTrg = true;
                            }
                            job.setManualJob(manualTrg);
                            if (exTrg instanceof CronTrigger) {
                                // daily,weekly and monthly triggers are
                                // CronTriggers
                                CronTrigger cTrg = (CronTrigger) exTrg;
                                String expString = cTrg.getCronExpression();
                                // parse cron expression to recreate date
                                String[] dates = expString.split(" ");
                                String hour, minute;
                                if (!dates[2].equals("*") && Integer.parseInt(dates[2]) < 10) {
                                    hour = "0" + dates[2]; // appending '0' to
                                    // march time
                                    // format: hh:mm
                                } else {
                                    hour = dates[2];
                                }
                                if (Integer.parseInt(dates[1]) < 10) {
                                    minute = "0" + dates[1]; // appending '0' to
                                    // march time
                                    // format: hh:mm
                                } else {
                                    minute = dates[1];
                                }
                                if (!dates[3].equals("?")) {
                                    // the month day is selected
                                    job.setTriggerType(QuartzJobForm.MONTHLY);
                                    job.setExeTimeH(hour);
                                    job.setExeTimeM(minute);
                                    job.setDayOfMonth(Integer.parseInt(dates[3]));
                                } else {
                                    if (!dates[5].equals("?") && !dates[5].equals("*")) {
                                        // week day is selected
                                        job.setTriggerType(QuartzJobForm.WEEKLY);
                                        job.setDayOfWeek(Integer.parseInt(dates[5]));
                                        job.setExeTimeH(hour);
                                        job.setExeTimeM(minute);
                                    } else {
                                        if (!hour.equals("*")) {
                                            // dayly is selected
                                            job.setExeTimeH(hour);
                                            job.setExeTimeM(minute);
                                        }
                                    }
                                }
                            } else {
                                if (exTrg instanceof SimpleTrigger) {
                                    // hour ,minute and secondr triggers are
                                    // SimpleTrigger
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
                            JobDetail jd = sched.getJobDetail(exTrg.getJobName(), exTrg.getJobGroup());
                            // delete manual job trigger from db
                            if (manualTrg && sched.getTriggerState(exTrg.getName(), exTrg.getGroup()) == Trigger.STATE_COMPLETE) {
                                sched.unscheduleJob(exTrg.getName(), exTrg.getGroup());
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

                            if (sched.getTriggerState(exTrg.getName(), exTrg.getGroup()) == Trigger.STATE_PAUSED) {
                                job.setPaused(true);
                            } else {
                                job.setPaused(false);
                            }

                            qrtzCol.add(job);
                        }
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

            Class cls = Class.forName(job.getClassFullname());

            JobDetail newJob = new JobDetail(job.getName(), null, cls);
            Trigger trg;
            switch (job.getTriggerType()) {
            case QuartzJobForm.EVERY_SECOND:
                trg = TriggerUtils.makeSecondlyTrigger(Integer.valueOf(job.getExeTimeS()));
                break;

            case QuartzJobForm.MINUTELY:
                trg = TriggerUtils.makeMinutelyTrigger(Integer.valueOf(job.getExeTimeM()));
                break;

            case QuartzJobForm.HOURLY:
                trg = TriggerUtils.makeHourlyTrigger(Integer.valueOf(job.getExeTimeH()));
                break;

            default:

                int h = Integer.valueOf(job.getExeTimeH());
                int m = Integer.valueOf(job.getExeTimeM());

                switch (job.getTriggerType()) {
                case QuartzJobForm.DAILY:
                    trg = TriggerUtils.makeDailyTrigger(h, m);
                    break;

                case QuartzJobForm.WEEKLY:
                    trg = TriggerUtils.makeWeeklyTrigger(job.getDayOfWeek(), h, m);
                    break;
                case QuartzJobForm.MONTHLY:
                    trg = TriggerUtils.makeMonthlyTrigger(job.getDayOfMonth(), h, m);
                    break;
                default:
                    trg = TriggerUtils.makeDailyTrigger(h, m);
                    break;
                }
                break;
            }
            trg.setName(job.getName() + "Trigger");

            trg.setStartTime(sdf.parse(job.getStartDateTime() + " " + job.getStartH() + ":" + job.getStartM() + ":00"));

            if (job.getEndDateTime() != null && !(job.getEndDateTime().equals(""))) {
                trg.setEndTime(sdf.parse(job.getEndDateTime() + " " + job.getEndH() + ":" + job.getEndM() + ":00"));
            }
            sched.scheduleJob(newJob, trg);
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
            sched.pauseJob(job.getName(), job.getGroupName());
            sched.pauseTrigger(job.getTriggerGroupName(), job.getTriggerGroupName());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void runJobIfNotPaused(String name) {
        QuartzJobForm job = getJobByName(name);
        if (!job.isPaused()) {
            runJob(job);
        }
    }

    /**
     * Creates a new trigger now for the given job. Used to run job manually
     * 
     * @param name
     *            name of the job
     * @see runJob(QuartzJobForm)
     */

    public static void runJob(String name) {
        QuartzJobForm job = getJobByName(name);
        runJob(job);
    }

    /**
     * Creates a new trigger now for the given job Used to run job manually.
     * 
     * @param job
     * @see runJob(String)
     * @see QuartzJobForm
     */
    public static void runJob(QuartzJobForm job) {
        Scheduler sched = getScheduler();
        try {
            sched.triggerJob(job.getName(), job.getGroupName());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void reScheduleJob(QuartzJobForm job) {
        try {
            //deleteJob(job); // delete previous job
            deleteJob(job.getName());
            addJob(job); // add new job
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
            sched.resumeTrigger(job.getTriggerGroupName(), job.getTriggerGroupName());
            sched.resumeJob(job.getName(), job.getGroupName());
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
            sched.deleteJob(job.getName(), job.getGroupName());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Scheduler getScheduler() {
        try {
            SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
            Scheduler sched = schedFact.getScheduler();
            return sched;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public QuartzJobUtils() {
    }
}
