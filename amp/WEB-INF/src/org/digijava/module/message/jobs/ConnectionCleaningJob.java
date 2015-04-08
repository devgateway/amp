package org.digijava.module.message.jobs;

import org.digijava.kernel.persistence.PersistenceManager;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Job;

public abstract class ConnectionCleaningJob implements Job {
	
	@Override public final void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			executeInternal(context);
		}
		finally {
			PersistenceManager.endSessionLifecycle();
		}
	}
	
	public abstract void executeInternal(JobExecutionContext context) throws JobExecutionException;
}
