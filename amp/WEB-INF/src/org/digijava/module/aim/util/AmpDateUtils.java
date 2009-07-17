package org.digijava.module.aim.util;

/*
 Author: Vazha Ezugbaia
 */
import java.util.Calendar;
import java.util.Date;

public class AmpDateUtils {

    public static Date getDateBeforeDays(Date fromDate, int daysCount) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(fromDate);
        cl.add(Calendar.DAY_OF_MONTH,-daysCount);
        return cl.getTime();
    }

    public static Date getDateAfterDays(Date fromDate, int daysCount) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(fromDate);
        cl.add(Calendar.DAY_OF_MONTH, daysCount);
        return cl.getTime();
    }

    public AmpDateUtils() {
    }
}
