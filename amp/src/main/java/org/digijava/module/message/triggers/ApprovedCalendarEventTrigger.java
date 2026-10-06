package org.digijava.module.message.triggers;

import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.message.helper.Event;

public class ApprovedCalendarEventTrigger extends AbstractCalendarEventTrigger{

    public ApprovedCalendarEventTrigger(CalendarItem calendarItem,
            String teamManager, AmpTeamMember creator) {
        super(calendarItem, teamManager, creator,
                "calendar/showCalendarEvent.do~ampCalendarId="+calendarItem.getId()+"~method=preview~resetForm=true");
    }

    @Override
    protected Event getEvent() {
        return new Event(ApprovedCalendarEventTrigger.class);
    }

}
