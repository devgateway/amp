package org.digijava.module.calendar.jobs;

import java.util.Date;
import java.util.List;

import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.util.AmpDbUtil;
import org.digijava.module.message.jobs.ActivityDateJobUtil;
import org.digijava.module.message.jobs.ConnectionCleaningJob;
import org.digijava.module.message.triggers.CalendarEventTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class CalendarEventJob extends ConnectionCleaningJob implements StatefulJob {

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Date dateAfterDays = ActivityDateJobUtil.getDateAfterDays();

        List<AmpCalendar> eventList = AmpDbUtil.getAmpCalendarsByStartDate(dateAfterDays);
        if (eventList != null && !eventList.isEmpty()) {
            for (AmpCalendar cal : eventList) {
                new CalendarEventTrigger(cal);
            }
        }
    }

    public CalendarEventJob() {
    }
}
