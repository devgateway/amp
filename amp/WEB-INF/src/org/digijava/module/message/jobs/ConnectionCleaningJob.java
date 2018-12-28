package org.digijava.module.message.jobs;

import org.digijava.kernel.persistence.PersistenceManager;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Job;

public abstract class ConnectionCleaningJob implements Job {
    
    @Override public final void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            PersistenceManager.inTransaction(() -> {
                try {
                    executeInternal(context);
                } catch (JobExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e) {
            if (e.getCause() instanceof JobExecutionException) {
                throw (JobExecutionException) e.getCause();
            } else {
                throw e;
            }
        }
    }
    
    public abstract void executeInternal(JobExecutionContext context) throws JobExecutionException;
}
