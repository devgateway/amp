package org.digijava.module.message.jobs;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Job class to prevent the same job from executing concurrently
 * 
 * @author Viorel Chihai
 *
 */
public abstract class NonConcurrentJob extends ConnectionCleaningJob {
    private final Logger logger = LoggerFactory.getLogger(NonConcurrentJob.class);
    
    @Override public final void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {      
            List<JobExecutionContext> jobs = context.getScheduler().getCurrentlyExecutingJobs();
            if (jobs != null && !jobs.isEmpty()) {
                  for (JobExecutionContext job : jobs) {
                    if (job.getTrigger().getJobName().equals(context.getTrigger().getJobName()) 
                            && !job.getJobInstance().equals(this)) {
                        logger.warn("There's another instance running, : " + this);
                        return;
                    }
                }
            }
        } catch (SchedulerException e) {
            logger.error("Error during non concurrent job execution ", e);
        }
        executeNonConcurrentInternal(context);
    }
    
    public abstract void executeNonConcurrentInternal(JobExecutionContext context) throws JobExecutionException;
}
