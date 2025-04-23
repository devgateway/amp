package org.digijava.module.digifeed.utils;

import org.digijava.module.common.util.DateTimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class PublisherDateFormatter {
    //private static SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT);
    private static SimpleDateFormat valueDateFormat = new SimpleDateFormat("EEE MMM d yyyy");
    public static Date parse(String date) throws ParseException {
        return valueDateFormat.parse(date);
    }

    public static String print(Date date) {
        String result = null;
        if (date != null) {
            result = DateTimeUtil.formatDate(date);
        }
        return result;
    }
    
    public static String valuePrint(Date date) {
        String result = null;
        if (date != null) {
            result = valueDateFormat.format(date);
        }
        return result;
    }
}

