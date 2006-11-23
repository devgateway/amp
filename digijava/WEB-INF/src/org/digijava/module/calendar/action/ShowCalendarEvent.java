package org.digijava.module.calendar.action;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendarAttendee;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.entity.CalendarOptions;
import org.digijava.module.calendar.entity.DateBreakDown;
import org.digijava.module.calendar.entity.DateNavigator;
import org.digijava.module.calendar.form.CalendarEventForm;
import org.digijava.module.calendar.util.AmpDbUtil;

public class ShowCalendarEvent
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        CalendarEventForm calendarEventForm = (CalendarEventForm) form;
        try {
            // calendar type
            List calendarTypesList = DateNavigator.getCalendarTypes();
            calendarEventForm.setCalendarTypes(calendarTypesList);
            // selected calendar type
            int selectedCalendarType = calendarEventForm.
                getSelectedCalendarTypeId();
            if(selectedCalendarType != CalendarOptions.CALENDAR_TYPE_GREGORIAN &&
               selectedCalendarType != CalendarOptions.CALENDAR_TYPE_ETHIOPIAN &&
               selectedCalendarType !=
               CalendarOptions.CALENDAR_TYPE_ETHIOPIAN_FY) {
                selectedCalendarType = CalendarOptions.defaultCalendarType;
                calendarEventForm.setSelectedCalendarTypeId(
                    selectedCalendarType);
            }
            // populate values from database if in edit mode
            Long ampCalendarId = calendarEventForm.getAmpCalendarId();
            ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(
                request);
            String instanceId = moduleInstance.getInstanceName();
            String siteId = moduleInstance.getSite().getSiteId();
            AmpCalendar ampCalendar = AmpDbUtil.getAmpCalendar(ampCalendarId,
                instanceId, siteId);
            if(ampCalendar != null) {
                Calendar calendar = ampCalendar.getCalendarPK().getCalendar();
                // title
                calendarEventForm.setEventTitle(calendar.getFirstCalendarItem().
                                                getTitle());
                // selected event type
                calendarEventForm.setSelectedEventTypeId(ampCalendar.
                    getEventType().getId());
                // selected donors
                if(calendarEventForm.getDonors()==null){
                    calendarEventForm.setDonors(new ArrayList());
                    if(ampCalendar.getDonors() != null) {
                        Iterator it = ampCalendar.getDonors().iterator();
                        while(it.hasNext()) {
                            AmpOrganisation donor = (AmpOrganisation) it.next();
                            LabelValueBean lvb = new LabelValueBean(donor.
                                getName(),
                                donor.getAmpOrgId().toString());
                            calendarEventForm.getDonors().add(lvb);
                        }
//                    String[] donorIds = new String[ampCalendar.getDonors().size()];
//                    int counter = 0;
//                    while(it.hasNext()) {
//                        AmpOrganisation donor = (AmpOrganisation) it.next();
//                        donorIds[counter++] = donor.getAmpOrgId().toString();
//                    }
//                    calendarEventForm.setSelectedDonors(donorIds);
                    }
                }
                // selected start date
                GregorianCalendar startDate = new GregorianCalendar();
                startDate.setTime(calendar.getStartDate());
                DateBreakDown startDateBreakDown = new DateBreakDown(startDate,
                    selectedCalendarType);
                calendarEventForm.setSelectedStartDate(startDateBreakDown.
                    formatDateString());
                calendarEventForm.setSelectedStartTime(startDateBreakDown.
                    formatTimeString());
                // selected end date
                GregorianCalendar endDate = new GregorianCalendar();
                endDate.setTime(calendar.getEndDate());
                DateBreakDown endDateBreakDown = new DateBreakDown(endDate,
                    selectedCalendarType);
                calendarEventForm.setSelectedEndDate(endDateBreakDown.
                    formatDateString());
                calendarEventForm.setSelectedEndTime(endDateBreakDown.
                    formatTimeString());
                // attendee users
                Set attendees = ampCalendar.getAttendees();
                Set selectedAttendeeUsers = new HashSet();
                Set selectedAttendeeGuests = new HashSet();
                if(attendees != null) {
                    Iterator it = attendees.iterator();
                    while(it.hasNext()) {
                        AmpCalendarAttendee attendee = (AmpCalendarAttendee) it.
                            next();
                        if(attendee.getUser() != null) {
                            selectedAttendeeUsers.add(attendee.getUser().getId().
                                toString());
                        } else if(attendee.getGuest() != null) {
                            selectedAttendeeGuests.add(attendee.getGuest());
                        } else {
                            // ignore invalid attendee
                            continue;
                        }
                    }
                }
                // selected attendee users
                calendarEventForm.setSelectedAttendeeUsers((String[])
                    selectedAttendeeUsers.toArray(new String[0]));
                // selected attendee guests
                calendarEventForm.setSelectedAttendeeGuests((String[])
                    selectedAttendeeGuests.toArray(new String[0]));
                // private event
                calendarEventForm.setPrivateEvent(ampCalendar.isPrivateEvent());
            }
            // event types list
            List eventTypesList = AmpDbUtil.getEventTypes();
            calendarEventForm.setEventTypesList(eventTypesList);
            // donors
//            Collection organisations = org.digijava.module.aim.util.DbUtil.
//                getDonors();
//            List donors = new ArrayList();
//            Iterator it = organisations.iterator();
//            while(it.hasNext()) {
//                AmpOrganisation org = (AmpOrganisation) it.next();
//                LabelValueBean lvb = new LabelValueBean(org.getName(),
//                    org.getAmpOrgId().toString());
//                donors.add(lvb);
//            }
//            calendarEventForm.setDonors(donors);
            // start date
            GregorianCalendar startDate = DateBreakDown.
                createValidGregorianCalendar(selectedCalendarType,
                                             calendarEventForm.
                                             getSelectedStartDate(),
                                             calendarEventForm.
                                             getSelectedStartTime());
            calendarEventForm.setStartDate(startDate);
            DateBreakDown startDateBreakDown = new DateBreakDown(startDate,
                selectedCalendarType);
            calendarEventForm.setStartDateBreakDown(startDateBreakDown);
            calendarEventForm.setSelectedStartDate(startDateBreakDown.
                formatDateString());
            calendarEventForm.setSelectedStartTime(startDateBreakDown.
                formatTimeString());
            // stop date
            GregorianCalendar endDate = DateBreakDown.
                createValidGregorianCalendar(selectedCalendarType,
                                             calendarEventForm.
                                             getSelectedEndDate(),
                                             calendarEventForm.
                                             getSelectedEndTime());
            calendarEventForm.setEndDate(endDate);
            DateBreakDown endDateBreakDown = new DateBreakDown(endDate,
                selectedCalendarType);
            calendarEventForm.setEndDateBreakDown(endDateBreakDown);
            calendarEventForm.setSelectedEndDate(endDateBreakDown.
                                                 formatDateString());
            calendarEventForm.setSelectedEndTime(endDateBreakDown.
                                                 formatTimeString());
            // attendee users
            Iterator userIt = AmpDbUtil.getUsers().iterator();
            List attendeeUsers = new ArrayList();
            while(userIt.hasNext()) {
                User user = (User) userIt.next();
                LabelValueBean lvb = new LabelValueBean(user.getFirstNames() +
                    " " +
                    user.getLastName(), user.getId().toString());
                attendeeUsers.add(lvb);
            }
            calendarEventForm.setAttendeeUsers(attendeeUsers);
            // attendee guests
            List attendeeGuests = new ArrayList();
            if(calendarEventForm.getSelectedAttendeeGuests() != null) {
                String[] guests = calendarEventForm.getSelectedAttendeeGuests();
                for(int i = 0; i < guests.length; i++) {
                    LabelValueBean lvb = new LabelValueBean(guests[i], guests[i]);
                    attendeeGuests.add(lvb);
                }
                calendarEventForm.setSelectedAttendeeGuests(null);
            }
            calendarEventForm.setAttendeeGuests(attendeeGuests);
        } catch(Exception ex) {
            return mapping.findForward("failure");
        }
        return mapping.findForward("success");
    }
}
