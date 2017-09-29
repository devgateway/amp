package org.digijava.module.calendar.form;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.Team;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.calendar.entity.DateBreakDown;
import org.digijava.module.message.helper.MessageConstants;
import org.digijava.module.aim.helper.Constants;

public class CalendarEventForm
    extends ActionForm {
    



    private Long ampCalendarId;
    private String eventTitle;
    private String method;
    private String searchOrgKey;
    private Map<String,Team> teamsMap;
    private boolean privateEvent;

    private Collection calendarTypes;
    private Long selectedCalendarTypeId;
    private String selectedCalendarTypeName;

    private Collection eventTypesList;
    private Long selectedEventTypeId;
    private String selectedEventTypeName;

//    private Collection organisations;
//    private String[] selectedOrganisations;
//    private Collection selectedOrganisationsCol;

    private String selectedStartDate;
    private String selectedStartTime;
    private String selectedStartMonth;
    private String selectedStartYear;
    private Long recurrPeriod;
    private String typeofOccurrence;
    private String recurrStartDate;
    private String recurrEndDate;
    private String recurrSelectedStartTime;
    private String recurrSelectedEndTime;
    private String occurrWeekDays;
    private String weekDays;
    private GregorianCalendar startDate;
    private DateBreakDown startDateBreakDown;
    private String selectedEndDate;
    private String selectedEndTime;
    private GregorianCalendar endDate;
    private DateBreakDown endDateBreakDown;
    private Collection months;
    private Long calendarTypeId;
//    private Collection eventOrganisations;
//    private String[] selectedEventOrganisations;
//    private Collection selectedEventOrganisationsCol;

    public Long getCalendarTypeId() {
        return calendarTypeId;
    }

    public void setCalendarTypeId(Long calendarTypeId) {
        this.calendarTypeId = calendarTypeId;
    }


    private Collection attendees;
    private String[] selectedAtts;
    private Collection<LabelValueBean> selectedAttsCol;
    private int approve;
    private Long selOrganizations[];
    private Collection<AmpOrganisation> organizations;
    private String description;
    private Long eventCreatorId;
    private String eventCreator;
    private Boolean actionButtonsVisible;
    

    


    public String getRecurrSelectedStartTime() {
        return recurrSelectedStartTime;
    }

    public void setRecurrSelectedStartTime(String recurrSelectedStartTime) {
        this.recurrSelectedStartTime = recurrSelectedStartTime;
    }

    public String getRecurrSelectedEndTime() {
        return recurrSelectedEndTime;
    }

    public void setRecurrSelectedEndTime(String recurrSelectedEndTime) {
        this.recurrSelectedEndTime = recurrSelectedEndTime;
    }
    
    public Long[] getSelOrganizations() {
        return selOrganizations;
    }

    public int getApprove() {
        return approve;
    }
    
    public void setApprove(int approve) {
        this.approve = approve;
    }
        
    public void setSelOrganizations(Long[] selOrganizations) {
        this.selOrganizations = selOrganizations;
    }

    public Collection<AmpOrganisation> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Collection<AmpOrganisation> organizations) {
        this.organizations = organizations;
    }

    
    private boolean resetForm;

    public Collection<Team> getTeamMapValues(){
        return (Collection<Team>)teamsMap.values();
    }

    public Long getAmpCalendarId() {
        return ampCalendarId;
    }

    public Collection getCalendarTypes() {
        return calendarTypes;
    }

    public GregorianCalendar getEndDate() {
        return endDate;
    }

    public DateBreakDown getEndDateBreakDown() {
        return endDateBreakDown;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public Collection getEventTypesList() {
        return eventTypesList;
    }

//    public Collection getOrganisations() {
//        return organisations;
//    }

    public boolean isPrivateEvent() {
        return privateEvent;
    }

    public String[] getSelectedAtts() {
        return selectedAtts;
    }

    public Long getSelectedCalendarTypeId() {
        return selectedCalendarTypeId;
    }

    public String getSelectedCalendarTypeName() {
        return selectedCalendarTypeName;
    }

    public String getSelectedEndDate() {
        return selectedEndDate;
    }

    public String getSelectedEndTime() {
        return selectedEndTime;
    }

    public Long getSelectedEventTypeId() {
        return selectedEventTypeId;
    }

    public String getSelectedEventTypeName() {
        return selectedEventTypeName;
    }

//    public String[] getSelectedOrganisations() {
//        return selectedOrganisations;
//    }

    public String getSelectedStartDate() {
        return selectedStartDate;
    }

    public String getSelectedStartTime() {
        return selectedStartTime;
    }

    public GregorianCalendar getStartDate() {
        return startDate;
    }

    public DateBreakDown getStartDateBreakDown() {
        return startDateBreakDown;
    }

    public String getMethod() {
        return method;
    }

    public Collection getAttendees() {
        return attendees;
    }

//    public String[] getSelectedEventOrganisations() {
//        return selectedEventOrganisations;
//    }

    public String getSearchOrgKey() {
        return searchOrgKey;
    }

    public Map<String,Team> getTeamsMap() {
        return teamsMap;
    }

    public Collection<LabelValueBean> getSelectedAttsCol() {
        return selectedAttsCol;
    }

//    public Collection getSelectedOrganisationsCol() {
//        return selectedOrganisationsCol;
//    }
//
//    public Collection getSelectedEventOrganisationsCol() {
//        return selectedEventOrganisationsCol;
//    }

//    public Collection getEventOrganisations() {
//        return eventOrganisations;
//    }

    public boolean isResetForm() {
        return resetForm;
    }

    public void setStartDateBreakDown(DateBreakDown startDateBreakDown) {
        this.startDateBreakDown = startDateBreakDown;
    }

    public void setStartDate(GregorianCalendar startDate) {
        this.startDate = startDate;
    }

    public void setSelectedStartTime(String selectedStartTime) {
        this.selectedStartTime = selectedStartTime;
    }

    public void setSelectedStartDate(String selectedStartDate) {
        this.selectedStartDate = selectedStartDate;
    }

//    public void setSelectedOrganisations(String[] selectedOrganisations) {
//        this.selectedOrganisations = selectedOrganisations;
//    }

    public void setSelectedEventTypeName(String selectedEventTypeName) {
        this.selectedEventTypeName = selectedEventTypeName;
    }

    public void setSelectedEventTypeId(Long selectedEventTypeId) {
        this.selectedEventTypeId = selectedEventTypeId;
    }

    public void setSelectedEndTime(String selectedEndTime) {
        this.selectedEndTime = selectedEndTime;
    }

    public void setSelectedEndDate(String selectedEndDate) {
        this.selectedEndDate = selectedEndDate;
    }

    public void setSelectedCalendarTypeName(String selectedCalendarTypeName) {
        this.selectedCalendarTypeName = selectedCalendarTypeName;
    }

    public void setSelectedCalendarTypeId(Long selectedCalendarTypeId) {
        this.selectedCalendarTypeId = selectedCalendarTypeId;
    }

    public void setSelectedAtts(String[] selectedAtts) {
        this.selectedAtts = selectedAtts;
    }

    public void setPrivateEvent(boolean privateEvent) {
        this.privateEvent = privateEvent;
    }

//    public void setOrganisations(Collection organisations) {
//        this.organisations = organisations;
//    }

    public void setEventTypesList(Collection eventTypesList) {
        this.eventTypesList = eventTypesList;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public void setEndDateBreakDown(DateBreakDown endDateBreakDown) {
        this.endDateBreakDown = endDateBreakDown;
    }

    public void setEndDate(GregorianCalendar endDate) {
        this.endDate = endDate;
    }

    public void setCalendarTypes(Collection calendarTypes) {
        this.calendarTypes = calendarTypes;
    }

    public void setAmpCalendarId(Long ampCalendarId) {
        this.ampCalendarId = ampCalendarId;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setAttendees(Collection attendees) {
        this.attendees = attendees;
    }

//    public void setSelectedEventOrganisations(String[] selectedEventOrganisations) {
//        this.selectedEventOrganisations = selectedEventOrganisations;
//    }

    public void setSearchOrgKey(String searchOrgKey) {
        this.searchOrgKey = searchOrgKey;
    }

    public void setTeamsMap(Map<String,Team> teamsMap) {
        this.teamsMap = teamsMap;
    }

    public void setSelectedAttsCol(Collection<LabelValueBean> selectedAttsCol) {
        this.selectedAttsCol = selectedAttsCol;
    }

//    public void setSelectedOrganisationsCol(Collection<LabelValueBean> selectedOrganisationsCol) {
//        this.selectedOrganisationsCol = selectedOrganisationsCol;
//    }
//
//    public void setSelectedEventOrganisationsCol(Collection selectedEventOrganisationsCol) {
//        this.selectedEventOrganisationsCol = selectedEventOrganisationsCol;
//    }
//
//    public void setEventOrganisations(Collection eventOrganisations) {
//        this.eventOrganisations = eventOrganisations;
//    }

    public void setResetForm(boolean resetForm) {
        this.resetForm = resetForm;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        ampCalendarId=null;
        eventTitle=null;
        //method=null;
        searchOrgKey=null;
        teamsMap=null;
        privateEvent=true;

        calendarTypes=null;
        selectedCalendarTypeId=null;
        selectedCalendarTypeName=null;

        eventTypesList=null;
        selectedEventTypeId=null;
        selectedEventTypeName=null;
        description = null;
        recurrPeriod = null;
        typeofOccurrence = null;
//
//        organisations=null;
//        selectedOrganisations=null;
//        selectedOrganisationsCol=null;
//        organizations = null;
//        selOrganizations = null;
        
       String dtformat = FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);
    if (dtformat == null){
        dtformat = "dd/mm/yyyy";
        }
    

        SimpleDateFormat sdf = new SimpleDateFormat(dtformat);

        selectedStartDate=sdf.format(new Date());
        selectedStartTime="00:00";
        startDate=null;
        startDateBreakDown=null;
        selectedEndDate=sdf.format(new Date());
        selectedEndTime="00:00";
        endDate=null;
        endDateBreakDown=null;
        recurrEndDate=null;
        recurrSelectedEndTime=null;
        
//        eventOrganisations=null;
//        selectedEventOrganisations=null;
//        selectedEventOrganisationsCol=null;

        attendees=null;
        selectedAtts=null;
        selectedAttsCol=null;
      
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

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

    public Collection getMonths() {
        return months;
    }

    public void setMonths(Collection months) {
        this.months = months;
    }

    public String getRecurrStartDate() {
        return recurrStartDate;
    }

    public void setRecurrStartDate(String recurrStartDate) {
        this.recurrStartDate = recurrStartDate;
    }

    public String getRecurrEndDate() {
        return recurrEndDate;
    }

    public void setRecurrEndDate(String recurrEndDate) {
        this.recurrEndDate = recurrEndDate;
    }
    
    public String getSelectedStartYear() {
        return selectedStartYear;
    }

    public void setSelectedStartYear(String selectedStartYear) {
        this.selectedStartYear = selectedStartYear;
    }

    public String getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
    }


    public Long getEventCreatorId() {
        return eventCreatorId;
    }

    public void setEventCreatorId(Long eventCreatorId) {
        this.eventCreatorId = eventCreatorId;
    }
    
    public String getEventCreator() {
        return eventCreator;
    }

    public void setEventCreator(String eventCreator) {
        this.eventCreator = eventCreator;
    }

    public Boolean getActionButtonsVisible() {
        return actionButtonsVisible;
    }

    public void setActionButtonsVisible(Boolean actionButtonsVisible) {
        this.actionButtonsVisible = actionButtonsVisible;
    }

}
