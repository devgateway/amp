package org.digijava.module.calendar.action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.calendar.dbentity.AmpEventType;
import org.digijava.module.calendar.entity.CalendarOptions;
import org.digijava.module.calendar.form.CalendarEventForm;
import org.digijava.module.calendar.util.AmpDbUtil;

public class PreviewCalendarEvent
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        CalendarEventForm calendarEventForm = (CalendarEventForm) form;
        try {
            // calendar type
            calendarEventForm.setSelectedCalendarTypeName(CalendarOptions.
                CALENDAR_TYPE_NAME[calendarEventForm.getSelectedCalendarTypeId()]);
            // event type
            Long selectedEventTypeId = calendarEventForm.getSelectedEventTypeId();
            AmpEventType eventType = AmpDbUtil.getEventType(selectedEventTypeId);
            calendarEventForm.setSelectedEventTypeName(eventType.getName());
            // donors
            String[] donorIds = calendarEventForm.getSelectedDonors();
            List donors = new ArrayList();
            if(donorIds != null && donorIds.length > 0) {
                for(int i = 0; i < donorIds.length; i++) {
                    AmpOrganisation donor = org.digijava.module.aim.util.DbUtil.
                        getOrganisation(Long.valueOf(donorIds[i]));
                    LabelValueBean lvb = new LabelValueBean(donor.getName(),
                        donorIds[i]);
                    donors.add(lvb);
                }
            }
            calendarEventForm.setDonors(donors);
            // attendee users
            List attendeeUsers = new ArrayList();
            String[] userIds = calendarEventForm.getSelectedAttendeeUsers();
            if(userIds != null && userIds.length > 0) {
                for(int i = 0; i < userIds.length; i++) {
                    User user = UserUtils.getUser(Long.valueOf(userIds[i]));
                    LabelValueBean lvb = new LabelValueBean(user.getFirstNames() +
                        " " + user.getLastName(), userIds[i]);
                    attendeeUsers.add(lvb);
                }
            }
            calendarEventForm.setAttendeeUsers(attendeeUsers);
            // attendee guests
            List attendeeGuests = new ArrayList();
            String[] guestNames = calendarEventForm.getSelectedAttendeeGuests();
            if(guestNames != null && guestNames.length > 0) {
                for(int i = 0; i < guestNames.length; i++) {
                    LabelValueBean lvb = new LabelValueBean(guestNames[i], guestNames[i]);
                    attendeeGuests.add(lvb);
                }
            }
            calendarEventForm.setAttendeeGuests(attendeeGuests);
        } catch(Exception ex) {
            return mapping.findForward("failure");
        }
        return mapping.findForward("success");
    }
}
