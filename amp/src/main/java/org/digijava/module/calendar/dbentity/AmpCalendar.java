package org.digijava.module.calendar.dbentity;

import java.io.Serializable;
import java.util.Set;

import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class AmpCalendar implements Serializable {   

    private AmpCalendarPK calendarPK;
    private AmpTeamMember member;
    private Set<AmpOrganisation> organisations;
    private Set attendees;
    private boolean privateEvent;
    private AmpCategoryValue eventsType; 

    public AmpCalendar() {

    }

    public AmpCalendar(AmpCalendarPK calendarPK) {
        this.calendarPK = calendarPK;
    }

    public AmpCalendar(Calendar calendar) {
        this.calendarPK = new AmpCalendarPK(calendar);
    }

    public AmpCalendarPK getCalendarPK() {
        return calendarPK;
    }
    public boolean isPrivateEvent() {
        return privateEvent;
    }

    public AmpTeamMember getMember() {
        return member;
    }

    public Set getOrganisations() {
        return organisations;
    }

    public Set getAttendees() {
        return attendees;
    }

    public void setCalendarPK(AmpCalendarPK calendarPK) {
        this.calendarPK = calendarPK;
    }
    public void setPrivateEvent(boolean privateEvent) {
        this.privateEvent = privateEvent;
    }

    public void setMember(AmpTeamMember member) {
        this.member = member;
    }

    public void setOrganisations(Set organisations) {
        this.organisations = organisations;
    }

    public void setAttendees(Set attendees) {
        this.attendees = attendees;
    }
    
    public AmpCategoryValue getEventsType() {
        return eventsType;
    }

    public void setEventsType(AmpCategoryValue eventsType) {
        this.eventsType = eventsType;
    }
}
