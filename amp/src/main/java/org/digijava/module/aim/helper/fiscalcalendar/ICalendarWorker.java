package org.digijava.module.aim.helper.fiscalcalendar;

import org.joda.time.Chronology;
import org.joda.time.DateTime;

import java.util.Date;

public interface ICalendarWorker  {
    
    int YEAR_OFFSET_STRING = 5;
    
    /**
     * The date in the given calendar
     * 
     * @return
     * @throws Exception
     */
    public DateTime getCalendarDate();
    
    public Chronology getChronology();
    
    public Date getDate() throws Exception;
    
    /**
     * Get the the YEAR according to the current fiscal calendar configuration
     * 
     * @return Integer
     * @throws Exception
     */
    public Integer getYear() throws Exception;

    /**
     * Get the QUARTER according to the current fiscal calendar configuration
     * 
     * @return Integer
     * @throws Exception
     */
    public Integer getQuarter() throws Exception;

    /**
     * Get the MONTH according to the current fiscal calendar configuration
     * 
     * @return Comparable
     * @throws Exception
     */
    public ComparableMonth getMonth() throws Exception;

    /**
     * The the time, and apply the current fiscal calendar configuration
     * 
     * @param time
     */
    public void setTime(Date time);

    
    public Integer getYearDiff(ICalendarWorker worker) throws Exception;
    
    public String getFiscalYear(String prefix) throws Exception;
   
    public ComparableMonth getFiscalMonth() throws Exception;
    
    public default String getFiscalYear() throws Exception {
        return getFiscalYear(null);
    }
    
    public default String getFiscalPrefix(String prefix) {
        if (prefix == null)
            return getDefaultFiscalPrefix();
        return prefix;
    }
    
    public default String getDefaultFiscalPrefix() {
        return "Fiscal Year";
    }
    
    int parseYear(String year, String prefix);
    
}
