package org.digijava.module.message.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AmpDateUtils;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.triggers.ActivityProposedCompletionDateTrigger;
import org.digijava.module.message.util.AmpMessageUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class ActivityProposedCompletionDateJob extends ConnectionCleaningJob implements StatefulJob { 
	
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

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String exDt=sdf.format(dateAfterDays);
        List<AmpActivityVersion> actList=ActivityUtil.getAllAssignedActivitiesList();
        if(actList!=null){
            for (AmpActivityVersion act : actList) {
                if (act.getProposedCompletionDate() != null) {
                    String dt = sdf.format(act.getProposedCompletionDate());
                    if (dt.equals(exDt)) {
                        new ActivityProposedCompletionDateTrigger(act);
                    }
                }
            }
        }
    }
}
