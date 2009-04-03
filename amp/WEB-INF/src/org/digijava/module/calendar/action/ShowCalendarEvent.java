package org.digijava.module.calendar.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Team;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendarAttendee;
import org.digijava.module.calendar.dbentity.AmpCalendarPK;
import org.digijava.module.calendar.dbentity.AmpEventType;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.dbentity.RecurrCalEvent;
import org.digijava.module.calendar.entity.CalendarOptions;
import org.digijava.module.calendar.entity.DateBreakDown;
import org.digijava.module.calendar.entity.DateNavigator;
import org.digijava.module.calendar.form.CalendarEventForm;
import org.digijava.module.calendar.util.AmpDbUtil;
import org.digijava.module.common.dbentity.ItemStatus;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.triggers.CalendarEventSaveTrigger;
import org.digijava.module.message.triggers.RemoveCalendarEventTrigger;
import org.digijava.module.message.util.AmpMessageUtil;


public class ShowCalendarEvent extends Action {
	
	private static Logger logger = Logger.getLogger(ShowCalendarEvent.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
        CalendarEventForm ceform = (CalendarEventForm) form;
        if (ceform.getMethod().equalsIgnoreCase("new")) {
            ceform.reset(mapping, request);
            ceform.setOrganizations(null);
            ceform.setSelOrganizations(null);
        }
        else if(ceform.getMethod().equalsIgnoreCase("removeOrg")) 
        {
        	Long[] listSelectedOrganizations = ceform.getSelOrganizations();
            if (listSelectedOrganizations != null && listSelectedOrganizations.length > 0) {
                Collection<AmpOrganisation> colOrganizations = ceform.getOrganizations();
                Collection<AmpOrganisation> newColOrganizations = new ArrayList<AmpOrganisation>();
                newColOrganizations.addAll(colOrganizations);

                Iterator<AmpOrganisation> itOrgs = colOrganizations.iterator();
                while (itOrgs.hasNext()) {
                    AmpOrganisation currentOrg = itOrgs.next();
                    for (int index = 0; index < listSelectedOrganizations.length; index++) {
                        if (currentOrg.getAmpOrgId().equals(listSelectedOrganizations[index])) {
                            newColOrganizations.remove(currentOrg);
                            break;
                        }
                    }
                }
                ceform.setOrganizations(newColOrganizations);
            }
        }
        	
        // calendar type
        Collection calendarTypesList = DateNavigator.getCalendarTypes();
        Collection defCnISO = FeaturesUtil.getDefaultCountryISO();
        if (defCnISO != null) {
            AmpGlobalSettings sett = (AmpGlobalSettings) defCnISO.iterator().next();
            if (!sett.getGlobalSettingsValue().equalsIgnoreCase("et")) {
                for (Iterator iter = calendarTypesList.iterator(); iter.hasNext(); ) {
                    LabelValueBean item = (LabelValueBean) iter.next();
                    if (item.getLabel().equalsIgnoreCase("ethiopian") ||
                        item.getLabel().equalsIgnoreCase("ethiopian fy")) {
                        iter.remove();
                    }
                }
            }
        }

        ceform.setCalendarTypes(calendarTypesList);
        ceform.setEventTypesList(AmpDbUtil.getEventTypes());
        if (ceform.getSelectedEventTypeId() != null && ceform.getSelectedEventTypeId() > 0) {
            AmpEventType eventType = AmpDbUtil.getEventType(ceform.getSelectedEventTypeId());
            if (eventType != null) {
                ceform.setSelectedEventTypeName(eventType.getName());
            }
        }
        // selected calendar type
        Long selectedCalendarTypeId = ceform.getSelectedCalendarTypeId();
        if (selectedCalendarTypeId == null ||
            (!selectedCalendarTypeId.equals(CalendarOptions.CALENDAR_TYPE_GREGORIAN) &&
             !selectedCalendarTypeId.equals(CalendarOptions.CALENDAR_TYPE_ETHIOPIAN) &&
             !selectedCalendarTypeId.equals(CalendarOptions.CALENDAR_TYPE_ETHIOPIAN_FY))) {
            selectedCalendarTypeId = Long.valueOf(CalendarOptions.defaultCalendarType);
            ceform.setSelectedCalendarTypeId(selectedCalendarTypeId);
        }

        ceform.setTeamsMap(loadRecepients());

        String[] slAtts = ceform.getSelectedAtts();
        if (slAtts != null) {
            Collection<LabelValueBean> selectedAttsCol = new ArrayList<LabelValueBean> ();
            for (int i = 0; i < slAtts.length; i++) {
                if (slAtts[i].startsWith("t:")) {
                    AmpTeam team = TeamUtil.getAmpTeam(Long.valueOf(slAtts[i].substring(2)));
                    if (team != null) {
                        selectedAttsCol.add(new LabelValueBean("---"+team.getName()+"---", slAtts[i]));
                    }
                } else
                	if (slAtts[i].startsWith("m:")) {
                    AmpTeamMember member = TeamMemberUtil.getAmpTeamMember(Long.valueOf(slAtts[i].substring(2)));
                    if (member != null) {
                        selectedAttsCol.add(new LabelValueBean(member.getUser().getFirstNames() + " " + member.getUser().getLastName(), slAtts[i]));
                    }
                } else if (slAtts[i].startsWith("g:")) {
                    selectedAttsCol.add(new LabelValueBean(slAtts[i].substring(2), slAtts[i]));
                }
            }
            ceform.setSelectedAttsCol(selectedAttsCol);
        }

//        String[] slOrgs = ceform.getSelectedEventOrganisations();
//        
//        if (slOrgs != null) {
//            Collection<LabelValueBean> selectedOrgsCol = new ArrayList<LabelValueBean> ();
//            for (int i = 0; i < slOrgs.length; i++) {
//                AmpOrganisation org = DbUtil.getOrganisation(Long.valueOf(slOrgs[i]));
//                if (org != null) {
//                    selectedOrgsCol.add(new LabelValueBean(org.getName(), slOrgs[i]));
//                }
//            }
//            ceform.setSelectedEventOrganisationsCol(selectedOrgsCol);
//        }
        
        Collection<AmpOrganisation> organizations = ceform.getOrganizations();
        if(organizations == null)
        	ceform.setOrganizations(new ArrayList<AmpOrganisation>());
        
        
        if (ceform.getMethod().equalsIgnoreCase("new")) {
            ceform.setAmpCalendarId(null);
            ceform.setActionButtonsVisible(true);
        } else if (ceform.getMethod().equalsIgnoreCase("edit")) {
            loadAmpCalendar(ceform, request);
        } else if (ceform.getMethod().equalsIgnoreCase("save")) {
        	String stDate=ceform.getSelectedStartDate() + " " + ceform.getSelectedStartTime();
        	String endDate=ceform.getSelectedEndDate()+ " " + ceform.getSelectedEndTime();
        	ActionErrors errors=validateDate(stDate,endDate);
        	errors.add(validateEventInformation(ceform.getEventTitle(),ceform.getSelectedAtts()));
        	if(!errors.isEmpty()){
        		saveErrors(request, errors);
        		return mapping.findForward("success");        		
        	}else{
        		saveAmpCalendar(ceform, request);
                ceform.setMethod("");
                request.setAttribute("calendarEventCreated", !ceform.isPrivateEvent());
                return mapping.findForward("forward");
        	}            

        } else if (ceform.getMethod().equalsIgnoreCase("delete")) {
        	AmpCalendar ampCalendar=AmpDbUtil.getAmpCalendar(ceform.getAmpCalendarId());
        	//get current member
        	HttpSession ses = request.getSession();
            TeamMember mem = (TeamMember) ses.getAttribute("currentMember");
            /**
        	 * if event belongs to several users,then deleting event should delete only link between the user that clicked delete button and event,
        	 * but for other users that event should remain,unless creator decides to remove it.   
        	 */
            if(ampCalendar.getMember()!=null && mem.getMemberId().equals(ampCalendar.getMember().getAmpTeamMemId())){
            	AmpDbUtil.deleteAmpCalendar(ceform.getAmpCalendarId());
            	new RemoveCalendarEventTrigger(ampCalendar);
            }else{
            	if(ampCalendar.getAttendees()!=null && ampCalendar.getAttendees().size()>0){        		
            		for (Object obj : ampCalendar.getAttendees()) {
            			AmpCalendarAttendee attendee=(AmpCalendarAttendee)obj;
            			if(attendee.getMember()!=null && attendee.getMember().getAmpTeamMemId().equals(mem.getMemberId())){
            				ampCalendar.getAttendees().remove(attendee);
            				AmpDbUtil.updateAmpCalendar(ampCalendar);
                			//delete that Attendee
                			AmpDbUtil.deleteAmpCalendarAttendee(attendee);
            				break;
            			}
    				}
            	}else{
            		AmpDbUtil.deleteAmpCalendar(ceform.getAmpCalendarId());
            	}
            }           
            ceform.setMethod("");
            return mapping.findForward("forward");

        } else if (ceform.getMethod().equalsIgnoreCase("preview")) {
        	String stDate=ceform.getSelectedStartDate() + " " + ceform.getSelectedStartTime();
        	String endDate=ceform.getSelectedEndDate()+ " " + ceform.getSelectedEndTime();
        	ActionErrors errors=new ActionErrors();
        	if(ceform.getAmpCalendarId()==null || !ceform.isResetForm()){
        		errors=validateDate(stDate,endDate);
        		errors.add(validateEventInformation(ceform.getEventTitle(),ceform.getSelectedAtts()));
        	}        	
        	if(!errors.isEmpty()){
        		saveErrors(request, errors);
        		return mapping.findForward("success");        		
        	}else{
        		loadAmpCalendar(ceform, request);
        		if(ceform.getAmpCalendarId()!=null && ceform.getAmpCalendarId() > 0){ //<--this means that user is not creating new event, but previewing old one
        			ceform.setActionButtonsVisible(false);
            		//get current member
            		HttpSession ses = request.getSession();
                    TeamMember mem = (TeamMember) ses.getAttribute("currentMember");
            		String[] selattendeess=ceform.getSelectedAtts();
            		if(ceform.getEventCreatorId()!=null && ceform.getEventCreatorId().equals(mem.getMemberId())){
            			ceform.setActionButtonsVisible(true);
            		}else if(ceform.isPrivateEvent()){
            			for (String attendee : selattendeess) {
    						if(attendee.startsWith("m:") && attendee.substring(attendee.indexOf(":")+1).equals(mem.getMemberId().toString())){
    							ceform.setActionButtonsVisible(true);
    							break;
    						}
    					}
            		}
        		}        		
                ceform.setMethod("");
                return mapping.findForward("preview");
        	}
        }

        return mapping.findForward("success");
    }

