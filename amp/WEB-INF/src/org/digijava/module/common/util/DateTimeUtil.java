/*
 *   DateTimeUtil.java
 *   @Author Lasha Dolidze lasha@digijava.org
 *   Created: Oct 20, 2003
 *   CVS-ID: $Id$
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/
package org.digijava.module.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersConstants;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.translation.exotic.AmpDateFormatterFactory;
import org.digijava.module.translation.exotic.AmpDateFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DateTimeUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateTimeUtil.class);

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMATTER = new ThreadLocal<>();
    private static final ThreadLocal<SimpleDateFormat> TIMESTAMP_FORMATTER = new ThreadLocal<>();
    private static final long FIRST_JAN_1970 = 2440588;
    private static final int JAN1970 = 2440587; // FIXME this numbers should have been 2440588 not changing until
                                            // we evaluate the impact

    /**
     * Convert iso date to java.util.Calendar
     * example:
     *  isoDate = "20030101" -> return Calendar object
     *
     * @param isoDate
     * @return
     */
    public static java.util.Calendar iso2Calendar(String isoDate) throws
        ParseException {

        java.util.Calendar date = new java.util.GregorianCalendar();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        date.setTime(simpleDateFormat.parse(isoDate));

        return date;
    }


    public static String getGlobalPattern() {
        String pattern = FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);
        if (pattern == null){
            pattern = Constants.CALENDAR_DATE_FORMAT;
        }
        pattern = pattern.replace('m', 'M');
        return pattern;
    }
    
    /**
     * Formats date using pattern from global settings
     * @param date
     * @return
     */
    public static String formatDateLocalized(Date date){
        AmpDateFormatter formatter = AmpDateFormatterFactory.getLocalizedFormatter();
        return formatter.format(date);
    }
    
    public static String formatDate(Date date) {
        AmpDateFormatter formatter = AmpDateFormatterFactory.getDefaultFormatter();
        return formatter.format(date);
    }
    
    public static String formatDate(Date date, String pattern) {
        AmpDateFormatter formatter = AmpDateFormatterFactory.getDefaultFormatter(pattern);
        return formatter.format(date);
    }
    
    public static Date parseDate(String date) throws Exception{
        // TODO This should be in some other Utility class, FormatUtil may be, or just Util
        String pattern=FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);
        if (pattern==null){
            pattern=Constants.CALENDAR_DATE_FORMAT;
        }
        pattern = pattern.replace('m', 'M');
        SimpleDateFormat formater=new SimpleDateFormat(pattern);
        Date result = formater.parse(date);
        return result;
    }

    public static Date parseDateForPicker(String date) throws Exception{
        // TODO This should be in some other Utility class, FormatUtil may be, or just Util
        String pattern=FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);
        if (pattern==null){
            pattern=Constants.CALENDAR_DATE_PICKER;
        }
        // AMP-2828 by mouhamad
        pattern = pattern.replace("m", "M");
        
        ////System.out.println(pattern);
        SimpleDateFormat formater=new SimpleDateFormat(pattern);
        Date result=null;
        //if(date.contains("-")) date=date.replaceAll("-", "/");
                try{
                    result = formater.parse(date);
                }
                catch(Exception ex){
                    // temp solution
                    // TODO refactoring contracting dates
                    // AMP-2828 by mouhamad
                    SimpleDateFormat formaterCont=new SimpleDateFormat("yyyy-MM-dd");
                    result = formaterCont.parse(date);
                }
        return result;
    }

    public static String formatDateForPicker2(Date date, String format) {
        // TODO This should be in some other Utility class, FormatUtil may be, or just Util
        String pattern = null;
        if (format == null) {
            pattern = FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);
            if (pattern == null) {
                pattern = Constants.CALENDAR_DATE_PICKER;
            }
        } else {
            pattern = format;
        }
        // AMP-2828 by mouhamad
        pattern = pattern.replace("m", "M");
        
        SimpleDateFormat formater=new SimpleDateFormat(pattern);
        //if(date.contains("-")) date=date.replaceAll("-", "/");
                String result="";
                try{
        result = formater.format(date);
                }
                catch(Exception ex){
                    // temp solution
                    // TODO refactoring contracting dates
                    // AMP-2828 by mouhamad
                    SimpleDateFormat formaterCont=new SimpleDateFormat(pattern);
                    result = formaterCont.format(date);
                }
        return result;
    }

    /**
     * Converts Julian day number to default date format. Julian number can be null.
     *
     * @param julianNumber julian day number as string
     * @return date using default date format
     */
    public static String convertJulianNrToDefaultDateFormat(String julianNumber) {
        Date fromDateTrue = DateTimeUtil.fromJulianNumberToDate(julianNumber);
        if (fromDateTrue == null) {
            return null;
        }
        String defaultFormatPattern = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT);
        SimpleDateFormat format = new SimpleDateFormat(defaultFormatPattern);
        return format.format(fromDateTrue);
    }
        
    public static int toJulianDayNumber(LocalDate date) {
        return (int) (date.toEpochDay() + FIRST_JAN_1970);
    }

    public static Integer getYearFromJulianNumber(Long julianNumber) {
        return getYearFromJulianNumber(Long.toString(julianNumber));
    }

    public static Integer getYearFromJulianNumber(String julianNumber) {
        Date date = fromJulianNumberToDate(julianNumber);
        if (date != null) {
            return getLocalDate(date).getYear();
        }
        return null;
    }

    public static Date fromJulianNumberToDate(String julianNumber) {
        if (julianNumber != null && !FiltersConstants.FILTER_UNDEFINED_MAX.equals(julianNumber)) {
            try {
                int day = Integer.parseInt(julianNumber) - JAN1970;
                julianNumber = Integer.toString(day);
                Date date = new SimpleDateFormat("D").parse(julianNumber);

                return date;
            } catch (ParseException e) {
                logger.error("fromJulianNumberToDate", e);
            }
        }

        return null;
    }
    
    public static Integer toJulianDayNumber(Date date){
        if (date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int a = (14 - month) / 12;
        int y = year + 4800 - a;
        int m = month + 12 * a -3;
        int jDayNo = day + ((int)(153 * m + 2)/5) + 365 * y + ((int)(y/4)) - ((int)(y/100)) + ((int)(y/400)) - 32045;
        
        return jDayNo;
    }
    
    public static String toJulianDayString(Date date){
        return String.valueOf(toJulianDayNumber(date));
    }
    
    public static String formatDateOrNull(Date date, String pattern) {
        if (date == null) return null;
        if (pattern == null) return formatDate(date);
        return formatDate(date, pattern);
    }
    
    public static String formatDateOrNull(Date date) {
        return formatDateOrNull(date, null);
    }
    
    public static Date parseDate(String date, String pattern) {
        if (date == null) return null;
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
    
    public static SimpleDateFormat getStrictSimpleDateFormat(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(false);
        return sdf;
    }
    
    public static LocalDate getLocalDate(Date date) {
        return (new java.sql.Date(date.getTime())).toLocalDate();
    }

    /**
     * Returns a date representing first day of the year. Uses Gregorian calendar.
     */
    public static Date firstDayOfYear(int year) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * Returns a date representing last day of the year. Uses Gregorian calendar.
     */
    public static Date lastDayOfYear(int year) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year + 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }
    
    /**
     * Gets a date formatted in ISO 8601 date format. If the date is null, returns null.
     *
     * @param date the date to be formatted
     * @return String, date in ISO 8601 date format
     */
    public static String formatISO8601Date(Date date) {
        return formatISO8601DateTimestamp(date, false);
    }
    
    /**
     * Gets a date formatted in ISO 8601 date time format. If the date is null, returns null.
     *
     * @param date the date to be formatted
     * @return String, date in ISO 8601 date time format
     */
    public static String formatISO8601Timestamp(Date date) {
        return formatISO8601DateTimestamp(date, true);
    }
    
    /**
     * Gets a date formatted in ISO 8601 date time format. If the date is null, returns null.
     *
     * @param date the date to be formatted
     * @param isTimestamp if the value should be parsed using the ISO8601DateTime or ISO8601Date format
     * @return String, date in ISO 8601 date time format
     */
    public static String formatISO8601DateTimestamp(Date date, boolean isTimestamp) {
        SimpleDateFormat formatter = isTimestamp ? getTimestampFormatter() : getDateFormatter();
        return date == null ? null : formatter.format(date);
    }
    
    /**
     * Gets a date formatted in ISO 8601 date format. If the date is null, returns null.
     *
     * @param date the date to be formatted
     * @return String, date in ISO 8601 date format
     */
    
    public static Date parseISO8601Date(String date) {
        return parseISO8601DateTimestamp(date, false);
    }
    
    /**
     * Gets a date formatted in ISO 8601 date time format. If the date is null, returns null.
     *
     * @param date the date to be formatted
     * @return String, date in ISO 8601 date time format
     */
    
    public static Date parseISO8601Timestamp(String date) {
        return parseISO8601DateTimestamp(date, true);
    }
    
    /**
     * Gets a date formatted in ISO 8601 date time format. If the date is null, returns null.
     *
     * @param date the date to be formatted
     * @param isTimestamp if the value should be parsed using the ISO8601DateTime or ISO8601Date format
     * @return String, date in ISO 8601 date time format
     */
    
    public static Date parseISO8601DateTimestamp(String date, boolean isTimestamp) {
        try {
            SimpleDateFormat formatter = isTimestamp ? getTimestampFormatter() : getDateFormatter();
            if (date != null) {
                if (date.length() != EPConstants.DATE_FORMAT_STRICT_LENGTH.get(formatter.toPattern())) {
                    throw new ParseException("Unparseable date '" + date + "'", date.length());
                }
                return formatter.parse(date);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        
        return null;
    }
    
    private static SimpleDateFormat getDateFormatter() {
        if (DATE_FORMATTER.get() == null) {
            SimpleDateFormat format = new SimpleDateFormat(EPConstants.ISO8601_DATE_FORMAT);
            format.setLenient(false);
            DATE_FORMATTER.set(format);
        }
        
        return DATE_FORMATTER.get();
    }
    
    protected static SimpleDateFormat getTimestampFormatter() {
        if (TIMESTAMP_FORMATTER.get() == null) {
            SimpleDateFormat format = new SimpleDateFormat(EPConstants.ISO8601_DATE_AND_TIME_FORMAT);
            format.setLenient(false);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            TIMESTAMP_FORMATTER.set(format);
        }
        
        return TIMESTAMP_FORMATTER.get();
    }
    
}
