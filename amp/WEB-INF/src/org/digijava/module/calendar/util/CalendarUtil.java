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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.CalendarConverter;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.exception.NoCategoryClassException;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendarPK;
import org.digijava.module.calendar.dbentity.Calendar.TBD;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.dbentity.RecurrCalEvent;
import org.digijava.module.calendar.entity.AmpEventType;
import org.digijava.module.calendar.exception.CalendarException;
import org.digijava.module.calendar.form.CalendarItemForm;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * Class with static methods used for Data convertion and form population 
 */
public class CalendarUtil {
	protected static final String NUMBER_REGEX = "[0-9]+";
	protected static final Pattern pattern = Pattern.compile(NUMBER_REGEX);
	
	/**
	 * Converts java.util.Calendar to a String value considering TBD value.
	 * @param calendar
	 * @param tbd
	 * @return formated String
	 * @see #formatDate(Date, TBD)
	 */
	public static String formatDate(Calendar calendar, TBD tbd) {
		return formatDate(calendar.getTime(), tbd);
	}
	
	/**
	 * Converts java.util.Date to String taking into considration 
	 * the specifics of our event (Calendar) ie TBD and allDayEvent values
	 * @param date
	 * @param tbd
	 * @return formated String (eventually with "TBD") 
	 * @see org.digijava.module.calendar.dbentity.Calendar
	 * @see #formatDate(Calendar, TBD)
	 */
	public static String formatDate(Date date, TBD tbd) {
		StringBuffer buffer = new StringBuffer();
		if (tbd == TBD.YEAR) buffer.append("TBD");
		else if (tbd == TBD.MONTH) buffer.append("TBD ").append(new SimpleDateFormat("yyyy").format(date));
		else if (tbd == TBD.DAY) buffer.append("TBD ").append(new SimpleDateFormat("MMM yyyy").format(date));
		else if (tbd == TBD.HOUR) buffer.append("TBD ").append(new SimpleDateFormat("dd MMM yyyy").format(date));
		else if (tbd == TBD.MINUTE) buffer.append("TBD ").append(new SimpleDateFormat("HH, dd MMM yyyy").format(date));
		else {
			if (isAllDayEvent(date)) buffer.append(new SimpleDateFormat("dd MMM yyyy").format(date));
			else buffer.append(new SimpleDateFormat("HH:mm, dd MMM yyyy").format(date));
		}
			
		return buffer.toString();
	}
	
	
	/**
	 * This method populates CalendarItemForm with start date and end date 
	 * @param event
	 * @param form
	 * @see #populateDate(Date, CalendarItemForm.DateForm, TBD)
	 * @see CalendarItemForm
	 * @see CalendarItemForm.DateForm
	 */
	public static void populateDates(org.digijava.module.calendar.dbentity.Calendar event, CalendarItemForm form) {
		populateDate(event.getStartDate(),form.getStart(), event.getStartTBD());
		populateDate(event.getEndDate(), form.getEnd(), event.getEndTBD());
	}
	
	/**
	 * Populates an instance of CalendarItemForm.DateForm (start or end for CalendarItemForm)
	 * with year, month, date (day), hour, minute, allDayEvent values 
	 * @param date
	 * @param form
	 * @param tbd
	 */
	public static void populateDate(Date date, CalendarItemForm.DateForm form, TBD tbd) {
        Calendar calendar = Calendar.getInstance();
        
        calendar.setTime(date);
        if (tbd != TBD.DAY) form.setDate(Integer.toString(calendar.get(Calendar.DATE)));
        if (tbd != TBD.MONTH) form.setMonth(Integer.toString(calendar.get(Calendar.MONTH) + 1));
        if (tbd != TBD.YEAR) form.setYear(Integer.toString(calendar.get(Calendar.YEAR)));
        
        
        if (isAllDayEvent(calendar)) {
            form.setAllDayEvent(true);
        } else {
            form.setAllDayEvent(false);
            form.setHour(String.valueOf(calendar.get(Calendar.HOUR)));
            form.setMin(String.valueOf(calendar.get(Calendar.MINUTE)));
        }
		
	}
	
	
	
	/**
	 * Calculates whenever our date instance is considered all day event
	 * @param date
	 * @return true is date instance (for our logic) is all day event
	 * 		   false otherwise
	 * @see #isAllDayEvent(Calendar)
	 */
	
	public static boolean isAllDayEvent(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return isAllDayEvent(calendar);
	}
	
	
	