    private void saveAmpCalendar(CalendarEventForm ceform, HttpServletRequest request) throws Exception{
        try {
        	
        	if (ceform.getAmpCalendarId() != null && ceform.getAmpCalendarId() > 0) {
                AmpDbUtil.deleteAmpCalendar(ceform.getAmpCalendarId());
            }

            AmpCalendar ampCalendar = new AmpCalendar();

            AmpEventType eventType = AmpDbUtil.getEventType(ceform.getSelectedEventTypeId());
            ampCalendar.setEventType(eventType);

            if(ampCalendar.getMember()==null){
                HttpSession ses = request.getSession();
                TeamMember mem = (TeamMember) ses.getAttribute("currentMember");
                AmpTeamMember calMember = TeamMemberUtil.getAmpTeamMember(mem.getMemberId());
                ampCalendar.setMember(calMember);
            }

            ampCalendar.setOrganisations( new HashSet<AmpOrganisation>(ceform.getOrganizations()));            
           
            Set<AmpCalendarAttendee> atts =  new HashSet<AmpCalendarAttendee>();
            String[] slAtts = ceform.getSelectedAtts();
            if (slAtts != null) {
            	 //getting settings for message
            	AmpMessageSettings settings=AmpMessageUtil.getMessageSettings();
                for (int i = 0; i < slAtts.length; i++) {
                    AmpCalendarAttendee att = new AmpCalendarAttendee();
                    att.setAmpCalendar(ampCalendar); 
                    	if (slAtts[i].startsWith("m:")) {
                        AmpTeamMember member = TeamMemberUtil.getAmpTeamMember(Long.valueOf(slAtts[i].substring(2)));
                        att.setMember(member);
                        atts.add(att);
                    } else if (slAtts[i].startsWith("g:")) {
                        att.setGuest(slAtts[i]);
                        atts.add(att);
                    }                    
                }
            }
            ampCalendar.setAttendees(atts);

            AmpCalendarPK calPK = ampCalendar.getCalendarPK();

            if (calPK == null) {
                calPK = new AmpCalendarPK(new Calendar());
            }

            Calendar calendar = calPK.getCalendar();
            // title
            Set calendarItems =new HashSet();
            CalendarItem calendarItem = new CalendarItem();
            calendarItem.setCalendar(calendar);
            calendarItem.setTitle(ceform.getEventTitle());
            calendarItem.setCreationIp(RequestUtils.getRemoteAddress(request));
            calendarItem.setCreationDate(new Date());
            calendarItem.setDescription(ceform.getDescription());
            
            // fill calendar object

            calendarItems.add(calendarItem);
            calendar.setCalendarItem(calendarItems);

            if(ceform.getSelectedStartMonth() != null){           
	            Set recEvent =new HashSet();
	            RecurrCalEvent recurrEvent = new RecurrCalEvent();
	            recurrEvent.setCalendar(calendar);
	            recurrEvent.setRecurrPeriod(ceform.getRecurrPeriod());
	            recurrEvent.setSelectedStartMonth(ceform.getSelectedStartMonth());
	            recurrEvent.setTypeofOccurrence(ceform.getTypeofOccurrence());
	            recurrEvent.setOccurrWeekDays(ceform.getWeekDays());

                // selected start date and selected end date
                String dtformat = FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);
	            if (dtformat == null) {
	                dtformat = "dd/MM/yyyy";
	            }
                 SimpleDateFormat sdf = new SimpleDateFormat(dtformat);
                 Date recurrStartDate=null;
                 Date recurrEndDate= null;
                 if(ceform.getRecurrStartDate()!=null && !ceform.getRecurrStartDate().equals("")){
                	 recurrStartDate=sdf.parse(ceform.getRecurrStartDate());
                 }
                 if(ceform.getRecurrEndDate()!=null && !ceform.getRecurrEndDate().equals("")){
                	recurrEndDate= sdf.parse(ceform.getRecurrEndDate());
                 }
                recurrEvent.setRecurrStartDate(recurrStartDate);
                recurrEvent.setRecurrEndDate(recurrEndDate);              


	            recEvent.add(recurrEvent);
	            calendar.setRecurrCalEvent(recEvent);
            }
            
            //status
            calendar.setStatus(new ItemStatus(ItemStatus.PUBLISHED));

            ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);
            calendar.setInstanceId(moduleInstance.getInstanceName());
            calendar.setSiteId(moduleInstance.getSite().getSiteId());

