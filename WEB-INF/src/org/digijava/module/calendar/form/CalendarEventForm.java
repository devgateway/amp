package org.digijava.module.calendar.form;

import java.util.GregorianCalendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.calendar.entity.DateBreakDown;

public class CalendarEventForm
    extends ActionForm {
    private String method;
    private Long ampCalendarId;
    private List calendarTypes;
    private int selectedCalendarTypeId;
    private String selectedCalendarTypeName;
    private String eventTitle;
    private List eventTypesList;
    private Long selectedEventTypeId;
    private String selectedEventTypeName;
    private List donors;
    private String[] selectedDonors;
    private String selectedStartDate;
    private String selectedStartTime;
    private GregorianCalendar startDate;
    private DateBreakDown startDateBreakDown;
    private String selectedEndDate;
    private String selectedEndTime;
    private GregorianCalendar endDate;
    private DateBreakDown endDateBreakDown;
    private List attendeeUsers;
    private List attendeeGuests;
    private List selectedUsersList;
    private String[] selectedAttendeeUsers;
    private String[] selectedAttendeeGuests;
    private String[] selectedUsers;
    private boolean privateEvent=true;
    private int ispreview=0;
    private boolean reset=false;
   

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

	public int getIspreview() {
		return ispreview;
	}

	public void setIspreview(int ispreview) {
		this.ispreview = ispreview;
	}

	public List getCalendarTypes() {
        return calendarTypes;
    }

    public List getDonors() {
        return donors;
    }

    public GregorianCalendar getEndDate() {
        return endDate;
    }

    public DateBreakDown getEndDateBreakDown() {
        return endDateBreakDown;
    }

    public List getEventTypesList() {

        return eventTypesList;
    }

    public int getSelectedCalendarTypeId() {

        return selectedCalendarTypeId;
    }

    public String[] getSelectedDonors() {
        return selectedDonors;
    }

    public GregorianCalendar getStartDate() {
        return startDate;
    }

    public DateBreakDown getStartDateBreakDown() {
        return startDateBreakDown;
    }

    public String getSelectedStartDate() {
        return selectedStartDate;
    }

    public String getSelectedEndDate() {
        return selectedEndDate;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String[] getSelectedAttendeeGuests() {
        return selectedAttendeeGuests;
    }

    public String[] getSelectedAttendeeUsers() {
        return selectedAttendeeUsers;
    }

    public String getSelectedEventTypeName() {

        return selectedEventTypeName;
    }

    public Long getSelectedEventTypeId() {
        return selectedEventTypeId;
    }

    public String getSelectedCalendarTypeName() {
        return selectedCalendarTypeName;
    }

    public boolean isPrivateEvent() {
        return privateEvent;
    }

    public List getAttendeeGuests() {
        return attendeeGuests;
    }

    public List getAttendeeUsers() {
        return attendeeUsers;
    }

    public String getMethod() {
        return method;
    }

    public Long getAmpCalendarId() {
        return ampCalendarId;
    }

    public String getSelectedEndTime() {
        return selectedEndTime;
    }

    public String getSelectedStartTime() {
        return selectedStartTime;
    }

    public String[] getSelectedUsers() {
        return selectedUsers;
    }

    public List getSelectedUsersList() {
        return selectedUsersList;
    }

    public void setCalendarTypes(List calendarTypes) {
        this.calendarTypes = calendarTypes;
    }

    public void setDonors(List donors) {
        this.donors = donors;
    }

    public void setEndDate(GregorianCalendar endDate) {
        this.endDate = endDate;
    }

    public void setEndDateBreakDown(DateBreakDown endDateBreakDown) {
        this.endDateBreakDown = endDateBreakDown;
    }

    public void setEventTypesList(List eventTypesList) {
        this.eventTypesList = eventTypesList;
    }

    public void setSelectedCalendarTypeId(int selectedCalendarTypeId) {

        this.selectedCalendarTypeId = selectedCalendarTypeId;
    }

    public void setSelectedDonors(String[] selectedDonors) {
        this.selectedDonors = selectedDonors;
    }

    public void setStartDate(GregorianCalendar startDate) {
        this.startDate = startDate;
    }

    public void setStartDateBreakDown(DateBreakDown startDateBreakDown) {
        this.startDateBreakDown = startDateBreakDown;
    }

    public void setSelectedStartDate(String selectedStartDate) {
        this.selectedStartDate = selectedStartDate;
    }

    public void setSelectedEndDate(String selectedEndDate) {
        this.selectedEndDate = selectedEndDate;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public void setSelectedAttendeeGuests(String[] selectedAttendeeGuests) {
        this.selectedAttendeeGuests = selectedAttendeeGuests;
    }

    public void setSelectedAttendeeUsers(String[] selectedAttendeeUsers) {
        this.selectedAttendeeUsers = selectedAttendeeUsers;
    }

    public void setSelectedEventTypeId(Long selectedEventTypeId) {
        this.selectedEventTypeId = selectedEventTypeId;
    }

    public void setSelectedEventTypeName(String selectedEventTypeName) {

        this.selectedEventTypeName = selectedEventTypeName;
    }

    public void setSelectedCalendarTypeName(String selectedCalendarTypeName) {
        this.selectedCalendarTypeName = selectedCalendarTypeName;
    }

    public void setPrivateEvent(boolean privateEvent) {
        this.privateEvent = privateEvent;
    }

    public void setAttendeeUsers(List attendeeUsers) {
        this.attendeeUsers = attendeeUsers;
    }

    public void setAttendeeGuests(List attendeeGuests) {
        this.attendeeGuests = attendeeGuests;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setAmpCalendarId(Long ampCalendarId) {
        this.ampCalendarId = ampCalendarId;
    }

    public void setSelectedStartTime(String selectedStartTime) {
        this.selectedStartTime = selectedStartTime;
    }

    public void setSelectedEndTime(String selectedEndTime) {
        this.selectedEndTime = selectedEndTime;
    }

    public void setSelectedUsers(String[] selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public void setSelectedUsersList(List selectedUsersList) {
        this.selectedUsersList = selectedUsersList;
    }

    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest httpServletRequest) {
        ActionErrors errors = new ActionErrors();
        // event title
        if(this.getEventTitle() == null || this.getEventTitle().equals("")) {
            errors.add(null, new ActionError("error.calendar.emptyEventTitle"));
        }
        // event typeId
        if(this.getSelectedEventTypeId() == null) {
            errors.add(null, new ActionError("error.calendar.emptyEventType"));
        }
        // start date
        if(this.selectedStartDate == null || this.selectedStartDate.equals("")) {
            errors.add(null,
                       new ActionError("error.calendar.emptyEventStartDate"));
        } else if(!DateBreakDown.isValidDate(this.selectedCalendarTypeId,
                                             this.selectedStartDate)) {
            errors.add(null,
                       new ActionError("error.calendar.invalidEventStartDate"));
        } else if(!DateBreakDown.isValidTime(this.selectedStartTime)) {
            errors.add(null,
                       new ActionError("error.calendar.invalidEventStartTime"));
        }
        // end date
        if(this.selectedEndDate == null || this.selectedEndDate.equals("")) {
            errors.add(null, new ActionError("error.calendar.emptyEventEndDate"));
        } else if(!DateBreakDown.isValidDate(this.selectedCalendarTypeId,
                                             this.selectedEndDate)) {
            errors.add(null,
                       new ActionError("error.calendar.invalidEventEndDate"));
        } else if(!DateBreakDown.isValidTime(this.selectedEndTime)) {
            errors.add(null,
                       new ActionError("error:calendar:invalidEventEndTime"));
        }
        // startDate <= endDate ?
        GregorianCalendar startDate = DateBreakDown.createValidGregorianCalendar(this.
            selectedCalendarTypeId, this.selectedStartDate, this.selectedStartTime);
        GregorianCalendar endDate = DateBreakDown.createValidGregorianCalendar(this.
            selectedCalendarTypeId, this.selectedEndDate, this.selectedEndTime);
        if(startDate.getTimeInMillis() > endDate.getTimeInMillis()) {
            errors.add(null,
                       new ActionError("error.calendar.endDateLessThanStartDate"));
        }
        return errors.isEmpty() ? null : errors;
    }
}
