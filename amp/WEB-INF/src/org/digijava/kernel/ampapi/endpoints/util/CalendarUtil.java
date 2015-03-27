package org.digijava.kernel.ampapi.endpoints.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.joda.time.DateTime;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GregorianChronology;

public class CalendarUtil {

	public static Date getStartDate(Long AmpFiscalCalendarId, int year) {
		AmpFiscalCalendar fiscalCalendar = null;
		if (AmpFiscalCalendarId != null) {
			fiscalCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(AmpFiscalCalendarId);
		}
		return getStartDate(fiscalCalendar, year);
	}

	public static Date getEndDate(Long AmpFiscalCalendarId, int year) {
		AmpFiscalCalendar fiscalCalendar = null;
		if (AmpFiscalCalendarId != null) {
			fiscalCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(AmpFiscalCalendarId);
		}
		return getEndDate(fiscalCalendar, year);
	}

	public static Date getStartDate(AmpFiscalCalendar calendar, int year) {
		Date startDate = null;
		if (calendar != null) {
			if (calendar.getBaseCal().equalsIgnoreCase("GREG-CAL")) {
				startDate = getStartOfYear(year, calendar.getStartMonthNum() - 1, calendar.getStartDayNum());
			} else {
				startDate = getGregorianCalendarDate(calendar, year, true);
			}
		} else {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.YEAR, year);
			startDate = cal.getTime();
		}
		return startDate;
	}

	public static Date getEndDate(AmpFiscalCalendar calendar, int year) {
		Date endDate = null;
		if (calendar != null) {
			if (calendar.getBaseCal().equalsIgnoreCase("GREG-CAL")) {
				// we need data including the last day of toYear,this is till the first day of toYear+1
				int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
				endDate = new Date(getStartOfYear(year + 1, calendar.getStartMonthNum() - 1, calendar.getStartDayNum())
						.getTime() - MILLISECONDS_IN_DAY);
			} else {
				endDate = getGregorianCalendarDate(calendar, year, false);
			}

		} else {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.YEAR, year + 1);
			endDate = cal.getTime();
		}
		return endDate;
	}

	public static Date getStartOfYear(int year, int month, int day) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(year, month, day, 0, 0, 0);
		return cal.getTime();
	}

	public static Date getGregorianCalendarDate(AmpFiscalCalendar fiscalCalendar, int year, boolean startDate) {
		return getCalendar(fiscalCalendar, startDate, year);
	}

	public static Date getCalendar(AmpFiscalCalendar fiscalCalendar, boolean startDate, int year) {
		DateTime dt = null;
		String calendarType = fiscalCalendar.getBaseCal();
		if (calendarType.equals("ETH-CAL")) {
			DateTime dtEth = new DateTime(year, fiscalCalendar.getStartMonthNum(), fiscalCalendar.getStartDayNum(), 0,
					0, 0, 0, GregorianChronology.getInstance());
			DateTime dt1 = dtEth.withChronology(EthiopicChronology.getInstance());
			dt = new DateTime();
			dt = dt.withDate(dt1.getYear(), dt1.getMonthOfYear(), dt1.getDayOfMonth());
		} else {
			if (calendarType.equals("NEP-CAL")) {
				dt = new DateTime(year, fiscalCalendar.getStartMonthNum(), fiscalCalendar.getStartDayNum(), 0, 0, 0, 0,
						GregorianChronology.getInstance());
				dt = dt.plusYears(56);
				dt = dt.plusMonths(8);
				dt = dt.plusDays(17); // this is to convert gregorian to nepali calendar
			} else
				dt = new DateTime(year, fiscalCalendar.getStartMonthNum(), fiscalCalendar.getStartDayNum(), 0, 0, 0, 0,
						GregorianChronology.getInstance());
		}
		if (!startDate) {
			dt = dt.plusYears(1);
			dt = dt.minusDays(1);
		}
		return dt.toDate();
	}

}
