package org.digijava.module.aim.util;

/*
 Author: Vazha Ezugbaia
 */
import java.util.Date;
import java.util.Calendar;

public class AmpDateUtils {

    public static Date getDateBeforeDays(Date fromDate, int daysCount) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(fromDate);
        cl.roll(Calendar.DAY_OF_MONTH, -daysCount);
        return cl.getTime();
    }

    public AmpDateUtils() {
    }
}
