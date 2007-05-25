package org.digijava.module.calendar.dbentity;

import java.util.Set;

import org.digijava.kernel.user.User;

public class AmpCalendar {
    private AmpCalendarPK calendarPK;
    private AmpEventType eventType;
    private User user;
    private Set donors;
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

    public Set getDonors() {
        return donors;
    }

    public Set getAttendees() {
        return attendees;
    }

    public User getUser() {
        return user;
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

    public void setAttendees(Set attendees) {
        this.attendees = attendees;
    }

    public void setDonors(Set donors) {
        this.donors = donors;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
