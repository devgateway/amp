package org.digijava.module.calendar.dbentity;

import org.digijava.kernel.user.User;

public class AmpCalendarAttendee {
    private Long id;
    private AmpCalendar ampCalendar;
    private User user;
    private String guest;

    public AmpCalendarAttendee() {

    }

    public AmpCalendarAttendee(AmpCalendar ampCalendar, User user) {
        this.ampCalendar = ampCalendar;
        this.user = user;
    }

    public AmpCalendarAttendee(AmpCalendar ampCalendar, String guest) {
        this.ampCalendar = ampCalendar;
        this.guest = guest;
    }

    public String getGuest() {
        return guest;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public AmpCalendar getAmpCalendar() {
        return ampCalendar;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAmpCalendar(AmpCalendar ampCalendar) {
        this.ampCalendar = ampCalendar;
    }

    public boolean equals(Object obj) {
        boolean result;
        AmpCalendarAttendee other = (AmpCalendarAttendee) obj;
        Long idA = this.getId() != null ? this.getId() : new Long(0);
        Long idB = other != null && other.getId() != null ? other.getId() :
            new Long(0);
        result = idA.equals(idB);
        if(!result) {
            return result;
        }
        Long userA = this.getUser() != null && this.getUser().getId() != null ?
            this.getUser().getId() : new Long(0);
        Long userB = other != null && other.getUser() != null &&
            other.getUser().getId() != null ? other.getUser().getId() :
            new Long(0);
        result = userA.equals(userB);
        if(!result) {
            return result;
        }
        String guestA = this.getGuest() != null ? this.getGuest() : "";
        String guestB = other != null && other.getGuest() != null ?
            other.getGuest() : "";
        return guestA.equals(guestB);
    }

    public int hashCode() {
        Long id = this.getId() != null ? this.getId() : new Long(0);
        return id.hashCode();
    }
}
