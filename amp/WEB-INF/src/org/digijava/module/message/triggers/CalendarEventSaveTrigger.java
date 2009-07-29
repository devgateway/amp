package org.digijava.module.message.triggers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.message.helper.Event;

public class CalendarEventSaveTrigger extends Trigger {
	
	 public static final String PARAM_ID="id";
	 public static final String PARAM_NAME="name";
	 public static final String PARAM_URL="url";
	 public static final String SENDER="sender";
	 public static final String EVENT_START_DATE="start date";
	 public static final String EVENT_END_DATE="end date";
	 
	 public static final String [] parameterNames=new String[]{PARAM_ID,PARAM_NAME,PARAM_URL,SENDER,EVENT_START_DATE,EVENT_END_DATE};

	 public CalendarEventSaveTrigger(Object source) {
	        if(!(source instanceof AmpCalendar)) throw new RuntimeException("Incompatible object. Source must be an AmpCalendar!");
	        this.source=source;
	        forwardEvent();
	    }

	
	protected Event generateEvent() {
		 Event e=new Event(CalendarEventSaveTrigger.class);
	        AmpCalendar ampCalEvent=(AmpCalendar)source;
	        Calendar cal=null;
	        if(ampCalEvent.getCalendarPK()!=null &&
	           ampCalEvent.getCalendarPK().getCalendar()!=null){
	            cal=ampCalEvent.getCalendarPK().getCalendar();
	            CalendarItem ci=(CalendarItem)cal.getCalendarItem().iterator().next();
	            e.getParameters().put(PARAM_ID,cal.getId());
	            e.getParameters().put(PARAM_NAME,ci.getTitle());
	            e.getParameters().put(PARAM_URL,"calendar/showCalendarEvent.do~method=preview~resetForm=true~ampCalendarId="+cal.getId().toString());
	            e.getParameters().put(SENDER,ampCalEvent.getMember());
	            e.getParameters().put(EVENT_START_DATE, buildDateFromEvent(cal.getStartDate()));
	            e.getParameters().put(EVENT_END_DATE, buildDateFromEvent(cal.getEndDate()));
	        }
	    return e;
	}

	@Override
	public String[] getParameterNames() {
		  return parameterNames;
	}
	
	private String buildDateFromEvent(Date date){
        String pattern = FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);
        if (pattern == null) {
            pattern = "dd/MM/yyyy";
        }
        pattern+=" HH:mm";
        
        SimpleDateFormat formater=new SimpleDateFormat(pattern);
		String result = formater.format(date);
		return result;	
	}
}
