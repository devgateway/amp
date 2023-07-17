package org.digijava.module.calendar.dbentity;

import java.io.Serializable;
import java.util.Set;

import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AMP_CALENDAR")
public class AmpCalendar implements Serializable {
    @EmbeddedId
    private AmpCalendarPK calendarPK;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private AmpTeamMember member;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AMP_CALENDAR_EVENT_ORGANISATIO",
            joinColumns = @JoinColumn(name = "CALENDAR_ID"),
            inverseJoinColumns = @JoinColumn(name = "amp_org_id"))
    private Set<AmpOrganisation> organisations = new HashSet<>();

    @OneToMany(mappedBy = "ampCalendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpCalendarAttendee> attendees = new HashSet<>();

    @Column(name = "PRIVATE")
    private boolean privateEvent;

    @ManyToOne
    @JoinColumn(name = "EVENTS_TYPE_ID")
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
