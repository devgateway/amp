package org.digijava.module.calendar.dbentity;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "DG_RECURR_EVENT")
public class RecurrCalEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dg_recurr_event_seq")
    @SequenceGenerator(name = "dg_recurr_event_seq", sequenceName = "dg_recurr_event_seq", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CALENDAR_ID", nullable = false)
    private Calendar calendar;

    @Column(name = "TYPE_OF_OCCURRENCE")
    private String typeofOccurrence;

    @Column(name = "SELECTED_MONTH")
    private String selectedStartMonth;

    @Column(name = "OCCURR_WEEKDAYS")
    private String occurrWeekDays;

    @Column(name = "RECURR_PERIOD")
    private Long recurrPeriod;

    @Column(name = "RECURR_START_DATE")
    private Date recurrStartDate;

    @Column(name = "RECURR_END_DATE")
    private Date recurrEndDate;
    
    
    public String getSelectedStartMonth() {
        return selectedStartMonth;
    }
    public void setSelectedStartMonth(String selectedStartMonth) {
        this.selectedStartMonth = selectedStartMonth;
    }
    public Long getRecurrPeriod() {
        return recurrPeriod;
    }
    public void setRecurrPeriod(Long recurrPeriod) {
        this.recurrPeriod = recurrPeriod;
    }
    public String getTypeofOccurrence() {
        return typeofOccurrence;
    }
    public void setTypeofOccurrence(String typeofOccurrence) {
        this.typeofOccurrence = typeofOccurrence;
    }
    public String getOccurrWeekDays() {
        return occurrWeekDays;
    }
    public void setOccurrWeekDays(String occurrWeekDays) {
        this.occurrWeekDays = occurrWeekDays;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Calendar getCalendar() {
        return calendar;
    }
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Date getRecurrEndDate() {
        return recurrEndDate;
    }

    public void setRecurrEndDate(Date recurrEndDate) {
        this.recurrEndDate = recurrEndDate;
    }

    public Date getRecurrStartDate() {
        return recurrStartDate;
    }

    public void setRecurrStartDate(Date recurrStartDate) {
        this.recurrStartDate = recurrStartDate;
    }
}
