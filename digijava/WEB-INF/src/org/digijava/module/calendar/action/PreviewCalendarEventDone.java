package org.digijava.module.calendar.action;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.collections.CollectionUtils;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.entity.DateBreakDown;
import org.digijava.module.calendar.form.CalendarEventForm;
import org.digijava.module.calendar.util.AmpDbUtil;
import org.digijava.module.calendar.util.AmpUtil;
import org.digijava.module.common.dbentity.ItemStatus;

public class PreviewCalendarEventDone
    extends DispatchAction {

    public ActionForward save(ActionMapping mapping, ActionForm form,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {

        CalendarEventForm calendarEventForm = (CalendarEventForm) form;
        try {
            Long ampCalendarId = calendarEventForm.getAmpCalendarId();
            ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(
                request);
            String instanceId = moduleInstance.getInstanceName();
            String siteId = moduleInstance.getSite().getSiteId();
            AmpCalendar ampCalendar = AmpDbUtil.getAmpCalendar(ampCalendarId,
                instanceId, siteId);
            if(ampCalendar == null) {
                ampCalendar = new AmpCalendar(new Calendar());
            }
            Calendar calendar = ampCalendar.getCalendarPK().getCalendar();
            if(calendar.getId() == null) {
                CalendarItem calendarItem = new CalendarItem();
                calendarItem.setCalendar(calendar);
                /** @todo
                 *
                 * language - should be set;
                 * description - should be set;
                 *
                 **/
                calendarItem.setTitle(calendarEventForm.getEventTitle());
                calendarItem.setCreationIp(RequestUtils.getRemoteAddress(
                    request));
                calendarItem.setCreationDate(new Date());
                // fill calendar object
                HashSet calendarItems = new HashSet();
                calendarItems.add(calendarItem);
                calendar.setCalendarItem(calendarItems);
                calendar.setStatus(new ItemStatus(ItemStatus.PUBLISHED));
                calendar.setInstanceId(instanceId);
                calendar.setSiteId(siteId);
                // user
                User user = RequestUtils.getUser(request);
                ampCalendar.setUser(user);
            } else {
                calendar.getFirstCalendarItem().setTitle(calendarEventForm.
                    getEventTitle());
            }
            int selectedCalendarTypeId = calendarEventForm.
                getSelectedCalendarTypeId();
            GregorianCalendar startDate = DateBreakDown.
                createValidGregorianCalendar(selectedCalendarTypeId,
                                             calendarEventForm.
                                             getSelectedStartDate(),
                                             calendarEventForm.
                                             getSelectedStartTime());
//            startDate.set(startDate.HOUR_OF_DAY,
//                          startDate.getActualMinimum(startDate.HOUR_OF_DAY));
//            startDate.set(startDate.MINUTE,
//                          startDate.getActualMinimum(startDate.MINUTE));
            startDate.set(startDate.SECOND,
                          startDate.getActualMinimum(startDate.SECOND));
            calendar.setStartDate(startDate.getTime());
            GregorianCalendar endDate = DateBreakDown.
                createValidGregorianCalendar(selectedCalendarTypeId,
                                             calendarEventForm.
                                             getSelectedEndDate(),
                                             calendarEventForm.
                                             getSelectedEndTime());
//            endDate.set(endDate.HOUR_OF_DAY,
//                        endDate.getActualMaximum(endDate.HOUR_OF_DAY));
//            endDate.set(endDate.MINUTE, endDate.getActualMaximum(endDate.MINUTE));
            endDate.set(endDate.SECOND, endDate.getActualMaximum(endDate.SECOND));
            calendar.setEndDate(endDate.getTime());
            /* add amp specific attributes */
            // event type
            ampCalendar.setEventType(AmpDbUtil.getEventType(calendarEventForm.
                getSelectedEventTypeId()));
            // donors
            String[] selectedDonorIds = calendarEventForm.getSelectedDonors();
            Set selectedDonors = AmpUtil.getSelectedDonors(selectedDonorIds);
            ampCalendar.setDonors(selectedDonors);
            // attendee users
            String[] selectedAttendeeUserIds = calendarEventForm.
                getSelectedAttendeeUsers();
            Set selectedAttendeeUsers = AmpUtil.getSelectedAttendeeUsers(
                ampCalendar, selectedAttendeeUserIds);
            // attendee guests
            String[] selectedAttendeeGuestIds = calendarEventForm.
                getSelectedAttendeeGuests();
            Set selectedAttendeeGuests = AmpUtil.getSelectedAttendeeGuests(
                ampCalendar, selectedAttendeeGuestIds);
            // syncronize attendees
            HashSet newAttendees = new HashSet();
            newAttendees.addAll(selectedAttendeeUsers);
            newAttendees.addAll(selectedAttendeeGuests);
            HashSet oldAttendees = new HashSet();
            if(ampCalendar.getAttendees() != null) {
                oldAttendees.addAll(ampCalendar.getAttendees());
            }
            Set syncAttendees = CollectionUtils.synchronizeSets(oldAttendees,
                newAttendees, AmpUtil.attendeeSyncronizer);
            ampCalendar.setAttendees(syncAttendees);
            // private event
            ampCalendar.setPrivateEvent(calendarEventForm.isPrivateEvent());
            // update database
            AmpDbUtil.updateAmpCalendar(ampCalendar);
        } catch(Exception ex) {
            return mapping.findForward("failure");
        }
        return mapping.findForward("save");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        return mapping.findForward("edit");
    }

    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws
        Exception {
        return save(mapping, form, request, response);
    }
}
