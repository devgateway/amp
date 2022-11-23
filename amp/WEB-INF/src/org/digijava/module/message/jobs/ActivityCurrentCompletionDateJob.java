package org.digijava.module.message.jobs;

import java.util.Date;
import java.util.List;

import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.message.triggers.ActivityCurrentCompletionDateTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class ActivityCurrentCompletionDateJob extends ConnectionCleaningJob implements StatefulJob {
    
    
    @Override public void executeInternal(JobExecutionContext context) throws JobExecutionException{

        Date dateAfterDays = ActivityDateJobUtil.getDateAfterDays();

        List<AmpActivityVersion> actList = ActivityUtil.getActivitiesWhichMatchDate("actualCompletionDate", dateAfterDays);
        for (AmpActivityVersion act : actList) {
            new ActivityCurrentCompletionDateTrigger(act);
        }
    }
}
