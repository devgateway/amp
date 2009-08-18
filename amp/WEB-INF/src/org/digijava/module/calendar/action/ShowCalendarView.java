package org.digijava.module.calendar.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendarPK;
import org.digijava.module.calendar.dbentity.AmpEventType;
import org.digijava.module.calendar.entity.AmpCalendarGraph;
import org.digijava.module.calendar.entity.CalendarOptions;
import org.digijava.module.calendar.entity.DateBreakDown;
import org.digijava.module.calendar.entity.DateNavigator;
import org.digijava.module.calendar.entity.EventsFilter;
import org.digijava.module.calendar.form.CalendarViewForm;
import org.digijava.module.calendar.util.AmpDbUtil;
import org.digijava.module.calendar.util.AmpUtil;

public class ShowCalendarView extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        CalendarViewForm calendarViewForm = (CalendarViewForm) form;
        HttpSession ses = request.getSession();
        TeamMember mem = (TeamMember) ses.getAttribute("currentMember");
        Boolean showPublicEvents = calendarViewForm.getResetFilter();
        
        if (showPublicEvents == null)
        	showPublicEvents = new Boolean(true);
        
        Object eventCreated=request.getAttribute("calendarEventCreated");

        // calendar type
        List calendarTypesList = DateNavigator.getCalendarTypes();

        Collection defCnISO = FeaturesUtil.getDefaultCountryISO();
        if (defCnISO != null) {
            AmpGlobalSettings sett = (AmpGlobalSettings) defCnISO.iterator().next();
            if (!sett.getGlobalSettingsValue().equalsIgnoreCase("et")) {
                for (Iterator iter = calendarTypesList.iterator(); iter.hasNext(); ) {
                    LabelValueBean item = (LabelValueBean) iter.next();
                    if (item.getLabel().equalsIgnoreCase("ethiopian") ||item.getLabel().equalsIgnoreCase("ethiopian fy")) {
                        iter.remove();
                    }
                }
            }
        }
        
        calendarViewForm.setCalendarTypes(calendarTypesList);
        // selected calendar type
        int selectedCalendarType = calendarViewForm.getSelectedCalendarType();
        if (selectedCalendarType != CalendarOptions.CALENDAR_TYPE_GREGORIAN &&
            selectedCalendarType != CalendarOptions.CALENDAR_TYPE_ETHIOPIAN &&
            selectedCalendarType != CalendarOptions.CALENDAR_TYPE_ETHIOPIAN_FY) {
            selectedCalendarType = CalendarOptions.defaultCalendarType;
        }
        calendarViewForm.setSelectedCalendarType(selectedCalendarType);
        // view
        String view = calendarViewForm.getView();
        if (view == null ||
            !view.equals(CalendarOptions.CALENDAR_VIEW_YEARLY) &&
            !view.equals(CalendarOptions.CALENDAR_VIEW_MONTHLY) &&
            !view.equals(CalendarOptions.CALENDAR_VIEW_WEEKLY) &&
            !view.equals(CalendarOptions.CALENDAR_VIEW_DAYLY) &&
            !view.equals(CalendarOptions.CALENDAR_VIEW_CUSTOM) && !view.equals(CalendarOptions.CALENDAR_VIEW_NONE)) {
            view = CalendarOptions.defaultView;
        }
        calendarViewForm.setView(view);
        // current date
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.setFirstDayOfWeek(currentDate.MONDAY);
        calendarViewForm.setCurrentDate(currentDate);
        DateBreakDown currentDateBreakDown = new DateBreakDown(currentDate, selectedCalendarType, request);
        calendarViewForm.setCurrentDateBreakDown(currentDateBreakDown);
        GregorianCalendar startDate;
        DateBreakDown startDateBreakDown;
        GregorianCalendar endDate;
        DateBreakDown endDateBreakDown;
        DateNavigator navigator = null;
        if (!view.equals(CalendarOptions.CALENDAR_VIEW_CUSTOM)) {
            // stamp
            int timestamp = calendarViewForm.getTimestamp();
            if (timestamp == 0) {
                timestamp = (int) (currentDate.getTimeInMillis() / 1000);
                calendarViewForm.setTimestamp(timestamp);
            }
            // base date
            GregorianCalendar baseDate = (GregorianCalendar) currentDate.clone();
            int currentStamp = (int) (currentDate.getTimeInMillis() / 1000);
            baseDate.add(baseDate.SECOND, timestamp - currentStamp);
            calendarViewForm.setBaseDate(baseDate);
            DateBreakDown baseDateBreakDown = new DateBreakDown(baseDate,selectedCalendarType, request);
            calendarViewForm.setBaseDateBreakDown(baseDateBreakDown);
            // date navigator
            navigator = new DateNavigator(baseDate, selectedCalendarType,calendarViewForm.getView(), request);
            // start date
            startDate = DateNavigator.getStartDate(calendarViewForm.getView(),baseDate);
            calendarViewForm.setStartDate(startDate);
            startDateBreakDown = new DateBreakDown(startDate,selectedCalendarType, request);
            calendarViewForm.setStartDateBreakDown(startDateBreakDown);
            // stop date
            endDate = DateNavigator.getEndDate(calendarViewForm.getView(),baseDate);
            calendarViewForm.setEndDate(endDate);
            endDateBreakDown = new DateBreakDown(endDate, selectedCalendarType, request);
            calendarViewForm.setEndDateBreakDown(endDateBreakDown);
        } else {
            // start date
            startDate = DateBreakDown.createValidGregorianCalendar(selectedCalendarType, calendarViewForm.getCustomViewStartDate(),"00:00");
            calendarViewForm.setStartDate(startDate);
            startDateBreakDown = new DateBreakDown(startDate,selectedCalendarType, request);
            calendarViewForm.setStartDateBreakDown(startDateBreakDown);
            calendarViewForm.setCustomViewStartDate(startDateBreakDown.formatDateString());
            // stop date
            endDate = DateBreakDown.createValidGregorianCalendar(selectedCalendarType, calendarViewForm.getCustomViewEndDate(),
                "23:59");
            calendarViewForm.setEndDate(endDate);
            endDateBreakDown = new DateBreakDown(endDate, selectedCalendarType, request);
            calendarViewForm.setEndDateBreakDown(endDateBreakDown);
            calendarViewForm.setCustomViewEndDate(endDateBreakDown.formatDateString());
            // date navigator
            navigator = new DateNavigator(startDate, endDate);
        }
        calendarViewForm.setDateNavigator(navigator);
        // FILTER
        EventsFilter filter = calendarViewForm.getFilter();
        if (showPublicEvents!=null && showPublicEvents) {
            filter.setShowPublicEvents(true);
        } else {
            filter.setShowPublicEvents(false);
        }

        // event types
        List eventTypes = AmpDbUtil.getEventTypes();

        if (eventTypes.size() <= 0) {
            return mapping.findForward("forward");
        }
        filter.setEventTypes(eventTypes);
        Boolean resetEventTypes=calendarViewForm.getResetEventTypes();
        if(resetEventTypes!=null && resetEventTypes){
        	filter.setSelectedEventTypes(new String[]{});
        }
        // donors        
        Collection organisations = org.digijava.module.aim.util.DbUtil.getOrganisations();
        List donors = new ArrayList();
        Iterator it = organisations.iterator();
        while (it.hasNext()) {
            AmpOrganisation org = (AmpOrganisation) it.next();
            LabelValueBean lvb = new LabelValueBean(org.getName(), org.getAmpOrgId().toString());
            donors.add(lvb);
        }
        filter.setDonors(donors);
        Boolean resetDonors=calendarViewForm.getResetDonors();
        if(resetDonors!=null && resetDonors){
        	filter.setSelectedDonors(new String[]{});
        }
        // select event types, donors, showPublicEvents
        if (!calendarViewForm.isFilterInUse()||eventCreated!=null) {
            // event types
            String[] selectedEventTypes = new String[filter.getEventTypes().size()];
            int index = 0;
            it = filter.getEventTypes().iterator();
            while (it.hasNext()) {
                AmpEventType eventType = (AmpEventType) it.next();
                selectedEventTypes[index++] = eventType.getId().toString();
            }
            filter.setSelectedEventTypes(selectedEventTypes);
            // donor
            // FFerreyra: The selectedDonors array has one more member for "None" to show events without Donor set. See AMP-2691
            String[] selectedDonors = new String[filter.getDonors().size() + 1];
            index = 0;

            // FFerreyra: Add the first NULL Option to be set in the filter by default
            selectedDonors[index++] = "None";

            it = filter.getDonors().iterator();
            while (it.hasNext()) {
                LabelValueBean lvb = (LabelValueBean) it.next();
                selectedDonors[index++] = lvb.getValue();
            }
            filter.setSelectedDonors(selectedDonors);


            if (eventCreated != null) {
                // we are showing private or public events depending on the newly created event
                Boolean showPubEvent = (Boolean) eventCreated;
                filter.setShowPublicEvents(showPubEvent);
            }/* else {
                // showPublicEvents
                filter.setShowPublicEvents(false);
            }*/
            
        }
        // events
        Long userId = null;
        User currentUser = RequestUtils.getUser(request);
        if (currentUser != null) {
            userId = currentUser.getId();
        }
        AmpTeamMember member = TeamMemberUtil.getAmpTeamMember(mem.getMemberId());
        Collection ampCalendarEvents = AmpDbUtil.getAmpCalendarEventsByMember(startDate,
            endDate, filter.getSelectedEventTypes(), filter.getSelectedDonors(),
            member, filter.isShowPublicEvents(), null, null);
            
              List l = new ArrayList(ampCalendarEvents);
        Comparator c = new Comparator<AmpCalendar>(){
       	public int compare(AmpCalendar o1, AmpCalendar o2) {
        		AmpCalendarPK d1 = o1.getCalendarPK();
        		AmpCalendarPK d2 = o2.getCalendarPK();
        		if (d1.getStartYear() != d2.getStartYear())
        			return d1.getStartYear() - d2.getStartYear();
        		else
        			if (d1.getStartMonth() != d2.getStartMonth())
        				return d1.getStartMonth() - d2.getStartMonth();
        			else
        				if (d1.getStartDay() != d2.getStartDay())
        					return d1.getStartDay() - d2.getStartDay();
        				else
        					if (d1.getStartHour() != d2.getStartHour())
        						return d1.getStartHour() - d2.getStartHour();
        					else
        						if (d1.getStartMinute() != d2.getStartMinute())
        							return d1.getStartMinute() - d2.getStartMinute();
        						else
        							return 0;
			}
        };
        Collections.sort(l, c);    
            
       Collection<AmpCalendarGraph> ampCalendarGraphs = AmpUtil.getAmpCalendarGraphs(l,navigator, view);
       calendarViewForm.setAmpCalendarGraphs(ampCalendarGraphs);
      
         ses.setAttribute("mode",calendarViewForm.getView().length());
         ses.setAttribute("publicEvent", filter.isShowPublicEvents());
         ses.setAttribute("donor", filter.getSelectedDonors());
         ses.setAttribute("year", calendarViewForm.getBaseDateBreakDown().getYear());
         ses.setAttribute("month", calendarViewForm.getBaseDateBreakDown().getMonth());
         ses.setAttribute("day", calendarViewForm.getBaseDateBreakDown().getDayOfMonth());
         ses.setAttribute("type", calendarViewForm.getBaseDateBreakDown().getType());
         
         
         
        return mapping.findForward("success");
    }
}
