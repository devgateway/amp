/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.module.calendar.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.digijava.module.calendar.dbentity.Calendar.TBD;
import org.digijava.module.calendar.form.CalendarItemForm;
import org.digijava.module.calendar.form.CalendarItemForm.DateForm;

/**
 * This class returns start date and end date for given <code>CalendarItemForm</code> instance.
 * If some field is blank will consider min or max for actual value of respective Calendar field.
 * For example if day field for endDate is blank will put the last day of the month (31 for December).
 */
public  class CalendarPopulator {
    
    /** 
     * CalendarItemForm used to create start and end Calendars
     */
    private CalendarItemForm form = null;
    
    /**
     * Start Calendar (startDate)
     */
    private Calendar start = null;
    
    /**
     * End Calendar (endDate)
     */
    private Calendar end = null;
    
    
    /**
     * Simple interface for getting actual value of the field.
     * If it is not blank then it's value is set as Calendar's value.
     * If the field is blank will set the max or min of Calendar's actual value for specified field.
     * @see java.util.Calendar#getActualMinimum(int)
     * @see java.util.Calendar#getActualMaximum(int)
     */
    protected interface DefaultValueSetter {
        public Calendar setValue(Calendar calendar, int calendarField, String value);
    }
    
    /**
     * The value setter for StartDate
     */
    protected static class DefaultStartSetter implements DefaultValueSetter {
        public Calendar setValue(Calendar calendar, int calendarField, String value) {
            if (!StringUtils.isBlank(value)) calendar.set(calendarField, Integer.parseInt(value));
            else calendar.set(calendarField, calendar.getActualMinimum(calendarField));
            return calendar;
        }
    }
    
    /**
     * The value setter for EndDate 
     */
    protected static class DefaultEndSetter implements DefaultValueSetter {
        public Calendar setValue(Calendar calendar, int calendarField, String value) {
            if (!StringUtils.isBlank(value)) calendar.set(calendarField, Integer.parseInt(value));
            else calendar.set(calendarField, calendar.getActualMaximum(calendarField));
            return calendar;
        }
    }
    
    public CalendarPopulator(CalendarItemForm form) {
        this.form = form;
        init();
    }
    
    
    /**
     * Initializer for StartDate & EndDate.
     *
     */
    protected void init() {
        initStart();
        initEnd();
    }
    
    
    /**
     * This method creates an instance of java.util.Calendar and populates it
     * with some fields from Form. 
     * The initialized instance if for StartDate
     */
    protected void initStart() {
        Calendar calendar = new GregorianCalendar();
        populate(calendar, form.getStart(), new  DefaultStartSetter());
        this.start = calendar;
    }
    
    
    
    /**
     * Populating calendar instance from CalendarItemForm.DateForm
     * Just to differentiate allDayEvent we consider:
     *      second = 0 (min) if we have all day event
     *      second = 60 (max) if we don't have all day event
     * @param calendar
     * @param form
     * @param setter
     * @return changed calendar instance
     */
    protected Calendar populate(Calendar calendar, CalendarItemForm.DateForm form,  DefaultValueSetter setter) {
        if (!form.isAllDayEvent()) {
            setter.setValue(calendar, Calendar.SECOND, String.valueOf(calendar.getActualMaximum(Calendar.SECOND)));
            setter.setValue(calendar,Calendar.MINUTE, form.getMin());
            setter.setValue(calendar,Calendar.HOUR_OF_DAY, form.getHour());
        } else {
            // setting default values
            setter.setValue(calendar,Calendar.SECOND,String.valueOf(calendar.getActualMinimum(Calendar.SECOND)));
            setter.setValue(calendar,Calendar.MINUTE,null);
            setter.setValue(calendar,Calendar.HOUR_OF_DAY,null);
        }
        
        // I don't really like this solution, 
        // maybe we should consider months 0..11 (and not 1..12) this implies changing a lot of dependences?
        if (!StringUtils.isBlank(form.getMonth())) setter.setValue(calendar, Calendar.MONTH, String.valueOf(Integer.parseInt(form.getMonth()) -1)); 
            else setter.setValue(calendar, Calendar.MONTH, form.getMonth());
        setter.setValue(calendar, Calendar.DATE, form.getDate());
        setter.setValue(calendar, Calendar.YEAR, form.getYear());
        
        return calendar;

    }
    
    /**
     * This method creates an instance of java.util.Calendar and populates it
     * with some fields from Form. 
     * The initialized instance if for EndDate
     *
     */
    protected void initEnd() {
        Calendar calendar = new GregorianCalendar();
        populate(calendar,form.getEnd(), new DefaultEndSetter());
        this.end = calendar;
        
    }
    
    /**
     * Calculates TBD for this form
     * @param form
     * @return TBD instance
     *         null if we don't have any TBD field
     */
    private TBD getTBD(DateForm form) {
        if (StringUtils.isBlank(form.getYear())) return TBD.getTBD(Calendar.YEAR);
        if (StringUtils.isBlank(form.getMonth())) return TBD.getTBD(Calendar.MONTH);
        if (StringUtils.isBlank(form.getDate())) return TBD.getTBD(Calendar.DATE);
        if (StringUtils.isBlank(form.getHour())) return TBD.getTBD(Calendar.HOUR);
        if (StringUtils.isBlank(form.getMin())) return TBD.getTBD(Calendar.MINUTE);
        return null;
    }
    
    /**
     * @return startTBD
     */
    public TBD getStartTBD() {
        return getTBD(form.getStart());
    }
    
    /**
     * @return end TBD
     */
    public TBD getEndTBD() {
        return getTBD(form.getEnd());
    }
    
    /**
     * 
     * @return calendar instance for StartDate
     */
    public Calendar getStart() {
        return start;
    }
    
    /**
     * 
     * @return calendar instace for EndDate
     */
    public Calendar getEnd() {
        return end;
    }
    
    
    
}
