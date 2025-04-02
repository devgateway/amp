package org.digijava.module.calendar.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.calendar.entity.AmpEventType;
import org.digijava.module.calendar.entity.CalendarOptions;
import org.digijava.module.calendar.entity.DateBreakDown;
import org.digijava.module.calendar.entity.DateNavigator;
import org.digijava.module.calendar.entity.EventsFilter;
import org.digijava.module.calendar.form.CalendarViewForm;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class ShowCalendarView extends Action {

    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {

        CalendarViewForm calendarViewForm = (CalendarViewForm) form;
        HttpSession ses = request.getSession();
        String printView =  request.getParameter("view");
        String printDate =  request.getParameter("date");
        Date printDateD = null;
        if (printDate!=null && !printDate.equals("NaN")) {
            printDateD = new Date(Long.parseLong(printDate));
        }
        int numDays = 0;
        
        if(printView != null){
            if(printView.equals("daily")){
                numDays = 1;
            }else if (printView.equals("weekly")){
                numDays = 2;
            }else if (printView.equals("yearly")){              
                numDays = 4;
            }else if (printView.equals("monthly")){
                numDays = 3;
            }
        }
        String print =  request.getParameter("print");
        
        Integer showPublicEvents = 0; 
        
        if (calendarViewForm.getShowPublicEvents()!=null && calendarViewForm.getShowPublicEvents().length()>0) {
            try {
                showPublicEvents = Integer.valueOf(calendarViewForm.getShowPublicEvents());
            } catch (NumberFormatException nfex) {
                showPublicEvents = new Integer (0); 
            }
        }
        
        calendarViewForm.setPrintMode(numDays);
        if(print != null){
         if(print.equals("true")){
            calendarViewForm.setPrint(true);
         }else{
             calendarViewForm.setPrint(false);
         }
        }else{
            calendarViewForm.setPrint(false);
        }
        if (showPublicEvents == null)
            showPublicEvents = 0;
        
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
        
        filter.setShowPublicEvents(showPublicEvents);

        // event types
        if (CategoryManagerUtil.getAmpEventColors().size() <= 0) {
            return mapping.findForward("forward");
        }
        filter.setEventTypes(CategoryManagerUtil.getAmpEventColors());
        //add new event type, is added separetely because it only need to be showd here
        AmpEventType newType = new AmpEventType();
        newType.setColor("orange");
        newType.setName("Overlapping");
        newType.setId(-1L);
        filter.getEventTypes().add(newType);
        
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
            
            if (eventCreated != null) {
                 filter.setShowPublicEvents(showPublicEvents);
                // we are showing private or public events depending on the newly created event
                //Boolean showPubEvent = (Boolean) eventCreated;
                //filter.setShowPublicEvents(showPubEvent);
            }/* else {
                // showPublicEvents
                filter.setShowPublicEvents(false);
            }*/
            
        }

         //fill session
         ses.setAttribute("calendarMode",calendarViewForm.getView().length());
         ses.setAttribute("view",calendarViewForm.getPrintMode());
         ses.setAttribute("print",calendarViewForm.getPrint());
         if (printDateD!=null){
             java.util.Calendar gc = new GregorianCalendar ();
             gc.setTime(printDateD);
            ses.setAttribute("printDay",gc.get(java.util.Calendar.DAY_OF_MONTH));
            ses.setAttribute("printMonth",gc.get(java.util.Calendar.MONTH) + 1);
            ses.setAttribute("printYear",gc.get(java.util.Calendar.YEAR));
         } else {
             ses.setAttribute("date",printDate);
         }
         ses.setAttribute("publicEvent", filter.getShowPublicEvents());
         ses.setAttribute("donor", filter.getSelectedDonors());
         ses.setAttribute("year", calendarViewForm.getBaseDateBreakDown().getYear());
         ses.setAttribute("month", calendarViewForm.getBaseDateBreakDown().getMonth());
         ses.setAttribute("day", calendarViewForm.getBaseDateBreakDown().getDayOfMonth());
         ses.setAttribute("type", calendarViewForm.getBaseDateBreakDown().getType());
         ses.setAttribute("eventTypes", filter.getSelectedEventTypes());
         
         
         if(calendarViewForm.getPrint()){
             return mapping.findForward("print");
         }
        return mapping.findForward("success");
    }
}
