package org.digijava.module.message.jobs;

import org.quartz.StatefulJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.dbentity.AmpActivity;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import org.digijava.module.aim.util.AmpDateUtils;
import org.digijava.module.message.helper.ActivityDisbursementDateTrigger;

public class ActivityDisbursementsDatesJob implements StatefulJob {
    public void execute(JobExecutionContext context) throws JobExecutionException{

        Date curDate=new Date();
        Date dateBefore2Days=AmpDateUtils.getDateBeforeDays(curDate,2);

        Calendar cl=Calendar.getInstance();
        cl.setTime(curDate);

        List<AmpActivity> actList=ActivityUtil.getAllActivitiesList();
        for (AmpActivity act: actList){
            if(act.getDisbursmentsDate()!=null){
                if(act.getDisbursmentsDate().equals(dateBefore2Days) ||
                   act.getDisbursmentsDate().after(dateBefore2Days)){
                   new ActivityDisbursementDateTrigger(act);
                }
            }
        }
    }
}
