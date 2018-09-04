package org.digijava.module.aim.helper.fiscalcalendar;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.chrono.GregorianChronology;

public class GregorianBasedWorker implements ICalendarWorker {

    private DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();

    protected Map<Integer, ComparableMonth> monthCache = new HashMap<Integer, ComparableMonth>();

    private GregorianCalendar internalCalendar = null;

    private Date internalTime = null;
    
    private DateTime calendarDate = null;

    private AmpFiscalCalendar fiscalCalendar = null;

    private int fiscalMonth;

    public GregorianBasedWorker(AmpFiscalCalendar fiscalCalendar) {
        this.fiscalCalendar = fiscalCalendar;
    }

    public GregorianBasedWorker(Date date) {
        setTime(date);
    }

    public GregorianBasedWorker() {

    }

    public Date getDate() throws Exception {

        return internalCalendar.getTime();
    }

    public void setTime(Date time) {
        internalTime = time;
        internalCalendar = new GregorianCalendar();
        internalCalendar.setTime(time);
        fiscalMonth = internalCalendar.get(GregorianCalendar.MONTH);

        if (fiscalCalendar != null) {
            // set offset from fiscal calendar

            internalCalendar.add(GregorianCalendar.YEAR, fiscalCalendar.getYearOffset());
            int toAdd = (- fiscalCalendar.getStartMonthNum() + 1 );
            internalCalendar.add(GregorianCalendar.MONTH, toAdd);
            toAdd = (fiscalCalendar.getStartDayNum() - 1);
            internalCalendar.add(GregorianCalendar.DAY_OF_MONTH, 0-toAdd);
        }
        calendarDate = new DateTime(GregorianChronology.getInstance());
        calendarDate = calendarDate.withDate(internalCalendar.get(GregorianCalendar.YEAR),
                internalCalendar.get(GregorianCalendar.MONTH) + 1, internalCalendar.get(GregorianCalendar.DAY_OF_MONTH));
    }
    
    public DateTime getCalendarDate() {
        return calendarDate;
    }
    
    public Chronology getChronology() {
        return GregorianChronology.getInstance();
    }

    public ComparableMonth getMonth() throws Exception {
        checkSetTimeCalled();
        int monthId = internalCalendar.get(Calendar.MONTH);
        ComparableMonth cm = monthCache.get(monthId);

        if (cm == null) {
            String monthStr = dateFormatSymbols.getMonths()[internalTime.getMonth()];
            cm = new ComparableMonth(monthId, monthStr);
            monthCache.put(monthId, cm);
        }

        return cm;

    }

    public Integer getQuarter() throws Exception {
        checkSetTimeCalled();
        int month=internalCalendar.get(Calendar.MONTH);
        switch (month) {
        case Calendar.JANUARY:
        case Calendar.FEBRUARY:
        case Calendar.MARCH:
            return 1;
        case Calendar.APRIL:
        case Calendar.MAY:
        case Calendar.JUNE:
            return 2;
        case Calendar.JULY:
        case Calendar.AUGUST:
        case Calendar.SEPTEMBER:
            return 3;
        case Calendar.OCTOBER:
        case Calendar.NOVEMBER:
        case Calendar.DECEMBER:
            return 4;
        default:
            return -1;
        }
    }

    public Integer getYear() throws Exception {
        checkSetTimeCalled();
        return internalCalendar.get(Calendar.YEAR);
    }

    private void checkSetTimeCalled() throws Exception {
        if (internalTime == null)
            throw new Exception("Should call to setime first");
    }

    public Integer getYearDiff(ICalendarWorker worker) throws Exception {
        return this.getYear().intValue() - worker.getYear().intValue();
    }

    public ComparableMonth getFiscalMonth() throws Exception {
        checkSetTimeCalled();
        if (!this.fiscalCalendar.getIsFiscal()) {
            return getMonth();
        } else {

            ComparableMonth cm = monthCache.get(fiscalMonth);

            if (cm == null) {
                int actualFiscalMonth = internalCalendar.get(GregorianCalendar.MONTH);
                String monthStr = dateFormatSymbols.getMonths()[fiscalMonth];
                cm = new ComparableMonth(actualFiscalMonth, monthStr);
                monthCache.put(fiscalMonth, cm);
            }

            return cm;
        }
    }

    @Override
    public String getFiscalYear(String prefix) throws Exception {
        if (this.fiscalCalendar.getIsFiscal()) {
            if (fiscalCalendar.getStartMonthNum()==1){
                return getFiscalPrefix(prefix) + " " + (this.getYear());
            } else {
                return getFiscalPrefix(prefix) + " " + (this.getYear()) + " - " + (this.getYear()+1);
            }
        }
        return this.getYear().toString();
    }
    
    @Override
    public int parseYear(String year, String prefix) {
        String parsedYear = year;
        if (this.fiscalCalendar.getIsFiscal()) {
            parsedYear = year.substring(getFiscalPrefix(prefix).length() + 1, 
                    getFiscalPrefix(prefix).length() + YEAR_OFFSET_STRING);
        }
        
        return Integer.parseInt(parsedYear);
    }
    
}
