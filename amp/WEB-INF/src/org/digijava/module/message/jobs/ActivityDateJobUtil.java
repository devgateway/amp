package org.digijava.module.message.jobs;

import org.digijava.module.aim.util.AmpDateUtils;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.util.AmpMessageUtil;

import java.util.Date;

public final class ActivityDateJobUtil {
    private static final Integer DAYS_COUNT = 3;

    private ActivityDateJobUtil() {
    }

    /**
     * @return the date after the count of days specified
     */
    public static Date getDateAfterDays() {
        Date curDate = new Date();
        Date dateAfterDays;
        try {
            AmpMessageSettings as = AmpMessageUtil.getMessageSettings();
            if (as != null && as.getDaysForAdvanceAlertsWarnings() != null
                    && as.getDaysForAdvanceAlertsWarnings().intValue() > 0) {
                dateAfterDays = AmpDateUtils.getDateAfterDays(curDate, as.getDaysForAdvanceAlertsWarnings().intValue());
            } else {
                dateAfterDays = AmpDateUtils.getDateAfterDays(curDate, DAYS_COUNT);
            }
        } catch (Exception ex) {
            dateAfterDays = AmpDateUtils.getDateAfterDays(curDate, DAYS_COUNT);
        }

        return dateAfterDays;
    }


}
