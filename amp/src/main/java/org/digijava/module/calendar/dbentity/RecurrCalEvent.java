package org.digijava.module.calendar.dbentity;

import java.util.Date;

public class RecurrCalEvent {

    private Long id;
    private Calendar calendar;
    private String selectedStartMonth;
    private Long recurrPeriod;
    private String typeofOccurrence;
    private String occurrWeekDays;
    private Date recurrStartDate;
    private Date recurrEndDate;
    
    
    public String getSelectedStartMonth() {
        return selectedStartMonth;
    }
    public void setSelectedStartMonth(String selectedStartMonth) {
        this.selectedStartMonth = selectedStartMonth;
    }
    public Long getRecurrPeriod() {
        return recurrPeriod;
    }
    public void setRecurrPeriod(Long recurrPeriod) {
        this.recurrPeriod = recurrPeriod;
    }
    public String getTypeofOccurrence() {
        return typeofOccurrence;
    }
    public void setTypeofOccurrence(String typeofOccurrence) {
        this.typeofOccurrence = typeofOccurrence;
    }
    public String getOccurrWeekDays() {
        return occurrWeekDays;
    }
    public void setOccurrWeekDays(String occurrWeekDays) {
        this.occurrWeekDays = occurrWeekDays;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Calendar getCalendar() {
        return calendar;
    }
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Date getRecurrEndDate() {
        return recurrEndDate;
    }

    public void setRecurrEndDate(Date recurrEndDate) {
        this.recurrEndDate = recurrEndDate;
    }

    public Date getRecurrStartDate() {
        return recurrStartDate;
    }

    public void setRecurrStartDate(Date recurrStartDate) {
        this.recurrStartDate = recurrStartDate;
    }
}
