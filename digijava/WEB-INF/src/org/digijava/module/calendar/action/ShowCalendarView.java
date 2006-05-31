package org.digijava.module.calendar.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.calendar.dbentity.AmpEventType;
import org.digijava.module.calendar.entity.CalendarOptions;
import org.digijava.module.calendar.entity.DateBreakDown;
import org.digijava.module.calendar.entity.DateNavigator;
import org.digijava.module.calendar.entity.EventsFilter;
import org.digijava.module.calendar.form.CalendarViewForm;
import org.digijava.module.calendar.util.AmpDbUtil;
import org.digijava.module.calendar.util.AmpUtil;

public class ShowCalendarView
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        CalendarViewForm calendarViewForm = (CalendarViewForm) form;

        // calendar type
        List calendarTypesList = DateNavigator.getCalendarTypes();
        calendarViewForm.setCalendarTypes(calendarTypesList);
        // selected calendar type
        int selectedCalendarType = calendarViewForm.getSelectedCalendarType();
        if(selectedCalendarType != CalendarOptions.CALENDAR_TYPE_GREGORIAN &&
           selectedCalendarType != CalendarOptions.CALENDAR_TYPE_ETHIOPIAN &&
           selectedCalendarType != CalendarOptions.CALENDAR_TYPE_ETHIOPIAN_FY) {
            selectedCalendarType = CalendarOptions.defaultCalendarType;
        }
        calendarViewForm.setSelectedCalendarType(selectedCalendarType);
        // view
        String view = calendarViewForm.getView();
        if(view == null ||
           !view.equals(CalendarOptions.CALENDAR_VIEW_YEARLY) &&
           !view.equals(CalendarOptions.CALENDAR_VIEW_MONTHLY) &&
           !view.equals(CalendarOptions.CALENDAR_VIEW_WEEKLY) &&
           !view.equals(CalendarOptions.CALENDAR_VIEW_DAYLY) &&
           !view.equals(CalendarOptions.CALENDAR_VIEW_CUSTOM)) {
            view = CalendarOptions.defaultView;
        }
        calendarViewForm.setView(view);
        // current date
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.setFirstDayOfWeek(currentDate.MONDAY);
        calendarViewForm.setCurrentDate(currentDate);
        DateBreakDown currentDateBreakDown = new DateBreakDown(currentDate,
            selectedCalendarType);
        calendarViewForm.setCurrentDateBreakDown(currentDateBreakDown);
        GregorianCalendar startDate;
        DateBreakDown startDateBreakDown;
        GregorianCalendar endDate;
        DateBreakDown endDateBreakDown;
        DateNavigator navigator = null;
        if(!view.equals(CalendarOptions.CALENDAR_VIEW_CUSTOM)) {
            // stamp
            int timestamp = calendarViewForm.getTimestamp();
            if(timestamp == 0) {
                timestamp = (int) (currentDate.getTimeInMillis() / 1000);
                calendarViewForm.setTimestamp(timestamp);
            }
            // base date
            GregorianCalendar baseDate = (GregorianCalendar) currentDate.clone();
            int currentStamp = (int) (currentDate.getTimeInMillis() / 1000);
            baseDate.add(baseDate.SECOND, timestamp - currentStamp);
            calendarViewForm.setBaseDate(baseDate);
            DateBreakDown baseDateBreakDown = new DateBreakDown(baseDate,
                selectedCalendarType);
            calendarViewForm.setBaseDateBreakDown(baseDateBreakDown);
            // date navigator
            navigator = new DateNavigator(baseDate, selectedCalendarType,
                                          calendarViewForm.getView());
            // start date
            startDate = DateNavigator.getStartDate(calendarViewForm.getView(),
                baseDate);
            calendarViewForm.setStartDate(startDate);
            startDateBreakDown = new DateBreakDown(startDate,
                selectedCalendarType);
            calendarViewForm.setStartDateBreakDown(startDateBreakDown);
            // stop date
            endDate = DateNavigator.getEndDate(calendarViewForm.getView(),
                                               baseDate);
            calendarViewForm.setEndDate(endDate);
            endDateBreakDown = new DateBreakDown(endDate, selectedCalendarType);
            calendarViewForm.setEndDateBreakDown(endDateBreakDown);
        } else {
            // start date
            startDate = DateBreakDown.createValidGregorianCalendar(
                selectedCalendarType, calendarViewForm.getCustomViewStartDate(),
                "00:00");
            calendarViewForm.setStartDate(startDate);
            startDateBreakDown = new DateBreakDown(startDate,
                selectedCalendarType);
            calendarViewForm.setStartDateBreakDown(startDateBreakDown);
            calendarViewForm.setCustomViewStartDate(startDateBreakDown.
                formatDateString());
            // stop date
            endDate = DateBreakDown.createValidGregorianCalendar(
                selectedCalendarType, calendarViewForm.getCustomViewEndDate(),
                "23:59");
            calendarViewForm.setEndDate(endDate);
            endDateBreakDown = new DateBreakDown(endDate, selectedCalendarType);
            calendarViewForm.setEndDateBreakDown(endDateBreakDown);
            calendarViewForm.setCustomViewEndDate(endDateBreakDown.
                                                  formatDateString());
            // date navigator
            navigator = new DateNavigator(startDate, endDate);
        }
        calendarViewForm.setDateNavigator(navigator);
        // FILTER
        EventsFilter filter = calendarViewForm.getFilter();
        // event types
        List eventTypes = AmpDbUtil.getEventTypes();
        filter.setEventTypes(eventTypes);
        // donors
        Collection organisations = org.digijava.module.aim.util.DbUtil.
            getDonors();
        List donors = new ArrayList();
        Iterator it = organisations.iterator();
        while(it.hasNext()) {
            AmpOrganisation org = (AmpOrganisation) it.next();
            LabelValueBean lvb = new LabelValueBean(org.getName(),
                org.getAmpOrgId().toString());
            donors.add(lvb);
        }
        filter.setDonors(donors);
        // select event types, donors, showPublicEvents
        if(!calendarViewForm.isFilterInUse()) {
            // event types
            String[] selectedEventTypes = new String[filter.getEventTypes().
                size()];
            int index = 0;
            it = filter.getEventTypes().iterator();
            while(it.hasNext()) {
                AmpEventType eventType = (AmpEventType) it.next();
                selectedEventTypes[index++] = eventType.getId().toString();
            }
            filter.setSelectedEventTypes(selectedEventTypes);
            // donors
            String[] selectedDonors = new String[filter.getDonors().size()];
            index = 0;
            it = filter.getDonors().iterator();
            while(it.hasNext()) {
                LabelValueBean lvb = (LabelValueBean) it.next();
                selectedDonors[index++] = lvb.getValue();
            }
            filter.setSelectedDonors(selectedDonors);
            // showPublicEvents
            filter.setShowPublicEvents(false);
        }
        // events
        Long userId = null;
        User currentUser = RequestUtils.getUser(request);
        if(currentUser != null) {
            userId = currentUser.getId();
        }
        List ampCalendarEvents = AmpDbUtil.getAmpCalendarEvents(startDate,
            endDate, filter.getSelectedEventTypes(), filter.getSelectedDonors(),
            userId, filter.isShowPublicEvents(), null, null);
        List ampCalendarGraphs = AmpUtil.getAmpCalendarGraphs(ampCalendarEvents,
            navigator, view);
        calendarViewForm.setAmpCalendarGraphs(ampCalendarGraphs);
        return mapping.findForward("success");
    }
}
