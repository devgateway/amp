package org.digijava.module.message.jobs;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.datafreeze.DataFreezeUtil;
import org.digijava.module.aim.dbentity.AmpDataFreezeSettings;
import org.digijava.module.aim.util.AmpDateUtils;
import org.digijava.module.message.triggers.DataFreezeEmailNotificationTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.util.Date;
import java.util.List;

public class DataFreezeEmailNotificationJob extends ConnectionCleaningJob implements StatefulJob {
    private static Logger logger = Logger.getLogger(DataFreezeEmailNotificationJob.class);
    public static final Integer DATA_FREEZE_NOTIFICATION_DAYS = 7;

    @Override
    public void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        List<AmpDataFreezeSettings> events = DataFreezeUtil.getEnabledDataFreezeEvents(null);
        try {
            for (AmpDataFreezeSettings event : events) {
                if (Boolean.TRUE.equals(event.getSendNotification())) {
                    Integer notificationDays = event.getNotificationDays() != null ? event.getNotificationDays()
                            : DATA_FREEZE_NOTIFICATION_DAYS;
                    Date freezingDate = DataFreezeUtil.getFreezingDate(event);
                    Integer numberOfDaysToFreezingDate = AmpDateUtils.daysBetween(new Date(), freezingDate);
                    if (numberOfDaysToFreezingDate == notificationDays) {
                        new DataFreezeEmailNotificationTrigger(event);
                    }
                }
            }

        } catch (Exception ex) {
            logger.error("Error sending data freeze notification" + ex);
        }
    }

}
