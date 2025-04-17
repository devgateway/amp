package org.digijava.module.message.triggers;

import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.message.helper.Event;

public class NotApprovedCalendarEventTrigger extends AbstractCalendarEventTrigger{

    public NotApprovedCalendarEventTrigger(CalendarItem calendarItem,
            String teamManager, AmpTeamMember creator) {
        super(calendarItem, teamManager, creator, null);
    }

    @Override
    protected Event getEvent() {
        return new Event(NotApprovedCalendarEventTrigger.class);
    }

}