	/**
	 * We consider our calendar instance to be all day event if second == 0 (min) 
	 * @param calendar
	 * @return true is all day event (for our logic)
	 * 		   false otherwise
	 * @see #isAllDayEvent(Date)
	 */	
	public static boolean isAllDayEvent(Calendar calendar) {
		return calendar.get(Calendar.SECOND) == calendar.getActualMinimum(Calendar.SECOND);
	}
	
	public static boolean isCurrentDateBetweenEventsDates(AmpCalendarPK calendarPK,int day,int month,int year,int hour,int minute){    	
    	java.util.Calendar currentCalDate=java.util.Calendar.getInstance();
    	//iteration's date(current)
    	currentCalDate.set(year, month, day, hour, minute);
    	//CalendarPK's start Date
    	java.util.Calendar calPKStartDate=java.util.Calendar.getInstance();
    	calPKStartDate.set(calendarPK.getStartYear(), calendarPK.getStartMonth(), calendarPK.getStartDay(), calendarPK.getStartHour(), calendarPK.getStartMinute());
    	//CalendarPK's start Date
    	java.util.Calendar calPKEndDate=java.util.Calendar.getInstance();
    	calPKStartDate.set(calendarPK.getEndYear(), calendarPK.getEndMonth(), calendarPK.getEndDay(), calendarPK.getEndHour(), calendarPK.getEndMinute());    	
    	//compare if current date is between calendar event's start/end dates
    	return calPKStartDate.before(currentCalDate)&& currentCalDate.before(calPKEndDate);
    }
	
	public static Calendar buildCalendar(int day,int month,int year,int hour,int minute){
		java.util.Calendar CalDate=java.util.Calendar.getInstance();
    	//iteration's date(current)
		CalDate.set(year, month, day, hour, minute);		
		return CalDate;
	}
	
	public static String getCalendarEventsXml(AmpTeamMember member,Integer filter,String siteId,String[] selectedEventTypeIdsIds,String instanceId) throws ParseException{
		String xml="";
		Collection ampCalendarEvents=null;
		try {
			if (member==null) {
				ampCalendarEvents = AmpDbUtil.getAmpCalendarEventsPublic(2,selectedEventTypeIdsIds, null, null);
			} else if(selectedEventTypeIdsIds!=null && selectedEventTypeIdsIds.length>0){
				 ampCalendarEvents = AmpDbUtil.getAmpCalendarEventsByMember(member, filter,selectedEventTypeIdsIds, null, null);
			 }		  
			 
			//List events = DbUtil.getCalendarEvents(siteId, instanceId, userId);
			if(ampCalendarEvents!=null && !ampCalendarEvents.isEmpty()){
				Iterator iter = ampCalendarEvents.iterator();
				while (iter.hasNext()) {
					 AmpCalendar ampCalendar = (AmpCalendar) iter.next();
					 Iterable recc = ampCalendar.getCalendarPK().getCalendar().getRecurrCalEvent();
					 xml+="<event id=\"" +ampCalendar.getCalendarPK().getCalendar().getId()+"\">";
					 if(!ampCalendar.getCalendarPK().getCalendar().getRecurrCalEvent().isEmpty()){
						 xml+="<start_date>"+ampCalendar.getCalendarPK().getCalendar().getStartDate()+"</start_date>";
						 xml+="<end_date>"+ampCalendar.getCalendarPK().getRecurrEndDate()+"</end_date>";
					 }else{
						 xml+="<start_date>"+ampCalendar.getCalendarPK().getCalendar().getStartDate()+"</start_date>";
						 xml+="<end_date>"+ampCalendar.getCalendarPK().getCalendar().getEndDate()+"</end_date>";
					 }
					 Iterator itritm = ampCalendar.getCalendarPK().getCalendar().getCalendarItem().iterator();
					 while(itritm.hasNext()){
						 CalendarItem calItme = (CalendarItem) itritm.next();
						 xml+="<text>"+org.digijava.module.aim.util.DbUtil.filter(calItme.getTitle())+"</text>";
						 if(calItme.getDescription() != null){
							 xml+="<details>"+org.digijava.module.aim.util.DbUtil.filter(calItme.getDescription())+"</details>";
						 }else{
						 	xml+="<details>"+"No Description"+"</details>";
						 }
						 if(calItme.getApprove() != null){
							 xml+="<approve>"+calItme.getApprove()+"</approve>";
						 }else{
						 	xml+="<approve>"+"1"+"</approve>";
						 }
					}
										//AmpCalendar id = AmpDbUtil.getAmpCalendar(ampCalendar.getCalendarPK().getCalendar().getId());
										//id.getEventType().getId();
					if(ampCalendar.getEventsType() != null){
						xml+="<type>"+ampCalendar.getEventsType().getId()+"</type>";
					}else{
						xml+="<type>"+0+"</type>";
					}
					
					if(!ampCalendar.getCalendarPK().getCalendar().getRecurrCalEvent().isEmpty()){
						 Iterator itrrecc = ampCalendar.getCalendarPK().getCalendar().getRecurrCalEvent().iterator();
						 while(itrrecc.hasNext()){
							 RecurrCalEvent recurrCalEvent = (RecurrCalEvent) itrrecc.next(); 
							 if(recurrCalEvent.getTypeofOccurrence().equals("day")||recurrCalEvent.getTypeofOccurrence().equals("year")){
								 xml+="<rec_type>"+recurrCalEvent.getTypeofOccurrence()+"_"+recurrCalEvent.getRecurrPeriod() +"</rec_type>";
							 }else if(recurrCalEvent.getTypeofOccurrence().equals("month")){
								 xml+="<rec_type>"+recurrCalEvent.getTypeofOccurrence()+"_"+recurrCalEvent.getSelectedStartMonth()+"</rec_type>";
							 }else if(recurrCalEvent.getTypeofOccurrence().equals("week")){
								String weekdays = sortingWeekDays(recurrCalEvent.getOccurrWeekDays().toCharArray());
								xml+="<rec_type>"+recurrCalEvent.getTypeofOccurrence()+"_"+recurrCalEvent.getRecurrPeriod()+"___"+weekdays+"</rec_type>";
							 }
							 Date SartDate = ampCalendar.getCalendarPK().getCalendar().getStartDate();
							 Date EndDate = ampCalendar.getCalendarPK().getCalendar().getEndDate();
							 int  eventLengths = getEventlength(SartDate,EndDate);
							 xml+="<event_length>"+eventLengths/1000+"</event_length>";
						}
					}	
					xml+="</event>";	
				}
			}
		} catch (CalendarException e) {
			e.printStackTrace();
		}
		return xml;
	}
	