            // selected start date and selected end date
            String dtformat = FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);            
            if (dtformat == null) {
                dtformat = "dd/MM/yyyy";
            }
            
            //date from calendar comes in this format
            dtformat+=" HH:mm";

            SimpleDateFormat sdf = new SimpleDateFormat(dtformat);

            String startDateTime = ceform.getSelectedStartDate() + " " + ceform.getSelectedStartTime();
            String endDateTime = ceform.getSelectedEndDate() + " " + ceform.getSelectedEndTime();

            calendar.setStartDate(sdf.parse(startDateTime));
            calendar.setEndDate(sdf.parse(endDateTime));

            calPK.setCalendar(calendar);
            ampCalendar.setCalendarPK(calPK);
            //Private Event checkbox label states "Public Event"            
            ampCalendar.setPrivateEvent(ceform.isPrivateEvent());

            AmpDbUtil.updateAmpCalendar(ampCalendar);
            //Create new calendar event alert           
            CalendarEventSaveTrigger cet=new CalendarEventSaveTrigger(ampCalendar);
           
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void loadAmpCalendar(CalendarEventForm ceform, HttpServletRequest request) {
        if (ceform.getAmpCalendarId() != null &&
            ceform.getAmpCalendarId() > 0 &&
            ceform.isResetForm()) {

            Long ampCalendarId = ceform.getAmpCalendarId();
            ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);
            String instanceId = moduleInstance.getInstanceName();
            String siteId = moduleInstance.getSite().getSiteId();
            AmpCalendar ampCalendar = AmpDbUtil.getAmpCalendar(ampCalendarId, instanceId, siteId);

            if (ampCalendar != null) {
                Collection<LabelValueBean> selectedAttsCol = new ArrayList<LabelValueBean> ();

                Collection<String> selAtts = new ArrayList<String> ();
                Set<AmpTeam> teams = new HashSet<AmpTeam> ();
                Collection<AmpTeamMember> members = new ArrayList<AmpTeamMember> ();
                Collection<String> guests = new ArrayList<String> ();
                //event creator
                if(ampCalendar.getMember()!=null){
                	ceform.setEventCreatorId(ampCalendar.getMember().getAmpTeamMemId());
                }
                if (ampCalendar.getAttendees() != null) {
                    //LabelValueBean lvb = null;
                    //List<AmpCalendarAttendee> atts=AmpDbUtil.getAmpCalendarAttendees(ampCalendar)
                    Iterator attItr = ampCalendar.getAttendees().iterator();
                    while (attItr.hasNext()) {
                        AmpCalendarAttendee attendee = (AmpCalendarAttendee) attItr.next();

                        AmpTeamMember member = attendee.getMember();
                        AmpTeam team = attendee.getTeam();
                        String guest = attendee.getGuest();

                        if (member != null) {
                        	if(!members.contains(member)){
                        		members.add(member);
                        	}
                        	if(!teams.contains(member.getAmpTeam())){
                        		teams.add(member.getAmpTeam());
                        	}                        	
                        } else if(guest!=null){
                        	guests.add(guest);
                        }
                        //else if (team != null) {
                        //	teams.add(team);                        	
                        //} else {
                        //	guests.add(guest);
                        //}
                    }                   
                    
                }
                
                for(AmpTeam team : teams){
    				LabelValueBean teamLabel=new LabelValueBean("---"+team.getName()+"---","t:"+team.getAmpTeamId().toString());
    				selectedAttsCol.add(teamLabel);
    				selAtts.add("t:" + team.getAmpTeamId().toString());
    				for(AmpTeamMember member : members){
    					if(team.getAmpTeamId().longValue()==member.getAmpTeam().getAmpTeamId().longValue()){
    						LabelValueBean tm=new LabelValueBean(member.getUser().getFirstNames() + " " + member.getUser().getLastName(),"m:" + member.getAmpTeamMemId().toString());
    						selectedAttsCol.add(tm);
    						selAtts.add("m:" + member.getAmpTeamMemId().toString());
    					}
    				}
    			}
                
                for(String guest: guests){
                	selectedAttsCol.add(new LabelValueBean(guest, "g:" + guest));
                	selAtts.add("g:" + guest);
                }

                ceform.setSelectedAttsCol(selectedAttsCol);

                ceform.setSelectedAtts( (String[]) selAtts.toArray(new String[selAtts.size()]));               

                Calendar calendar = ampCalendar.getCalendarPK().getCalendar();
                // title
                ceform.setEventTitle(calendar.getFirstCalendarItem().getTitle());
                //description
                ceform.setDescription(calendar.getFirstCalendarItem().getDescription());
                // private event
                ceform.setPrivateEvent(ampCalendar.isPrivateEvent());
                
                if(ampCalendar.getEventType()!=null){
                	//event type
                	ceform.setSelectedEventTypeName(ampCalendar.getEventType().getName());
                	// selected event type
                    ceform.setSelectedEventTypeId(ampCalendar.getEventType().getId());
                }
                

                Collection<AmpOrganisation> orgs = new ArrayList<AmpOrganisation> ();
                if (ampCalendar.getOrganisations() != null) {
                    Iterator orgItr = ampCalendar.getOrganisations().iterator();
                    while (orgItr.hasNext()) {
                        AmpOrganisation org = (AmpOrganisation) orgItr.next();
                        orgs.add(org);
                    }
                    ceform.setOrganizations(orgs);
                }

                // selected start date and selected end date
                GregorianCalendar startDate = new GregorianCalendar();
                startDate.setTime(calendar.getStartDate());

                GregorianCalendar endDate = new GregorianCalendar();
                endDate.setTime(calendar.getEndDate());

                DateBreakDown startDateBreakDown = null;
                DateBreakDown endDateBreakDown = null;
                
             
                	
                	Iterator iterRecevent = calendar.getRecurrCalEvent().iterator();
                	while(iterRecevent.hasNext()){
                		RecurrCalEvent rec = (RecurrCalEvent) iterRecevent.next();
                		//ceform.setRecurrEndDate(new Date(rec.getRecurrEndDate());
                		//ceform.setRecurrStartDate(rec.getRecurrStartDate());
                		if(rec.getRecurrStartDate() != null){
		                		 GregorianCalendar recstartDate = new GregorianCalendar();
		                		 recstartDate.setTime(rec.getRecurrStartDate());
		                		 
		                		 GregorianCalendar recurEndDate = new GregorianCalendar();
		                		 recurEndDate.setTime(rec.getRecurrEndDate());
                		}
                		ceform.setRecurrPeriod(rec.getRecurrPeriod());
                		ceform.setTypeofOccurrence(rec.getTypeofOccurrence());
                		ceform.setOccurrWeekDays(rec.getOccurrWeekDays());
                		
                		if(rec.getTypeofOccurrence().equals( "Yearly")){
                		ceform.setSelectedStartMonth("");
                		ceform.setSelectedStartYear(rec.getSelectedStartMonth());
                		}else if(rec.getTypeofOccurrence().equals( "Monthly")){
                			ceform.setSelectedStartYear("");
                			ceform.setSelectedStartMonth(rec.getSelectedStartMonth());
                		}else{
                			ceform.setSelectedStartYear("");
                			ceform.setSelectedStartMonth("");
                		}
                			
                		
                		rec.getId();
                		
                	}
                	
               
                

                try {
                    startDateBreakDown = new DateBreakDown(startDate, ceform.getSelectedCalendarTypeId().intValue());
                    endDateBreakDown = new DateBreakDown(endDate, ceform.getSelectedCalendarTypeId().intValue());
                    
                    ceform.setSelectedStartDate(startDateBreakDown.formatDateString());
                    ceform.setSelectedStartTime(startDateBreakDown.formatTimeString());

                    ceform.setSelectedEndDate(endDateBreakDown.formatDateString());
                    ceform.setSelectedEndTime(endDateBreakDown.formatTimeString());
                } catch (Exception ex) {
                }

                ceform.setPrivateEvent(ampCalendar.isPrivateEvent());
                ceform.setResetForm(false);
            }
        }
    }

    private Map<String, Team> loadRecepients() {
        Map<String, Team> teamMap = new HashMap<String, Team> ();
        List<AmpTeam> teams = (List<AmpTeam>) TeamUtil.getAllTeams();
        if (teams != null && teams.size() > 0) {
            for (AmpTeam ampTeam : teams) {
                if (!teamMap.containsKey("t" + ampTeam.getAmpTeamId())) {
                    Team team = new Team();
                    team.setId(ampTeam.getAmpTeamId());
                    team.setName(ampTeam.getName());
                    List<TeamMember> teamMembers = (List<TeamMember>) TeamMemberUtil.getAllTeamMembers(team.getId());
                    team.setMembers(teamMembers);
                    teamMap.put("t" + team.getId(), team);
                }
            }
        }
        return teamMap;
    }
    
    private ActionErrors validateDate(String eventStartDate,String eventEndDate) throws Exception{   
    	ActionErrors errors=new ActionErrors();
        String dtformat = FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);            
        if (dtformat == null) {
            dtformat = "dd/MM/yyyy";
        }
        dtformat+=" HH:mm";

        SimpleDateFormat sdf = new SimpleDateFormat(dtformat);
        Date stDate = null;
        Date endDate=null;
        
        try {
        	stDate=sdf.parse(eventStartDate);
		} catch (Exception e) {
			errors.add("incorrectDate", new ActionError("error.calendar.emptyEventStartDate"));
		}
		
        try {
        	endDate=sdf.parse(eventEndDate);
		
        } catch (Exception e) {
			errors.add("incorrectDate", new ActionError("error.calendar.emptyEventEndDate"));
		}
        
        if(stDate !=null && !stDate.equals(endDate) && !stDate.before(endDate)){
        	errors.add("incorrectDate", new ActionError("error.calendar.endDateLessThanStartDate"));
        }
    	return errors;
    }
    
    private ActionErrors validateEventInformation(String title,String[] selectedAttendees){
    	ActionErrors errors=new ActionErrors();
    	if(title==null || title.length()==0){
    		errors.add("emptyTitle",new ActionError("error.calendar.emptyEventTitle"));
    	}
    	if(selectedAttendees==null || selectedAttendees.length==0){
    		errors.add("noAttendees",new ActionError("error.calendar.noAttendees"));
    	}
    	return errors;
    }
    
}
