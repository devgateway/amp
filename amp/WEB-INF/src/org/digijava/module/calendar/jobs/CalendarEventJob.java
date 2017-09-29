package org.digijava.module.calendar.jobs;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.util.AmpDateUtils;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.util.AmpDbUtil;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.jobs.ConnectionCleaningJob;
import org.digijava.module.message.triggers.CalendarEventTrigger;
import org.digijava.module.message.util.AmpMessageUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class CalendarEventJob extends ConnectionCleaningJob implements StatefulJob { 
    
    @Override 
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {     

        Date curDate=new Date();
        Date dateAfterDays;
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

        java.util.Calendar cl=java.util.Calendar.getInstance();
        cl.setTime(curDate);

        List<AmpCalendar> eventList=AmpDbUtil.getAmpCalendarsByStartDate(dateAfterDays);
        if(eventList!=null && !eventList.isEmpty()){
            for (AmpCalendar cal: eventList){
                new CalendarEventTrigger(cal);
            }
        }
    }

    public CalendarEventJob() {
    }
}
