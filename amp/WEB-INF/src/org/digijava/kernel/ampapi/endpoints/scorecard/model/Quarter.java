package org.digijava.kernel.ampapi.endpoints.scorecard.model;

import java.util.Calendar;
import java.util.Date;

import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.fiscalcalendar.ICalendarWorker;

/**
 * Representation of a quarter (3 month period)
 * 
 * @author Emanuel Perez
 *
 */
public class Quarter {

    private Integer year;
    private String yearCode;
    private Integer quarterNumber;
    private AmpFiscalCalendar fiscalCalendar;

    public Quarter(AmpFiscalCalendar calendar, Date date) {
        this.fiscalCalendar = calendar;
        ICalendarWorker worker = calendar.getworker();
        worker.setTime(date);
        try {
            this.quarterNumber = worker.getQuarter();
            this.year = worker.getYear();
            this.yearCode = "" + worker.getYear();
            if (calendar.getStartMonthNum() != 1) {
                this.yearCode += "-" + (worker.getYear() + 1);
            }
        } catch (Exception e) {
            throw new RuntimeException ("Couldn't get quarter for date: " + date);
        }

    }

    public Quarter(AmpFiscalCalendar calendar, Integer quarterNumber, Integer year) {
        this.fiscalCalendar = calendar;
        this.quarterNumber = quarterNumber;
        this.year = year;
        this.yearCode = "" + year;
        if (calendar.getStartMonthNum() != 1) {
            this.yearCode += "-" + (year + 1);
        }
    }

    public Integer getQuarterNumber() {
        return quarterNumber;
    }

    public void setQuarterNumber(Integer quarterNumber) {
        this.quarterNumber = quarterNumber;
    }

    @Override
    public String toString() {
        return yearCode + " " + "Q" + quarterNumber;
    }

    /**
     * Gets the quarter before the current one
     * 
     * @return Quarter, the previous quarter
     */
    public Quarter getPreviousQuarter() {
        Quarter quarter;
        if (quarterNumber > 1) {
            quarter = new Quarter(fiscalCalendar, Integer.valueOf(quarterNumber - 1), year);
        } else {
            // the current object is the first quarter, so the previous quarter
            // is the fourth quarter of last year
            quarter = new Quarter(fiscalCalendar, 4, (year - 1));
        }
        return quarter;
    }
    /**
     * We search for the first quarter of the year
     * @return
     */
    
    public Quarter getFirstQuarter(){
        if (quarterNumber == 1){
            return this;
        }else {
            return (new Quarter(fiscalCalendar, Integer.valueOf(quarterNumber - 1), year)).getFirstQuarter();
        }
    }

    /**
     * Gets the date on which the current quarter starts
     * 
     * @return the date on which the current quarter starts
     */
    public Date getQuarterStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, fiscalCalendar.getStartDayNum());
        //fiscal Calendar month are 1 based, but Java is 0 based.
        //from the starting month, we add 3 months X number of quarters
        calendar.set(Calendar.MONTH, (fiscalCalendar.getStartMonthNum() - 1) + (quarterNumber - 1) * 3);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    
    /**
     * Gets the date on which the current quarter ends
     * 
     * @return the date on which the current quarter ends
     */
    public Date getQuarterEndDate () {
        Date start = getQuarterStartDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.MONTH, 3);
        calendar.add(Calendar.SECOND, -1);
        //it adds 90 days to the start of the quarter, and substracts 1 secs so time is 23:59:59
        return calendar.getTime();
    }

    public String getYearCode() {
        return yearCode;
    }

    public void setYearCode(String yearCode) {
        this.yearCode = yearCode;
    }
}
