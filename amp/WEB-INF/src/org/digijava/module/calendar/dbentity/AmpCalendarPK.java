package org.digijava.module.calendar.dbentity;

import java.io.Serializable;

public class AmpCalendarPK
    implements Serializable {
  private Calendar calendar;

  public AmpCalendarPK() {

  }

  public AmpCalendarPK(Long ampCalendarId) {
    this.calendar = new Calendar();
    this.calendar.setId(ampCalendarId);
  }

  public AmpCalendarPK(Calendar calendar) {
    this.calendar = calendar;
  }

  public Calendar getCalendar() {
    return calendar;
  }

  public void setCalendar(Calendar calendar) {
    this.calendar = calendar;
  }

  public boolean equals(Object o) {
    if (! (o instanceof AmpCalendarPK)) {
      return false;
    }
    AmpCalendarPK otherAmpCalendarPK = (AmpCalendarPK) o;
    long id = this != null && this.calendar != null ?
        this.calendar.getId().longValue() : 0;
    long otherId = otherAmpCalendarPK != null &&
        otherAmpCalendarPK.calendar != null ?
        otherAmpCalendarPK.calendar.getId().longValue() : 0;
    return id == otherId;
  }

  public int hashCode() {
    return this != null && this.calendar != null ?
        this.calendar.getId().hashCode() : 0;
  }

  public java.util.Calendar getCalendar(java.util.Date date) {
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.setTime(date);
    return cal;
  }

  public int getStartMonth() {
    java.util.Calendar cal = getCalendar(getCalendar().getStartDate());
    return cal.get(java.util.Calendar.MONTH);
  }

  public int getEndMonth() {
    java.util.Calendar cal = getCalendar(getCalendar().getEndDate());
    return cal.get(java.util.Calendar.MONTH);
  }

  public int getStartDay() {
    java.util.Calendar cal = getCalendar(getCalendar().getStartDate());
    return cal.get(java.util.Calendar.DATE);
  }
  public int getStartYear() {
	    java.util.Calendar cal = getCalendar(getCalendar().getStartDate());
	    return cal.get(java.util.Calendar.YEAR);
	  }
  
  public int getEndYear() {
	    java.util.Calendar cal = getCalendar(getCalendar().getEndDate());
	    return cal.get(java.util.Calendar.YEAR);
	  }

  public int getEndDay() {
    java.util.Calendar cal = getCalendar(getCalendar().getEndDate());
    return cal.get(java.util.Calendar.DATE);
  }

  public int getStartHour() {
    java.util.Calendar cal = getCalendar(getCalendar().getStartDate());
    return cal.get(java.util.Calendar.HOUR_OF_DAY);
  }
  
  public int getEndHour() {
	    java.util.Calendar cal = getCalendar(getCalendar().getEndDate());
	    return cal.get(java.util.Calendar.HOUR_OF_DAY);
	  }
  
  public int getStartMinute(){
	  java.util.Calendar cal= getCalendar(getCalendar().getStartDate());
	  return cal.get(java.util.Calendar.MINUTE);
  }
  
  public int getEndMinute(){
	  java.util.Calendar cal= getCalendar(getCalendar().getEndDate());
	  return cal.get(java.util.Calendar.MINUTE);
  }
}
