package org.digijava.module.calendar.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
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
        	if(calendarEventForm.isReset()==true){
        		calendarEventForm=resetForm(calendarEventForm);
        	}
            if(calendarEventForm.getMethod()==null){
            	if(calendarEventForm.getIspreview()==0){
            		calendarEventForm=resetForm(calendarEventForm);
            		
            	}  else if(calendarEventForm.getIspreview()==1){
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
                    calendarEventForm.setIspreview(0);
            	}
            }else if(calendarEventForm.getMethod().equalsIgnoreCase("new")){
                calendarEventForm=resetForm(calendarEventForm);
                calendarEventForm.setAmpCalendarId(new Long(0));
            }

            // calendar type
            List calendarTypesList = DateNavigator.getCalendarTypes();

            Collection defCnISO=FeaturesUtil.getDefaultCountryISO();
            if(defCnISO!=null){
                AmpGlobalSettings sett=(AmpGlobalSettings)defCnISO.iterator().next();
                if(!sett.getGlobalSettingsValue().equalsIgnoreCase("et")){
                    for(Iterator iter = calendarTypesList.iterator(); iter.hasNext(); ) {
                        LabelValueBean item = (LabelValueBean) iter.next();
                        if(item.getLabel().equalsIgnoreCase("ethiopian") ||
                            item.getLabel().equalsIgnoreCase("ethiopian fy")){
                            iter.remove();
                        }
                    }
                }
            }

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
              if(calendarEventForm.getMethod()!=null&&calendarEventForm.getMethod().equals("preview")){
                List attendeeGuests = new ArrayList();
                List attendeeUsers = new ArrayList();
                List<String> selUsersId=new ArrayList<String>();
                List selUsers = new ArrayList();
                Set attendees = ampCalendar.getAttendees();
                if (attendees != null) {
                  LabelValueBean lvb = null;
                  LabelValueBean lvbs = null;
                  Iterator it = attendees.iterator();
                  while (it.hasNext()) {
                    AmpCalendarAttendee attendee = (AmpCalendarAttendee) it.
                        next();
                    User user = attendee.getUser();

                    String guest = attendee.getGuest();
                    if (guest != null) {
                      lvb = new LabelValueBean(guest, guest);
                      lvbs = new LabelValueBean(guest,
                                                "g:" + guest);
                      attendeeGuests.add(lvb);
                    }
                    else {
                      if (user != null) {
                        String userId = user.getId().toString();
                        selUsersId.add("u:"+userId);
                        lvb = new LabelValueBean(user.getFirstNames() + " " +
                                                 user.getLastName(), userId);
                        lvbs = new LabelValueBean(user.getFirstNames() + " " +
                                                  user.getLastName(),
                                                  "u:" + userId);
                        attendeeUsers.add(lvb);
                      }
                    }
                    selUsers.add(lvbs);
                  }
                }
                String[] selUsr=selUsersId.toArray(new String[0]);
                calendarEventForm.setSelectedUsers(selUsr);
                calendarEventForm.setAttendeeUsers(attendeeUsers);
                calendarEventForm.setAttendeeGuests(attendeeGuests);
                calendarEventForm.setSelectedUsersList(selUsers);
                calendarEventForm.setSelectedEventTypeName(ampCalendar.
                    getEventType().
                    getName());
                Calendar calendar = ampCalendar.getCalendarPK().getCalendar();
                // title
                calendarEventForm.setEventTitle(calendar.getFirstCalendarItem().
                                                getTitle());
                // selected event type
                calendarEventForm.setSelectedEventTypeId(ampCalendar.
                    getEventType().getId());
                // private event
                    calendarEventForm.setPrivateEvent(ampCalendar.isPrivateEvent());
                   
                // selected donors
               // if(calendarEventForm.getDonors()==null){
                  calendarEventForm.setDonors(new ArrayList());
               // }
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
                 return mapping.findForward("preview");

              }


                // attendee users
              if(calendarEventForm.getMethod()==null) {
                    Set attendees = ampCalendar.getAttendees();
                    Set selectedAttendeeUsers = new HashSet();
                    Set selectedAttendeeGuests = new HashSet();
                    Set selectedUsers = new HashSet();
                    if(attendees != null) {  
                        Iterator it = attendees.iterator();
                        while(it.hasNext()) {
                            AmpCalendarAttendee attendee = (AmpCalendarAttendee) it.
                                    next();
                            if(attendee.getUser() != null) {
                                selectedAttendeeUsers.add(attendee.getUser().getId().toString());
                                selectedUsers.add(new String("u:" + attendee.getUser().getId().toString()));

                            } else if(attendee.getGuest() != null) {
                                selectedAttendeeGuests.add(attendee.getGuest());
                                selectedUsers.add(new String("g:" + attendee.getGuest()));
                            } else {
                                // ignore invalid attendee
                                continue;
                            }
                        }
                    } if(calendarEventForm.getSelectedUsers()!=null){
                    	String[] selUsers = calendarEventForm.getSelectedUsers();
                    	for (int i = 0; i < selUsers.length; i++) {
                   		 if(selUsers[i].substring(0,2).equals("g:")){
                   				selectedUsers.add(selUsers[i]);
                            }   else if(selUsers[i].substring(0,2).equals("u:")){
                            	selectedUsers.add(new String(selUsers[i].toString()));
                            }
                   	 }
                   	        
                    }
                    // selected attendee users
                    calendarEventForm.setSelectedAttendeeUsers((String[]) selectedAttendeeUsers.toArray(new String[0]));
                    // selected attendee guests
                    calendarEventForm.setSelectedAttendeeGuests((String[]) selectedAttendeeGuests.toArray(new String[0]));

                    calendarEventForm.setSelectedUsers((String[]) selectedUsers.toArray(new String[0]));
                    // private event
                    calendarEventForm.setPrivateEvent(ampCalendar.isPrivateEvent());
                }
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
            List selectedUsersList=new ArrayList();
            List ampUsers=AmpDbUtil.getUsers();
            if (ampUsers != null && ampUsers.size() != 0) {
                Iterator userIt = ampUsers.iterator();
                List attendeeUsers = new ArrayList();
                while (userIt.hasNext()) {
                    User user = (User) userIt.next();
                    if (!user.isBanned()) {
                        LabelValueBean lvb = new LabelValueBean(user.getFirstNames() +
                            " " + user.getLastName(), user.getId().toString());
                        attendeeUsers.add(lvb);

                        String[] selUsers = calendarEventForm.getSelectedUsers(); 
                        if (selUsers != null) {
                            for (int i = 0; i < selUsers.length; i++) {
                                String uid = "u:" + user.getId().toString();
                                if (uid.equals(selUsers[i])) {
                                    LabelValueBean lvbs = new LabelValueBean(user.getFirstNames() + " " + user.getLastName(), uid);
                                    selectedUsersList.add(lvbs);
                                    break;
                                }
                            }
                        }
                    }
                }
                calendarEventForm.setAttendeeUsers(attendeeUsers);
            }

            // attendee guests
            List attendeeGuests = new ArrayList();
			Set guestsList = new HashSet();
			if(calendarEventForm.getMethod()!=null){
				if (calendarEventForm.getSelectedAttendeeGuests() != null
						&& calendarEventForm.getSelectedAttendeeGuests().length > 0) {
					String[] guests = calendarEventForm.getSelectedAttendeeGuests();
					for (int i = 0; i < guests.length; i++) {
						LabelValueBean lvb = new LabelValueBean(guests[i],
								guests[i]);
						attendeeGuests.add(lvb);
						LabelValueBean lvbs = new LabelValueBean(guests[i],
								new String("g:" + guests[i]));
						guestsList.add(lvbs);
					}
					calendarEventForm.setSelectedAttendeeGuests(null);
				}
			}else {
				if (calendarEventForm.getSelectedUsers() != null) {
					String[] selUsers = calendarEventForm.getSelectedUsers();
					for (int i = 0; i < selUsers.length; i++) {
						if (selUsers[i].substring(0, 2).equals("g:")) {
							String user = new String(selUsers[i].substring(2,
									selUsers[i].length()));
							LabelValueBean lvbs = new LabelValueBean(user,
									selUsers[i]);
							guestsList.add(lvbs);
						}
					}

				}
			}

           	 selectedUsersList.addAll(guestsList);        

            calendarEventForm.setSelectedUsersList(selectedUsersList);
            calendarEventForm.setAttendeeGuests(attendeeGuests);

            calendarEventForm.setMethod(null);
        } catch(Exception ex) {
          ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return mapping.findForward("success");
    }

    
    private CalendarEventForm resetForm(CalendarEventForm calendarEventForm) {
    	calendarEventForm.setPrivateEvent(true);
    	calendarEventForm.setIspreview(0);
    	calendarEventForm.setReset(false);
    	calendarEventForm.setAttendeeGuests(null);
        calendarEventForm.setAttendeeUsers(null);
        calendarEventForm.setCalendarTypes(null);
        calendarEventForm.setDonors(null);
        calendarEventForm.setEndDate(null);
        calendarEventForm.setEndDateBreakDown(null);
        calendarEventForm.setEventTitle(null);
        calendarEventForm.setEventTypesList(null);
//      calendarEventForm.setMethod(null);
        calendarEventForm.setStartDate(null);
        calendarEventForm.setStartDateBreakDown(null);

        calendarEventForm.setSelectedUsers(null);
        calendarEventForm.setSelectedUsersList(null);

        calendarEventForm.setSelectedAttendeeGuests(null);
        calendarEventForm.setSelectedAttendeeUsers(null);

        calendarEventForm.setSelectedCalendarTypeName(null);
        calendarEventForm.setSelectedDonors(null);
        calendarEventForm.setSelectedEndDate(null);
        calendarEventForm.setSelectedEndTime(null);
        calendarEventForm.setSelectedEventTypeId(null);
        calendarEventForm.setSelectedEventTypeName(null);
        calendarEventForm.setSelectedStartDate(null);
        calendarEventForm.setSelectedStartTime(null);
        return calendarEventForm;
    }
}
