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
        	calendarEventForm.setPrivateEvent(calendarEventForm.isPrivateEvent());        	
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


            List attendeeGuests = new ArrayList();
            List attendeeUsers = new ArrayList();
            List selUsers =new ArrayList();

            String[] userIds = calendarEventForm.getSelectedUsers();
            if(userIds != null && userIds.length > 0) {
                LabelValueBean lvb=null;
                LabelValueBean lvbs=null;
                String userId=null;
                for(int i = 0; i < userIds.length; i++) {
                    if(userIds[i].substring(0,2).equals("u:")){
                        userId=new String(userIds[i].substring(2,userIds[i].length()));
                        User user = UserUtils.getUser(Long.valueOf(userId));
                        lvb = new LabelValueBean(user.getFirstNames() + " " + user.getLastName(), userId);
                        lvbs=new LabelValueBean(user.getFirstNames() + " " + user.getLastName(), userIds[i]);
                        attendeeUsers.add(lvb);
                    }else if(userIds[i].substring(0,2).equals("g:")){
                        userId=new String(userIds[i].substring(2,userIds[i].length()));
                        lvb = new LabelValueBean(userId, userId);
                        lvbs=new LabelValueBean(userId, userIds[i]);
                        attendeeGuests.add(lvb);
                    }
                    selUsers.add(lvbs);
                }
            }
            calendarEventForm.setAttendeeUsers(attendeeUsers);
            calendarEventForm.setAttendeeGuests(attendeeGuests);
            calendarEventForm.setSelectedUsersList(selUsers);

        } catch(Exception ex) {
            return mapping.findForward("failure");
        }
        return mapping.findForward("success");
    }
}
