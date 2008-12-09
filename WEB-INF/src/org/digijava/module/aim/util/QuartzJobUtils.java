package org.digijava.module.aim.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.digijava.module.aim.helper.QuartzJobForm;

public class QuartzJobUtils {

    public static ArrayList<QuartzJobForm> getAllJobs(){
        Scheduler sched=getScheduler();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            ArrayList<QuartzJobForm> qrtzCol = new ArrayList<QuartzJobForm> ();

            String[] trgGroupNames = sched.getTriggerGroupNames();
            for (int i = 0; i < trgGroupNames.length; i++) {
                String[] trgNames = sched.getTriggerNames(trgGroupNames[i]);
                if (trgNames != null) {
                    for (int j = 0; j < trgNames.length; j++) {
                        Trigger exTrg = sched.getTrigger(trgNames[j], trgGroupNames[i]);
                        if (exTrg != null) {

                            QuartzJobForm job = new QuartzJobForm();

                            job.setName(exTrg.getJobName());
                            job.setGroupName(exTrg.getJobGroup());
                            job.setTriggerGroupName(exTrg.getGroup());
                            job.setTriggerName(exTrg.getName());
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
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static QuartzJobForm getJobByName(String name){
        ArrayList<QuartzJobForm> jobsCol = getAllJobs();
        if(jobsCol!=null){
            for(QuartzJobForm job:jobsCol){
                if(name.equals(job.getName())){
                    return job;
                }
            }
        }
        return null;
    }

    public static void addJob(QuartzJobForm job){
        try{
            Scheduler sched=getScheduler();

            Class cls = Class.forName(job.getClassFullname());

            JobDetail newJob = new JobDetail(job.getName(), null, cls);
            Trigger trg;
            switch (job.getTriggerType()) {
                case 0:
                    trg = TriggerUtils.makeSecondlyTrigger(Integer.valueOf(job.getExeTime()));
                    break;

                case 1:
                    trg = TriggerUtils.makeMinutelyTrigger(Integer.valueOf(job.getExeTime()));
                    break;

                case 2:
                    trg = TriggerUtils.makeHourlyTrigger(Integer.valueOf(job.getExeTime()));
                    break;

                default:
                    String[] tm = job.getExeTime().split(":");
                    int h = 0;
                    if (tm.length > 0)
                        h = Integer.valueOf(tm[0]);

                    int m = 0;
                    if (tm.length > 1)
                        m = Integer.valueOf(tm[1]);

                    switch (job.getTriggerType()) {
                        case 3:
                            trg = TriggerUtils.makeDailyTrigger(h, m);
                            break;

                        case 4:
                            trg = TriggerUtils.makeWeeklyTrigger(job.getDayOfWeek(), h, m);
                            break;
                        case 5:
                            trg=TriggerUtils.makeMonthlyTrigger(job.getDayOfMonth(), h, m);
                            break;
                        default:
                            trg = TriggerUtils.makeDailyTrigger(h, m);
                            break;
                    }
                    break;
            }
            trg.setName(job.getName() + "Trigger");

            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            trg.setStartTime(sdf.parse(job.getStartDateTime()));
            if(job.getEndDateTime()!=null && job.getEndDateTime().equals(""))trg.setEndTime(sdf.parse(job.getEndDateTime()));
            sched.scheduleJob(newJob, trg);

            sched.start();
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static void pauseAll(){
        Scheduler sched=getScheduler();
        try{
            sched.pauseAll();
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    public static void resumeAll(){
        Scheduler sched=getScheduler();
        try{
            sched.resumeAll();
        }catch(Exception ex){
           throw new RuntimeException(ex);
        }
    }


    public static void pauseJob(String name){
        QuartzJobForm job=getJobByName(name);
        pauseJob(job);
    }
    public static void pauseJob(QuartzJobForm job){
        Scheduler sched=getScheduler();
        try{
            sched.pauseJob(job.getName(),job.getGroupName());
            sched.pauseTrigger(job.getTriggerGroupName(),job.getTriggerGroupName());
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static void resumeJob(String name){
        QuartzJobForm job=getJobByName(name);
        resumeJob(job);
    }
    public static void resumeJob(QuartzJobForm job){
        Scheduler sched=getScheduler();
        try{
            sched.resumeTrigger(job.getTriggerGroupName(),job.getTriggerGroupName());
            sched.resumeJob(job.getName(),job.getGroupName());
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static void deleteJob(String name){
        QuartzJobForm job=getJobByName(name);
        deleteJob(job);
    }

    public static void deleteJob(QuartzJobForm job){
        Scheduler sched=getScheduler();
        try{
            sched.deleteJob(job.getName(),job.getGroupName());
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static Scheduler getScheduler(){
        try{
            SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
            Scheduler sched = schedFact.getScheduler();
            return sched;
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    public QuartzJobUtils() {
    }
}
