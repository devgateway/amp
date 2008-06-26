package org.digijava.module.message.helper;

import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;

public class CalendarEventTrigger extends Trigger {

    public static final String PARAM_NAME="name";
    public static final String PARAM_URL="url";

    public static final String [] parameterNames=new String[]{PARAM_NAME,PARAM_URL};

    public CalendarEventTrigger(Object source) {
        if(!(source instanceof Calendar)) throw new RuntimeException("Incompatible object. Source must be an AmpCalendar!");
        this.source=source;
        forwardEvent();
    }

    /** @see org.digijava.module.message.helper.Trigger#generateEvent(java.lang.Object)
     */
    @Override
    protected Event generateEvent() {
        Event e=new Event(CalendarEventTrigger.class);
        Calendar calEvent=(Calendar)source;
        if(calEvent.getCalendarItem()!=null){
            CalendarItem ci=(CalendarItem)calEvent.getCalendarItem().iterator().next();
            e.getParameters().put(PARAM_NAME,ci.getTitle());
        }else{
            e.getParameters().put(PARAM_NAME,"N/A");
        }
        e.getParameters().put(PARAM_URL,"calendar/showCalendarEvent.do~method=preview~reset=true~ampCalendarId="+calEvent.getId().toString());
    return e;
    }

    /** @see org.digijava.module.message.helper.Trigger#getParameterNames()
     */
    @Override
    public String[] getParameterNames() {
        return parameterNames;
    }

}
