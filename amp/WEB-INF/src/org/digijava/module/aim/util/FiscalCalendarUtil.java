package org.digijava.module.aim.util;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.CalendarConverter;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.helper.fiscalcalendar.ICalendarWorker;
import org.hibernate.Session;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.chrono.GregorianChronology;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class FiscalCalendarUtil {
    
    private static Logger logger = Logger.getLogger(FiscalCalendarUtil.class);
    
    
    public static Date addToDate(Date dfromDate, Integer amount, Integer calendarPeriod){
        if(amount == null){
            amount = 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dfromDate);
        cal.add(calendarPeriod, amount);
        return cal.getTime();
    }
    
    public static Date getCalendarEndDateForCurrentYear(Long id) {
        Integer year = getCurrentYear();
        Date endDate = getCalendarEndDate(id, year);
        return endDate;
    }

    public static Date getFirstDateOfCurrentMonth(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        return cal.getTime();
    }

    public static Date getLastDateOfCurrentMonth(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static Date getCurrentDate(){
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }
    
    public static Integer getCurrentYear(){
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }
    
    public static Date getCalendarStartDateForCurrentYear(Long id) {
        Integer year = getCurrentYear();
        return getCalendarStartDate(id, year);
    }
    
    public static Date getCalendarStartDate(Long id,int year) {
        Date d = null;
        Session session = null;
        
        try {
            session = PersistenceManager.getSession();
            AmpFiscalCalendar fisCal = (AmpFiscalCalendar) session.get(AmpFiscalCalendar.class,
                    id);
            
            year += fisCal.getYearOffset().intValue();
            
            String stDate = fisCal.getStartDayNum() + "/" + fisCal.getStartMonthNum() + "/" + year;

            // quick fix, because of Global settings  date format...
            String pattern = "dd/MM/yyyy";
            d = new SimpleDateFormat(pattern).parse(stDate);

        } catch (Exception e) {
            logger.error("Exception from getAmpFiscalCalendar() :" + e.getMessage());
            e.printStackTrace(System.out);
        }
        return d;       
    }
    
    public static Date getCalendarEndDate(Long id,int year) {
        Date d = null;
        Session session = null;
        
        try {
            session = PersistenceManager.getSession();
            AmpFiscalCalendar fisCal = (AmpFiscalCalendar) session.get(AmpFiscalCalendar.class,
                    id);
            String stDate = null;
            boolean addYear = false;
            if (fisCal.getStartDayNum().intValue() == 1 && fisCal.getStartMonthNum().intValue() == 1) {
                stDate = fisCal.getStartDayNum() + "/" + fisCal.getStartMonthNum() + "/" + (year + 1);
            } else { 
                stDate = fisCal.getStartDayNum() + "/" + fisCal.getStartMonthNum() + "/" + (year);
                addYear = true;
            }
             // quick fix, because of Global settings  date format...
            String pattern = "dd/MM/yyyy";
            d = new SimpleDateFormat(pattern).parse(stDate);
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(d);
            if (addYear == true) {
                //Without this the endDate was before the endDate :(
                gc.add(Calendar.YEAR,1);
            }
            gc.add(Calendar.DATE, -1);
            d = gc.getTime();
        } catch (Exception e) {
            logger.error("Exception from getAmpFiscalCalendar() :" + e.getMessage());
            e.printStackTrace(System.out);
        }
        return d;       
    }   
    
    public static AmpFiscalCalendar getAmpFiscalCalendar(Long id) {
        Session session = null;
        AmpFiscalCalendar fisCal = null;
        
        try {
            session = PersistenceManager.getSession();
            fisCal = (AmpFiscalCalendar) session.get(AmpFiscalCalendar.class,
                    id);
        } catch (Exception e) {
            logger.error("Exception from getAmpFiscalCalendar() :" + e.getMessage());
            e.printStackTrace(System.out);
        }
        return fisCal;
    }
    
    public static List<AmpFiscalCalendar> getAllAmpFiscalCalendars(){
        Session session = null;
        List<AmpFiscalCalendar> list = null;        
        try {
            session = PersistenceManager.getSession();
            list = session.createQuery("from " + AmpFiscalCalendar.class.getName()).list();            
        } catch (Exception e) {
            logger.error("Exception from getAllAmpFiscalCalendars() :" + e.getMessage());            
        }
        return list;
    }
    
    public static AmpFiscalCalendar getAmpFiscalCalendar(String calendarName) {
        try {
            return (AmpFiscalCalendar) PersistenceManager.getSession().createQuery("select o from " + AmpFiscalCalendar.class.getName() 
                    + " o where o.name like '" + calendarName + "'").uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
    
    public static int getYear(Long fisCalId,String date) {
        Session session = null;
        int fiscalYr = 0;
        
        try {
            session = PersistenceManager.getSession();
            AmpFiscalCalendar fisCal = (AmpFiscalCalendar) session.get(AmpFiscalCalendar.class,fisCalId);
            int year = DateConversion.getYear(date);
            int stDay = fisCal.getStartDayNum().intValue();
            int stMnt = fisCal.getStartMonthNum().intValue();
            String day="", month="";
            if(stDay>9)
                 day=fisCal.getStartDayNum().toString();
            else day="0"+fisCal.getStartDayNum().toString();
            if(stMnt>9)
                 month=fisCal.getStartMonthNum().toString();
            else month="0"+fisCal.getStartMonthNum().toString();
            String bsDate =  day+ "/" + month + "/" + year;
            Date baseDate = DateConversion.getDate(bsDate); 
            Date transDate = DateConversion.getDate(date);

            if (transDate.after(baseDate) || transDate.equals(baseDate) ||
                    (stDay == 1 && stMnt == 1)) {
                fiscalYr = year;    
            } else {
                fiscalYr = year-1;
            }
            
        } catch (Exception e) {
            logger.error("Exception from getYear() :" + e.getMessage());
            e.printStackTrace(System.out);
        }
        return fiscalYr;
    }
    
    private static Integer getYearOnCalendar(AmpFiscalCalendar calendar, Integer pyear, AmpFiscalCalendar defCalendar) {
        if (pyear == null) 
            return 0;
        
        Integer year = null;
        try {
            Date testDate = new SimpleDateFormat("dd/MM/yyyy").parse("11/09/"+pyear);
            ICalendarWorker work1 = defCalendar.getworker();
            work1.setTime(testDate);
            ICalendarWorker work2 = calendar.getworker();
            work2.setTime(testDate);
            int diff = work2.getYearDiff(work1);
            pyear = pyear + diff;
            return pyear;
        } catch (Exception e) {
            logger.error("Can't get year on calendar",e);
        }
        return year;
    
    }
    
    public static Integer getYearOnCalendar(Long calendarId, Integer pyear, AmpApplicationSettings  tempSettings ) {
        if (pyear == null)
            return 0;
        
        
        AmpFiscalCalendar cal = FiscalCalendarUtil.getAmpFiscalCalendar(calendarId);
        
        AmpFiscalCalendar defauCalendar = null;
        if  (tempSettings != null)
            defauCalendar = tempSettings.getFiscalCalendar();
            
        if (defauCalendar == null){
            String calValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
            defauCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(Long.parseLong(calValue));
        }
            
        if (defauCalendar == null) 
            return pyear; //TODO-CONSTANTIN: was zero here
        
        Integer year = getYearOnCalendar(cal, pyear, defauCalendar);
        return year;
    }
    
    /**
     * 
     * @param calendarId
     * @param year
     * @return
     */
    public static DateTime convertDate(Long fromCalendarId, Date fromDate, Long toCalendarId) {
        return convertDate(FiscalCalendarUtil.getAmpFiscalCalendar(fromCalendarId), fromDate, 
                FiscalCalendarUtil.getAmpFiscalCalendar(toCalendarId));
    }
    
    /**
     * 
     * @param fromCalendar
     * @param fromDate
     * @param toCalendar
     * @return
     */
    public static DateTime convertDate(AmpFiscalCalendar fromCalendar, Date fromDate, AmpFiscalCalendar toCalendar) {
        DateTime dateTime = null;
        if (fromCalendar == null || fromDate == null || toCalendar == null || fromCalendar == toCalendar) {
            dateTime = fromCalendar == null ? toDateTime(fromDate) : 
                toDateTime(fromDate, fromCalendar.getworker().getChronology());
        } else {
            Date gregDate = toGregorianDate(fromDate, fromCalendar);
            ICalendarWorker toCalWorker = toCalendar.getworker();
            toCalWorker.setTime(gregDate);
            dateTime = toCalWorker.getCalendarDate();
        }
        return dateTime;
    }
    
    /**
     * A method to convert a Gregorian date into toCalendar date
     * 
     * @param gregorianDate
     * @param toCalendar
     * @return
     */
    public static DateTime convertFromGregorianDate(Date gregorianDate, AmpFiscalCalendar toCalendar) {
        
        ICalendarWorker toCalWorker = toCalendar.getworker();
        toCalWorker.setTime(gregorianDate);
        
        return toCalWorker.getCalendarDate();
    }
    
    /**
     * 
     * Note: Since no general solution existed so far for so many years, agreed on this quick solution to reduce 
     * conversion bugs and it will be redesign as part of migration to Java8 and new Reports Engine (after Mondrian era)  
     * 
     * @param date
     * @param fromCalendar
     * @return
     */
    public static Date toGregorianDate(Date date, AmpFiscalCalendar fromCalendar) {
        ICalendarWorker worker = fromCalendar.getworker();
        
        Calendar fromDate = Calendar.getInstance();
        fromDate.setTime(date);
        
        Calendar cal = Calendar.getInstance();
        worker.setTime(cal.getTime());
        
        int deltaYears = cal.get(Calendar.YEAR) - worker.getCalendarDate().getYear();
        fromDate.add(Calendar.YEAR, deltaYears);
        
        int deltaDays = cal.get(Calendar.DAY_OF_YEAR) - worker.getCalendarDate().getDayOfYear();
        fromDate.add(Calendar.DAY_OF_YEAR, deltaDays);
        
        return fromDate.getTime();
    }
    
    public static DateTime toDateTime(Date date) {
        return toDateTime(date, GregorianChronology.getInstance());
    }
        
    public static DateTime toDateTime(Date date, Chronology chronology) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        DateTime dateTime = new DateTime(chronology);
        dateTime = dateTime.withDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        // clear any HH:mm:ss
        dateTime = dateTime.minusMillis(dateTime.getMillisOfDay());
        return dateTime;
    }
    
    public static Date toGregorianDate(AmpFiscalCalendar fromCal, Integer year, int daysOffset) {
        return getDateTime(fromCal, year, daysOffset, null).toDate();
    }
    
    public static Integer getActualGregorianYear(AmpFiscalCalendar fromCal, Integer year, int daysOffset) {
        return getActualYear(fromCal, year, daysOffset, null);
    }
    
    public static Integer getActualYear(AmpFiscalCalendar fromCal, Integer year, int daysOffset, 
            AmpFiscalCalendar toCal) {
        return getDateTime(fromCal, year, daysOffset, toCal).getYear();
    }
    
    /**
     * 
     * @param fromCal
     * @param year
     * @param daysOffset
     * @param toCal or null if to convert to Gregorian calendar
     * @return
     */
    private static DateTime getDateTime(AmpFiscalCalendar fromCal, Integer year, int daysOffset, 
            AmpFiscalCalendar toCal) {
        Calendar tmpCal = Calendar.getInstance();
        tmpCal.set(year, 0, 1, 0, 0, 0);
        
        if (fromCal == null) {
            tmpCal.add(Calendar.DAY_OF_YEAR, daysOffset);
            return toDateTime(tmpCal.getTime());
        }
        
        tmpCal.setTime(FiscalCalendarUtil.toGregorianDate(tmpCal.getTime(), fromCal));
        
        if (toCal == null) {
            tmpCal.add(Calendar.DAY_OF_YEAR, daysOffset);
            return toDateTime(tmpCal.getTime());
        } else {
            ICalendarWorker calWorker = toCal.getworker();
            calWorker.setTime(tmpCal.getTime());
            DateTime startYearDate = calWorker.getCalendarDate();
            startYearDate.plusDays(daysOffset);
            return startYearDate;
        }
    }
    
    public static boolean isEthiopianCalendar(CalendarConverter calendarConverter) {
        if (calendarConverter != null && calendarConverter instanceof AmpFiscalCalendar) {
            AmpFiscalCalendar calendar = (AmpFiscalCalendar) calendarConverter;
            return calendar.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN.getValue());
        }
        
        return false;
    }
    
    public static AmpFiscalCalendar getGSCalendar() {
        Long calendarCode = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_CALENDAR);
        return getAmpFiscalCalendar(calendarCode);
    }
}
