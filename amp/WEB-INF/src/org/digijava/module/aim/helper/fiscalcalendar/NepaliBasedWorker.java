package org.digijava.module.aim.helper.fiscalcalendar;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.util.CalendarUtil;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.chrono.GregorianChronology;

public class NepaliBasedWorker implements ICalendarWorker {

    protected Map<Integer, ComparableMonth> monthCache = new HashMap<Integer, ComparableMonth>();

    private AmpFiscalCalendar fiscalCalendar = null;

    private DateTime nepaliCalendar;

    public NepaliBasedWorker(AmpFiscalCalendar fiscalCalendar) {
        this.fiscalCalendar = fiscalCalendar;
    }

    public Date getDate() throws Exception {
        throw new Exception("Not allowed on this calendar");
    }

    public void setTime(Date time) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(time);
        int gregYear = cal.get(Calendar.YEAR);
        int gregMonth = cal.get(Calendar.MONTH) + 1;
        int gregDay = cal.get(Calendar.DAY_OF_MONTH);

        if (fiscalCalendar != null) {
            nepaliCalendar = fromGregorianToNepali(gregYear, gregMonth, gregDay);
        }
    }
    
    /**
     * translates a year from Gregorian to Nepali
     * @param gregYear
     * @param gregMonth
     * @param gregDay
     * @return
     */
    public static DateTime fromGregorianToNepali(int gregYear, int gregMonth, int gregDay) {
        return CalendarUtil.shiftGregorianDate(gregYear, gregMonth, gregDay, null, 56, 8, 17);
    }
    
    public DateTime getCalendarDate() {
        return nepaliCalendar;
    }
    
    public Chronology getChronology() {
        return GregorianChronology.getInstance();
    }
    
    /*public Comparable getMonth() throws Exception {
        return this.getMonthName(nepaliCalendar.getMonth()) +"-"+ nepaliCalendar.getMonth();
    }*/

    public Integer getQuarter() throws Exception {
        return getQuarter(nepaliCalendar.getMonthOfYear());
    }

    public Integer getYear() throws Exception {
        return nepaliCalendar.getYear();
    }

    public Integer getDay() throws Exception {
        return nepaliCalendar.getDayOfMonth();
    }

    public Integer getMonthNumber() throws Exception {
        return nepaliCalendar.getMonthOfYear();
    }

    public Integer getDayOfWeek() throws Exception {
        return nepaliCalendar.getDayOfWeek();
    }

    private void checkSetTimeCalled() throws Exception {
    }

    public Integer getYearDiff(ICalendarWorker worker) throws Exception {
        return this.getYear().intValue() - worker.getYear().intValue();
    }

    public ComparableMonth getMonth() throws Exception {
        if (!this.fiscalCalendar.getIsFiscal()) {
            return new ComparableMonth(nepaliCalendar.getMonthOfYear(), this.getMonthName(nepaliCalendar.getMonthOfYear()));
        } else {
            checkSetTimeCalled();
            int monthId = nepaliCalendar.getMonthOfYear();
            ComparableMonth cm = monthCache.get(monthId);
            if (cm == null) {
                String monthStr = this.getMonthName(nepaliCalendar.getMonthOfYear());
                cm = new ComparableMonth(monthId, monthStr);
                monthCache.put(monthId, cm);
            }
            return cm;
        }
        //return getMonthName(nepaliCalendar.getMonth());
    }
    
    @Override
    public ComparableMonth getFiscalMonth() throws Exception {
        return getMonth();
    }

    @Override
    public String getFiscalYear(String prefix) throws Exception {
        if (this.fiscalCalendar.getIsFiscal()) {
            return getFiscalPrefix(prefix) + "," + this.getYear() + " - " + (this.getYear() + 1);
        }
        return this.getYear().toString();
    }

    private String getMonthName(int month) throws Exception {
        String ret = "";
        switch (month) {
        case 1:
            ret = "Baishak";
            break;
        case 2:
            ret = "Jestha";
            break;
        case 3:
            ret = "Ashad";
            break;
        case 4:
            ret = "Shrawn";
            break;
        case 5:
            ret = "Bhadra";
            break;
        case 6:
            ret = "Ashwin";
            break;
        case 7:
            ret = "Kartik";
            break;
        case 8:
            ret = "Mangshir";
            break;
        case 9:
            ret = "Poush";
            break;
        case 10:
            ret = "Magh";
            break;
        case 11:
            ret = "Falgun";
            break;
        case 12:
            ret = "Chaitra";
            break;
        default:
            ret = null;
            throw new Exception("Error, incorrect month");
        }
        return ret;
    }

    private Integer getQuarter(int month) {
        switch (month) {
        case 1:
        case 2:
        case 3:
            return 1;
        case 4:
        case 5:
        case 6:
            return 2;
        case 7:
        case 8:
        case 9:
            return 3;
        case 10:
        case 11:
        case 12:
            return 4;
        default:
            return -1;
        }
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
