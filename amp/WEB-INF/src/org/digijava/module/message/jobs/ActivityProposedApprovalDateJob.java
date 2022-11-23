package org.digijava.module.message.jobs;

import java.util.Date;
import java.util.List;

import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.message.triggers.ActivityProposedApprovalDateTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class ActivityProposedApprovalDateJob extends ConnectionCleaningJob implements StatefulJob {
    
    @Override public void executeInternal(JobExecutionContext context) throws JobExecutionException{
        Date dateAfterDays = ActivityDateJobUtil.getDateAfterDays();

        List<AmpActivityVersion> actList = ActivityUtil.getActivitiesWhichMatchDate("proposedApprovalDate", dateAfterDays);
        for (AmpActivityVersion act : actList) {
            new ActivityProposedApprovalDateTrigger(act);
        }
    }
}
