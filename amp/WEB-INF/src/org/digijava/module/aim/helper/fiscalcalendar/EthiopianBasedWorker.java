package org.digijava.module.aim.helper.fiscalcalendar;

import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.chrono.EthiopicChronology;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class EthiopianBasedWorker implements ICalendarWorker {

    protected Map<Integer, ComparableMonth> monthCache = new HashMap<Integer, ComparableMonth>();

    private GregorianCalendar internalCalendar = null;

    private EthiopianCalendar internalEthiopianCalendar = null;
    
    private int fiscalMonth;
    
    private Date internalTime = null;
    
    private DateTime calendarDate = null;

    private AmpFiscalCalendar fiscalCalendar = null;

    public EthiopianBasedWorker(AmpFiscalCalendar calendar) {
        this.fiscalCalendar = calendar;
    }

    public Date getDate() throws Exception {
        return internalCalendar.getTime();
    }

    public ComparableMonth getMonth() throws Exception {
        checkSetTimeCalled();
        int monthId = internalEthiopianCalendar.ethMonth;
        ComparableMonth cm = monthCache.get(monthId);
        if (cm == null) {
            String monthStr = internalEthiopianCalendar.ethMonthName;
            cm = new ComparableMonth(monthId, monthStr);
            monthCache.put(monthId, cm);
        }
        return cm;
    }

    public Integer getQuarter() throws Exception {
        checkSetTimeCalled();
        return internalEthiopianCalendar.ethQtr;
    }

    public Integer getYear() throws Exception {
        checkSetTimeCalled();
        return internalEthiopianCalendar.ethYear;
    }

    public void setTime(Date time) {
        internalTime = time;
        
        this.fiscalMonth=EthiopianCalendar.getEthiopianDate(time).ethMonth;
        
        internalCalendar = new GregorianCalendar();
        internalCalendar.setTime(time);
        fiscalMonth = internalCalendar.get(GregorianCalendar.MONTH);
            
        
        // set offset from fiscal calendar
        internalCalendar.add(GregorianCalendar.YEAR, fiscalCalendar.getYearOffset());
        int toAdd = -(fiscalCalendar.getStartMonthNum() - 1);
        internalCalendar.add(GregorianCalendar.MONTH, toAdd);
        toAdd = -(fiscalCalendar.getStartDayNum() - 1);
        internalCalendar.add(GregorianCalendar.DAY_OF_MONTH, toAdd);
        internalEthiopianCalendar = EthiopianCalendar.getEthiopianDate(internalCalendar);
        
        calendarDate = new DateTime(EthiopicChronology.getInstance());
        calendarDate = calendarDate.withDate(internalEthiopianCalendar.ethYear, 
                internalEthiopianCalendar.ethMonth, internalEthiopianCalendar.ethDay);
    }
    
    public DateTime getCalendarDate() {
        return calendarDate;
    }
    
    public Chronology getChronology() {
        return EthiopicChronology.getInstance();
    }

    private void checkSetTimeCalled() throws Exception {
        if (internalTime == null)
            throw new Exception("Should call to setime first");
    }
    
    
    public Integer getYearDiff(ICalendarWorker worker) throws Exception {
            return this.getYear().intValue() - worker.getYear().intValue();
        
    }

    public String getFiscalYearLabel() throws Exception {
        // TODO Auto-generated method stub
        return this.getYear().toString();
    }

    @Override
    public ComparableMonth getFiscalMonth() throws Exception {
        checkSetTimeCalled();
        if (!this.fiscalCalendar.getIsFiscal()) {
            return getMonth();
        } else {
            checkSetTimeCalled();
            int monthId = internalEthiopianCalendar.ethMonth;
            ComparableMonth cm = monthCache.get(monthId);
            if (cm == null) {
                String monthStr = internalEthiopianCalendar.ethMonthName;
                cm = new ComparableMonth(monthId, monthStr);
                monthCache.put(monthId, cm);
            }
            return cm;
            
        }
    }

    @Override
    public String getFiscalYear(String prefix) throws Exception {
        if (this.fiscalCalendar.getIsFiscal()) {
            return getFiscalPrefix(prefix) + "," + this.getYear() + " - " + (this.getYear() + 1);
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
