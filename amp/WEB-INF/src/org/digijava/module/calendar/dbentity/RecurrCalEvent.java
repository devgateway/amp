package org.digijava.module.calendar.dbentity;

public class RecurrCalEvent {

	private Long id;
	private Calendar calendar;
	private String selectedStartMonth;
    private Long recurrPeriod;
    private String typeofOccurrence;
    private String occurrWeekDays;
    private String recurrStartDate;
    private String recurrEndDate;
    
    
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

    public String getRecurrEndDate() {
        return recurrEndDate;
    }

    public void setRecurrEndDate(String recurrEndDate) {
        this.recurrEndDate = recurrEndDate;
    }

    public String getRecurrStartDate() {
        return recurrStartDate;
    }

    public void setRecurrStartDate(String recurrStartDate) {
        this.recurrStartDate = recurrStartDate;
    }
}
