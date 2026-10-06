package org.digijava.module.message.triggers;

import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.message.helper.Event;

public abstract class AbstractCalendarEventTrigger extends Trigger{
    public static final String PARAM_TITLE="title";
    public static final String PARAM_AUTHOR="autor";    
    public static final String PARAM_URL="url";
    public static final String PARAM_TEAM_MANAGER="team_manager";
    
    public static final String [] parameterNames=new String[]{PARAM_TITLE,PARAM_AUTHOR,PARAM_URL,PARAM_TEAM_MANAGER};
    
    private String teamManager = null;
    private AmpTeamMember creator = null;
    protected String url = null;
    
    public AbstractCalendarEventTrigger(CalendarItem calendarItem, String teamManager, AmpTeamMember creator, String url){
        this.source = calendarItem;
        this.teamManager = teamManager;
        this.creator = creator;
        this.url = url;
        forwardEvent();
    }
    
    @Override
    protected Event generateEvent() {
        Event e = getEvent(); 
        CalendarItem calendarItem=(CalendarItem) source;
        e.getParameters().put(PARAM_TITLE, calendarItem.getTitle());
        e.getParameters().put(PARAM_TEAM_MANAGER, teamManager);
        if(url != null)
            e.getParameters().put(PARAM_URL,url);
        e.getParameters().put(PARAM_AUTHOR, creator);
        return e;
    }

    @Override
    public String[] getParameterNames() {
        return parameterNames;
    }
    
    protected abstract Event getEvent();
}
