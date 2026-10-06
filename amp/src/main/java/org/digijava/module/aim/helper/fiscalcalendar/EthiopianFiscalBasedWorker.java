/**
 * EthiopianFiscalBasedWorker.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Feb 3, 2010
 */
package org.digijava.module.aim.helper.fiscalcalendar;

import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.chrono.EthiopicChronology;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * EthiopianFiscalBasedWorker
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Feb 3, 2010
 */
public class EthiopianFiscalBasedWorker implements ICalendarWorker {

    protected Map<Integer, ComparableMonth> monthCache = new HashMap<Integer, ComparableMonth>();

    private GregorianCalendar internalCalendar = null;

    private EthiopianCalendar internalEthiopianCalendar = null;

    private Date internalTime = null;
    
    private DateTime calendarDate = null;

    private AmpFiscalCalendar fiscalCalendar = null;

    public EthiopianFiscalBasedWorker(AmpFiscalCalendar calendar) {
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
        return internalEthiopianCalendar.ethFiscalQrt;
    }

    public Integer getYear() throws Exception {
        checkSetTimeCalled();
        return internalEthiopianCalendar.ethFiscalYear;
    }

    public void setTime(Date time) {
        internalTime = time;
        internalCalendar = new GregorianCalendar();
        internalCalendar.setTime(time);
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

    @Override
    public ComparableMonth getFiscalMonth() throws Exception {
        return getMonth();
    }

    @Override
    public String getFiscalYear(String prefix) throws Exception {
        if (this.fiscalCalendar.getIsFiscal()) {
            if (fiscalCalendar.getStartMonthNum() == 1) {
                return getFiscalPrefix(prefix) + " " + (this.getYear());
            } else {
                return getFiscalPrefix(prefix) + " " + (this.getYear()) + " - " + (this.getYear() + 1);
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
