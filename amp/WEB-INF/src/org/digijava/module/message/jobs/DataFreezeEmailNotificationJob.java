package org.digijava.module.message.jobs;

import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.datafreeze.DataFreezeUtil;
import org.digijava.module.aim.dbentity.AmpDataFreezeSettings;
import org.digijava.module.aim.util.AmpDateUtils;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.message.triggers.DataFreezeEmailNotificationTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class DataFreezeEmailNotificationJob extends ConnectionCleaningJob implements StatefulJob {
    private static Logger logger = Logger.getLogger(DataFreezeEmailNotificationJob.class);
    public static final Integer DATA_FREEZE_NOTIFICATION_DAYS = 7;

    @Override
    public void executeInternal(JobExecutionContext arg0) throws JobExecutionException {

        List<AmpDataFreezeSettings> events = DataFreezeUtil.getEnabledDataFreezeEvents(null);
        try {
            for (AmpDataFreezeSettings event : events) {
                Date freezingDate = (event.getGracePeriod() != null)
                        ? AmpDateUtils.getDateAfterDays(event.getFreezingDate(), event.getGracePeriod())
                        : event.getFreezingDate();
                Integer numberOfDaysToFreezing = AmpDateUtils.daysBetween(new Date(), freezingDate);
                logger.info("--------------------------------------------------------------");
                logger.info("Event Freeze Date: " + DateTimeUtil.formatDate(event.getFreezingDate()));
                logger.info("Grace Period: " + event.getGracePeriod());
                logger.info("Effective Freeze Date: " + DateTimeUtil.formatDate(freezingDate));
                logger.info("Number of days to freezing date: " + numberOfDaysToFreezing);
                if (numberOfDaysToFreezing == DATA_FREEZE_NOTIFICATION_DAYS) {
                    new DataFreezeEmailNotificationTrigger(event);
                }
            }

        } catch (Exception ex) {
            logger.error("Error sending data freeze notification" + ex);
        }
    }

}
