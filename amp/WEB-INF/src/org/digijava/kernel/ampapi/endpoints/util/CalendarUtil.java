package org.digijava.kernel.ampapi.endpoints.util;

import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.fiscalcalendar.NepaliBasedWorker;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GregorianChronology;

import javax.ws.rs.core.Response.Status;
import java.util.*;
import java.util.stream.Collectors;

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
        if (calendarType.equals("ETH-CAL"))
            dt = shiftGregorianDate(year, fiscalCalendar.getStartMonthNum(), fiscalCalendar.getStartDayNum(), EthiopicChronology.getInstance(), 0, 0, 0);
        else if (calendarType.equals("GREG-CAL"))
            dt = shiftGregorianDate(year, fiscalCalendar.getStartMonthNum(), fiscalCalendar.getStartDayNum(), null, 0, 0, 0);
        else if (calendarType.equals("NEP-CAL"))
            dt = NepaliBasedWorker.fromGregorianToNepali(year, fiscalCalendar.getStartMonthNum(), fiscalCalendar.getStartDayNum());
        else 
            throw new RuntimeException("unknown calendar: " + calendarType); 
        
        if (!startDate) {
            dt = dt.plusYears(1);
            dt = dt.minusDays(1);
        }
        return dt.toDate();
    }

    /**
     * converts a date from Gregorian to any other calendar delta'ed by a given number of years/months/days.
     * Because of: 
     *  http://beust.com/weblog/2013/03/30/the-time-that-never-was/, 
     *  http://stackoverflow.com/questions/5451152/how-to-handle-jodatime-illegal-instant-due-to-time-zone-offset-transition,
     *  http://www.javased.com/?post=5451152,
     *   this hacky function has been written (technically 3 iterations would be enough, but you never know)
     * 
     * Original ticket: https://jira.dgfoundation.org/browse/AMP-20282
     * @param gregYear
     * @param gregMonth
     * @param gregDay
     * @param chronology: the Chronology to use. if null, then Gregorian would be used
     * @param deltaYear - the number of yrs to delta by
     * @param deltaMonth - the number of months to delta by
     * @param deltaDay - the number of days to delta by
     * @return
     */
    public static DateTime shiftGregorianDate(int gregYear, int gregMonth, int gregDay,
            Chronology chronology,
            int deltaYear, int deltaMonth, int deltaDay) {
        
        int hod = 3; // hour of day: timeshifts usually happen between 22:00 and 02:00, so this sounds like a safest first bet
        LocalDateTime ldt = new LocalDateTime(chronology == null ? GregorianChronology.getInstanceUTC() : chronology)
            .withYear(gregYear)
            .withMonthOfYear(gregMonth)
            .withDayOfMonth(gregDay)
            .plusYears(deltaYear)
            .plusMonths(deltaMonth)
            .plusDays(deltaDay);
        
        while(true) {
            try {
                return ldt.withHourOfDay(hod).toDateTime();
            }
            catch(Exception e) {
                hod ++;
                if (hod >= 22) {
                    // screwed up
                    throw new IllegalArgumentException(String.format("could not create a date for (gregYear, gregMonth, gregDay) = (%d, %d, %d)", gregYear, gregMonth, gregDay));
                }
            }
        }
    }
    
    public static List<AmpFiscalCalendar> getCalendars(List<Long> ids) {
        
        Session session = PersistenceManager.getSession();
        List<AmpFiscalCalendar> allCalendars = session.createCriteria(AmpFiscalCalendar.class)
                .addOrder(Order.asc("id"))
                .list();
        
        List<Long> allDbIds = allCalendars.stream()
                .map(AmpFiscalCalendar::getAmpFiscalCalId)
                .collect(Collectors.toList());
        
        List<Long> notFoundIds = new ArrayList<>(ids);
        notFoundIds.removeAll(allDbIds);
        
        if (!notFoundIds.isEmpty()) {
            String errorDetails = notFoundIds.stream().map(id -> String.valueOf(id)).collect(Collectors.joining(","));
            throw new ApiRuntimeException(Status.BAD_REQUEST, ApiError.toError("Wrong calendar id: " + errorDetails));
        }
        
        List<AmpFiscalCalendar> calendars = allCalendars.stream()
                .filter(cal -> ids.contains(cal.getAmpFiscalCalId()))
                .collect(Collectors.toList());
                
        return calendars;
    }
}
