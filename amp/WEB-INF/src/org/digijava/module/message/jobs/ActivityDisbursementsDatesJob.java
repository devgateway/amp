package org.digijava.module.message.jobs;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AmpDateUtils;
import org.digijava.module.message.triggers.ActivityDisbursementDateTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class ActivityDisbursementsDatesJob implements StatefulJob {

	private static Logger logger = Logger.getLogger(ActivityDisbursementsDatesJob.class);

	public void execute(JobExecutionContext context) throws JobExecutionException{

        Date curDate=new Date();
        logger.info("Starting Activity Disbursements Dates checker quartz job at "+curDate);
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
        try {
			PersistenceManager.closeRequestDBSessionIfNeeded();
		} catch (DgException e) {
			e.printStackTrace();
		}
        logger.info("Finnished Activity Disbursements Dates checker quartz job for "+curDate);
    }
}
