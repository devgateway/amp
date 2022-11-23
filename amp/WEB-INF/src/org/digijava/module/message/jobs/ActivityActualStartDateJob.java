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
import org.digijava.module.message.triggers.ActivityActualStartDateTrigger;
import org.digijava.module.message.util.AmpMessageUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class ActivityActualStartDateJob extends ConnectionCleaningJob implements StatefulJob {

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Date dateAfterDays = ActivityDateJobUtil.getDateAfterDays();

        List<AmpActivityVersion> actList = ActivityUtil.getActivitiesWhichMatchDate("actualStartDate", dateAfterDays);
        for (AmpActivityVersion act : actList) {
            new ActivityActualStartDateTrigger(act);
        }
    }
}
