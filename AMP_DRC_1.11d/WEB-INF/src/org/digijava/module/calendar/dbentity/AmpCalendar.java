package org.digijava.module.calendar.dbentity;

import java.util.Set;

import org.digijava.module.aim.dbentity.AmpTeamMember;

public class AmpCalendar {
    private AmpCalendarPK calendarPK;
    private AmpEventType eventType;
    private AmpTeamMember member;
    private Set organisations;
    private Set attendees;
    private boolean privateEvent;

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

    public AmpEventType getEventType() {
        return eventType;
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

    public void setEventType(AmpEventType eventType) {
        this.eventType = eventType;
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
}
