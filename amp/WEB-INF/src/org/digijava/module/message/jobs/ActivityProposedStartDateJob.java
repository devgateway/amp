package org.digijava.module.message.jobs;

import java.util.Date;
import java.util.List;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AmpDateUtils;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.triggers.ActivityCurrentCompletionDateTrigger;
import org.digijava.module.message.triggers.ActivityProposedStartDateTrigger;
import org.digijava.module.message.util.AmpMessageUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.text.SimpleDateFormat;

public class ActivityProposedStartDateJob extends ConnectionCleaningJob implements StatefulJob { 
    
    @Override 
    public void executeInternal(JobExecutionContext context) throws JobExecutionException{      

        Date curDate=new Date();
        Date dateAfterDays=null;
        try{
            AmpMessageSettings as=AmpMessageUtil.getMessageSettings();
            if(as!=null &&
               as.getDaysForAdvanceAlertsWarnings()!=null &&
               as.getDaysForAdvanceAlertsWarnings().intValue()>0){
                dateAfterDays=AmpDateUtils.getDateAfterDays(curDate,as.getDaysForAdvanceAlertsWarnings().intValue());
            }else{
                dateAfterDays=AmpDateUtils.getDateAfterDays(curDate,3);
            }
        }catch(Exception ex){
            dateAfterDays=AmpDateUtils.getDateAfterDays(curDate,3);
        }

        List<AmpActivityVersion> actList = ActivityUtil.getActivitiesWhichMatchDate("proposedStartDate", dateAfterDays);
        for (AmpActivityVersion act : actList) {
            new ActivityProposedStartDateTrigger(act);
        }
    }
}
