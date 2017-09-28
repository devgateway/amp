package org.digijava.module.message.jobs;

import org.digijava.kernel.ampapi.endpoints.datafreeze.DataFreezeService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class ActivityFreezingJob extends ConnectionCleaningJob implements StatefulJob {

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        AmpJobsUtil.populateRequest();
        AmpJobsUtil.setTeamForNonRequestReport(59L);
        DataFreezeService.processFreezingEvent();

    }

}
