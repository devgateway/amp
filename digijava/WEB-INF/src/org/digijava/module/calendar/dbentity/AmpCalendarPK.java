package org.digijava.module.calendar.dbentity;

import java.io.Serializable;

public class AmpCalendarPK implements Serializable {
    private Calendar calendar;

    public AmpCalendarPK() {

    }

    public AmpCalendarPK(Long ampCalendarId) {
        this.calendar = new Calendar();
        this.calendar.setId(ampCalendarId);
    }

    public AmpCalendarPK(Calendar calendar) {
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public boolean equals(Object o) {
        if(!(o instanceof AmpCalendarPK)) {
            return false;
        }
        AmpCalendarPK otherAmpCalendarPK = (AmpCalendarPK) o;
        long id = this != null && this.calendar != null ?
            this.calendar.getId().longValue() : 0;
        long otherId = otherAmpCalendarPK != null &&
            otherAmpCalendarPK.calendar != null ?
            otherAmpCalendarPK.calendar.getId().longValue() : 0;
        return id == otherId;
    }

    public int hashCode() {
        return this != null && this.calendar != null ?
            this.calendar.getId().hashCode() : 0;
    }
}
