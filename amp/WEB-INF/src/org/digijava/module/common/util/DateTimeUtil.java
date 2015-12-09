/*
 *   DateTimeUtil.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Oct 20, 2003
 * 	 CVS-ID: $Id$
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
import java.util.Calendar;
import java.util.Date;

import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DateTimeUtil {

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


	/**
	 * Formats date using pattern from global settings
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date){
		// TODO This should be in some other Utility class, FormatUtil may be, or just Util
		String pattern=FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);
		if (pattern==null){
			pattern=Constants.CALENDAR_DATE_FORMAT;
		}
        pattern = pattern.replace('m', 'M');
		SimpleDateFormat formater=new SimpleDateFormat(pattern);
		String result = formater.format(date);

		return result;
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

	public static String parseDateForPicker2(Date date, String format) throws Exception{
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
	
	public static String formatDateOrNull(Date date) {
		if (date == null) return null;
		return formatDate(date);
	}
	
	public static Date parseDate(String date, String pattern) {
		try {
			return new SimpleDateFormat(pattern).parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

}
