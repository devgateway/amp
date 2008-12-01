package org.digijava.module.calendar.dbentity;

import java.io.Serializable;
import java.util.Iterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

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
    
    public int getRecurrStartMonth() throws ParseException{
       int result = -1;

           if(!getCalendar().getRecurrCalEvent().isEmpty()){
             Iterator itr = getCalendar().getRecurrCalEvent().iterator();

             while (itr.hasNext()) {
      
                RecurrCalEvent rec = (RecurrCalEvent) itr.next();
                 if(rec.getRecurrStartDate() != null){
                    Date month  = stringToDate(rec.getRecurrStartDate());
                    result = getRecStrDate(month);


                }
              }
           }
        
        return result;
    }

    public int getRecStrDate(Date data){
        java.util.Calendar cal = getCalendar(data);
        return cal.get(java.util.Calendar.MONTH);
    }

    
    public int getRecurrEndMonth() throws ParseException{
           int result = -1;

               if(!getCalendar().getRecurrCalEvent().isEmpty()){
                 Iterator itr = getCalendar().getRecurrCalEvent().iterator();

                 while (itr.hasNext()) {

                    RecurrCalEvent rec = (RecurrCalEvent) itr.next();
                     if(rec.getRecurrEndDate() != null){
                        Date month  = stringToDate(rec.getRecurrEndDate());
                        result = getRecEndDate(month);


                    }
                  }
               }

            return result;
        }

        public int getRecEndDate(Date data){
            java.util.Calendar cal = getCalendar(data);
            return cal.get(java.util.Calendar.MONTH);
        }

    public int getRecurrStartYear() throws ParseException{
           int result = -1;

               if(!getCalendar().getRecurrCalEvent().isEmpty()){
                 Iterator itr = getCalendar().getRecurrCalEvent().iterator();

                 while (itr.hasNext()) {

                    RecurrCalEvent rec = (RecurrCalEvent) itr.next();
                     if(rec.getRecurrStartDate() != null){
                        Date year  = stringToDate(rec.getRecurrStartDate());
                        result = getRecStartDate(year);


                    }
                  }
               }

            return result;
        }

        public int getRecStartDate(Date data){
            java.util.Calendar cal = getCalendar(data);
            return cal.get(java.util.Calendar.YEAR);
        }

    public int getRecurrEndYear() throws ParseException{
               int result = -1;

                   if(!getCalendar().getRecurrCalEvent().isEmpty()){
                     Iterator itr = getCalendar().getRecurrCalEvent().iterator();

                     while (itr.hasNext()) {

                        RecurrCalEvent rec = (RecurrCalEvent) itr.next();
                         if(rec.getRecurrEndDate() != null){
                            Date year  = stringToDate(rec.getRecurrEndDate());
                            result = getReccEndDate(year);
                         }
                      }
                   }

                return result;
            }

            public int getReccEndDate(Date data){
                java.util.Calendar cal = getCalendar(data);
                return cal.get(java.util.Calendar.YEAR);
            }


     public int getRecurrStrDayOfmonth() throws ParseException{
               int result = -1;

                   if(!getCalendar().getRecurrCalEvent().isEmpty()){
                     Iterator itr = getCalendar().getRecurrCalEvent().iterator();

                     while (itr.hasNext()) {

                        RecurrCalEvent rec = (RecurrCalEvent) itr.next();
                         if(rec.getRecurrEndDate() != null){
                            Date day  = stringToDate(rec.getRecurrStartDate());
                            result = getReccSTRDayOfMonth(day);
                         }
                      }
                   }

                return result;
            }

            public int getReccSTRDayOfMonth(Date data){
                java.util.Calendar cal = getCalendar(data);
                return cal.get(java.util.Calendar.DAY_OF_MONTH);
            }



    public int getRecurrEndDayOfmonth() throws ParseException{
               int result = -1;

                   if(!getCalendar().getRecurrCalEvent().isEmpty()){
                     Iterator itr = getCalendar().getRecurrCalEvent().iterator();

                     while (itr.hasNext()) {

                        RecurrCalEvent rec = (RecurrCalEvent) itr.next();
                         if(rec.getRecurrEndDate() != null){
                            Date day  = stringToDate(rec.getRecurrEndDate());
                            result = getReccEndDayOfMonth(day);
                         }
                      }
                   }

                return result;
            }

            public int getReccEndDayOfMonth(Date data){
                java.util.Calendar cal = getCalendar(data);
                return cal.get(java.util.Calendar.DAY_OF_MONTH);
            }


    public Date stringToDate(String data) throws ParseException{
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date result = df.parse(data);
        return result;        
    }
}
