package org.digijava.module.calendar.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import javax.persistence.*;

@Entity
@Table(name = "AMP_CALENDAR_ATTENDEE")
public class AmpCalendarAttendee implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CALENDAR_ID")
    private AmpCalendar ampCalendar;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private AmpTeamMember member;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private AmpTeam team;

    @Column(name = "GUEST")
    private String guest;

    public AmpCalendarAttendee() {

    }

    public AmpCalendarAttendee(AmpCalendar ampCalendar, AmpTeamMember member) {
        this.ampCalendar=ampCalendar;
        this.member=member;
    }

    public AmpCalendarAttendee(AmpCalendar ampCalendar, AmpTeam team) {
        this.ampCalendar=ampCalendar;
        this.team=team;
    }

    public AmpCalendarAttendee(AmpCalendar ampCalendar, String guest) {
        this.ampCalendar=ampCalendar;
        this.guest=guest;
    }

    public String getGuest() {
        return guest;
    }

    public Long getId() {
        return id;
    }

    public AmpCalendar getAmpCalendar() {
        return ampCalendar;
    }

    public AmpTeamMember getMember() {
        return member;
    }

    public AmpTeam getTeam() {
        return team;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setAmpCalendar(AmpCalendar ampCalendar) {
        this.ampCalendar = ampCalendar;
    }

    public void setMember(AmpTeamMember member) {
        this.member = member;
    }

    public void setTeam(AmpTeam team) {
        this.team = team;
    }

    public int hashCode() {
        Long id = this.getId() != null ? this.getId() : new Long(0);
        return id.hashCode();
    }
}