	public static String getEventTypesCss() throws CalendarException, NoCategoryClassException{
		String css = "";
		        
			List cs =  CategoryManagerUtil.getAmpEventColors();
			if(!cs.isEmpty()){
				
			Iterator iter = cs.iterator();		
				while(iter.hasNext()){
					
					AmpEventType item = (AmpEventType) iter.next();
					
					css+=".dhx_cal_event_line.event_"+item.getId()+"{";
					css+="background-color:"+item.getColor()+";}";
                    
                    css+=".dhx_cal_event_line.event_"+item.getId()+" span {";
					css+="color:white;}";
					
					css+=".dhx_cal_event.event_"+item.getId()+" div{";
					css+="background-color:"+item.getColor()+";";
					css+="color:white;}";
					
					css+=".dhx_cal_event_clear.event_"+item.getId()+"{";
                    css+="background-color:"+item.getColor()+";";
					css+="color:white;}";
					
                    // ========================
                    css+=".dhx_month_head.event_"+item.getId()+" {";
                    css+="background-color:"+item.getColor()+";";
					css+="color:white;}";
					
					
					
				}
				
				
			}
		return css;
	}
	public static String sortingWeekDays(char[] sortableWeekDays) {
	    String result = "";
	    char[] content = sortableWeekDays;
	    java.util.Arrays.sort(content);
	    for (int i=0; i<content.length; i++) {
	    	if(i!=0)
	    	result+= ","+content[i];
	    	else
	    		result+=content[i];
	    }
	    return result;
	}
	
	public static int getEventlength(Date startDate, Date endDate){
		
		int start = (int)startDate.getTime();
		int end = (int)endDate.getTime();
		int minus = (end-start);
		
		return minus;
	}
	
	/**
	 * Parses the string representation of the year back to a number
	 * @param calendar - calendar that generated the representation 
	 * @param year - the string representation previously provided by this same calendar
	 * @return year as a number
	 * @throws AMPException 
	 */
	public static int parseYear(CalendarConverter calendar, String year) throws AMPException {
		if (calendar.getIsFiscal()) {
			//Performs a simple match of the 1st number. 
			//If any calendar will do a very specific Fiscal Year representation, 
			//then we'll need it to customize, e.g. via a parseYear() method of the calendar worker 
			Matcher m = pattern.matcher(year);
			if (m.find())
				year = m.group();
			else 
				throw new AMPException(String.format("Invalid year format [%s] for the given Fiscal Calendar %s", year, calendar));
		}
		return Integer.parseInt(year);
	}
}
