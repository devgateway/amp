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

    public static int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
    
    public AmpDateUtils() {
    }
}
