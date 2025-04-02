package org.digijava.module.calendar.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.Team;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendarAttendee;
import org.digijava.module.calendar.dbentity.AmpCalendarPK;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.dbentity.RecurrCalEvent;
import org.digijava.module.calendar.entity.CalendarOptions;
import org.digijava.module.calendar.entity.DateBreakDown;
import org.digijava.module.calendar.entity.DateNavigator;
import org.digijava.module.calendar.exception.CalendarException;
import org.digijava.module.calendar.form.CalendarEventForm;
import org.digijava.module.calendar.util.AmpDbUtil;
import org.digijava.module.calendar.util.AmpUtil;
import org.digijava.module.calendar.util.CalendarConversor;
import org.digijava.module.calendar.util.CalendarThread;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.common.dbentity.ItemStatus;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.helper.MessageConstants;
import org.digijava.module.message.triggers.ApprovedCalendarEventTrigger;
import org.digijava.module.message.triggers.AwaitingApprovalCalendarTrigger;
import org.digijava.module.message.triggers.CalendarEventSaveTrigger;
import org.digijava.module.message.triggers.NotApprovedCalendarEventTrigger;
import org.digijava.module.message.triggers.RemoveCalendarEventTrigger;
import org.digijava.module.message.util.AmpMessageUtil;


public class ShowCalendarEvent extends Action {
    
    private static Logger logger = Logger.getLogger(ShowCalendarEvent.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        Site site = RequestUtils.getSite(request);
        CalendarThread.setSite(site);
        Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
        CalendarThread.setLocale(navigationLanguage);
        
//        if (!RequestUtils.isLoggued(response, request.getSession(), request)) {
//          return null;
//      }
        
        CalendarEventForm ceform = (CalendarEventForm) form;

        
        Object teamObj = request.getSession().getAttribute("teamHead");
        boolean isManager = (teamObj != null && ((String)teamObj).equalsIgnoreCase("yes"));
        
        String print = request.getParameter("method");
        String ampCalendarId = request.getParameter("calendarId");
        if(print!=null && print.equals("print")){
            ceform.setAmpCalendarId(new Long(ampCalendarId));
            ceform.setMethod(print);
        }
        if (ceform.getMethod().equalsIgnoreCase("new")) {
            ceform.reset(mapping, request);
            ceform.setOrganizations(null);
            ceform.setSelOrganizations(null);
            ceform.setActionButtonsVisible(true);
        }
        else if(ceform.getMethod().equalsIgnoreCase("removeOrg")) 
        {
            Long[] listSelectedOrganizations = ceform.getSelOrganizations();
            if (listSelectedOrganizations != null && listSelectedOrganizations.length > 0) {
                Collection<AmpOrganisation> colOrganizations = ceform.getOrganizations();
                Collection<AmpOrganisation> newColOrganizations = new TreeSet<AmpOrganisation>();
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
        if(ceform.getSelectedEventTypeId() != null && ceform.getSelectedEventTypeId() > 0){
            String eventTypeName=CategoryManagerUtil.getAmpCategoryValueFromDb(ceform.getSelectedEventTypeId()).getValue();
            ceform.setSelectedEventTypeName(eventTypeName);
        }
        
        // selected calendar type
        Long selectedCalendarTypeId = ceform.getCalendarTypeId();
        if (selectedCalendarTypeId == null ||
                (!selectedCalendarTypeId.equals(new Long(CalendarOptions.CALENDAR_TYPE_GREGORIAN)) &&
                 !selectedCalendarTypeId.equals(new Long(CalendarOptions.CALENDAR_TYPE_ETHIOPIAN)) &&
                 !selectedCalendarTypeId.equals(new Long(CalendarOptions.CALENDAR_TYPE_ETHIOPIAN_FY)))) {
                selectedCalendarTypeId = Long.valueOf(CalendarOptions.defaultCalendarType);
                ceform.setSelectedCalendarTypeId(selectedCalendarTypeId);
      }else{
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
                    selectedAttsCol.add(new LabelValueBean(slAtts[i].substring(slAtts[i].lastIndexOf("g:")+2), slAtts[i].substring(slAtts[i].lastIndexOf("g:"))));
                }
                    else if (slAtts[i].startsWith("guest")) {
                         String guest="---"+TranslatorWorker.translateText("Guest")+"---";
                         selectedAttsCol.add(new LabelValueBean(guest, "guest"));
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
            ceform.setOrganizations(new TreeSet<AmpOrganisation>());
        
        
        if (ceform.getMethod().equalsIgnoreCase("new")) {
            ceform.setAmpCalendarId(null);
            ceform.setActionButtonsVisible(true);
        } else if (ceform.getMethod().equalsIgnoreCase("edit")) {
            loadAmpCalendar(ceform, request);
        } else if(ceform.getMethod().equalsIgnoreCase("ok")){
            ceform.setMethod("");
            return mapping.findForward("forward");
        } else if (ceform.getMethod().equalsIgnoreCase("save")) {
            String stDate=ceform.getSelectedStartDate() + " " + ceform.getSelectedStartTime();
            String endDate=ceform.getSelectedEndDate()+ " " + ceform.getSelectedEndTime();
            ActionMessages errors=validateDate(stDate,endDate,ceform);
            errors.add(validateEventInformation(ceform.getEventTitle(),ceform.getSelectedAtts()));
            if(!errors.isEmpty()){
                saveErrors(request, errors);
                ceform.setEventTypesList(CategoryManagerUtil.getAmpEventColors());
                return mapping.findForward("success");              
            }else{
                AmpCalendar ampCalendar=saveAmpCalendar(ceform, request);
                ceform.setMethod("");
                request.setAttribute("calendarEventCreated", !ceform.isPrivateEvent());
                CalendarItem calendarItem =  ampCalendar.getCalendarPK().getCalendar().getFirstCalendarItem();
                AmpTeamMember tl = ampCalendar.getMember().getAmpTeam().getTeamLead();
                new AwaitingApprovalCalendarTrigger(calendarItem, tl.getUser().getName(),tl);
                return mapping.findForward("forward");
            }            

        } else if (ceform.getMethod().equalsIgnoreCase("delete")) {
            AmpCalendar ampCalendar=AmpDbUtil.getAmpCalendar(ceform.getAmpCalendarId());
            if(ampCalendar!=null){
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
            }
            
            ceform.setMethod("");
            return mapping.findForward("forward");
           
        } else if (ceform.getMethod().equalsIgnoreCase("valid")) {
            if (isManager){
                AmpCalendar ampCalendar=AmpDbUtil.getAmpCalendar(ceform.getAmpCalendarId());
                CalendarItem calendarItem =  ampCalendar.getCalendarPK().getCalendar().getFirstCalendarItem();
                if (ceform.getApprove()==MessageConstants.CALENDAR_EVENT_APPROVED){
                    AuditLoggerUtil.logObject(request.getSession(), request, calendarItem, "Approved");
                    calendarItem.setApprove(new Integer(MessageConstants.CALENDAR_EVENT_APPROVED));
                    AmpDbUtil.updateAmpCalendar(ampCalendar);
                    new ApprovedCalendarEventTrigger(calendarItem,
                            ((TeamMember) request.getSession().getAttribute("currentMember")).getMemberName(),
                            ampCalendar.getMember());
                } else if (ceform.getApprove()==MessageConstants.CALENDAR_EVENT_NOT_APPROVED){
                    AuditLoggerUtil.logObject(request.getSession(), request, calendarItem, "Not Approved");
                    AmpDbUtil.deleteAmpCalendar(ceform.getAmpCalendarId());
                    new NotApprovedCalendarEventTrigger(calendarItem,
                            ((TeamMember) request.getSession().getAttribute("currentMember")).getMemberName(),
                            ampCalendar.getMember());
                }
                ceform.setMethod("");
                return mapping.findForward("forward");                  
             }
         
        } else if (ceform.getMethod().equalsIgnoreCase("preview") || ceform.getMethod().equalsIgnoreCase("print")) {
            String stDate=ceform.getSelectedStartDate() + " " + ceform.getSelectedStartTime();
            String endDate=ceform.getSelectedEndDate()+ " " + ceform.getSelectedEndTime();
            ActionMessages errors=new ActionMessages();
            if(ceform.getAmpCalendarId()==null || !ceform.isResetForm()){
                errors=validateDate(stDate,endDate,ceform);
                errors.add(validateEventInformation(ceform.getEventTitle(),ceform.getSelectedAtts()));
            }           
            if(!errors.isEmpty()){
                saveErrors(request, errors);
                ceform.setEventTypesList(CategoryManagerUtil.getAmpEventColors());
                return mapping.findForward("success");              
            }else{
                  
                  
                loadAmpCalendar(ceform, request);
                if(ceform.getAmpCalendarId()!=null && ceform.getAmpCalendarId() > 0){ //<--this means that user is not creating new event, but previewing old one
                    ceform.setActionButtonsVisible(false);
                    //get current member
                    HttpSession ses = request.getSession();
                    TeamMember mem = (TeamMember) ses.getAttribute("currentMember");
                    if (mem!=null) {
                        ceform.setEventCreator(mem.getMemberName());
                        String[] selattendeess=ceform.getSelectedAtts();
                        if(ceform.getEventCreatorId()!=null && ceform.getEventCreatorId().equals(mem.getMemberId())){
                            ceform.setActionButtonsVisible(true);
                        }else if(ceform.isPrivateEvent()){
                            if(selattendeess!=null){
                            for (String attendee : selattendeess) {
                                if(attendee.startsWith("m:") && attendee.substring(attendee.indexOf(":")+1).equals(mem.getMemberId().toString())){
                                    ceform.setActionButtonsVisible(true);
                                    break;
                                }
                            }
                            }
                        }
                    }
                }               
                if(!ceform.getMethod().equalsIgnoreCase("print")){
                    //
                    if (ceform.getMethod().equalsIgnoreCase("preview")) {
                        // do nothing see AMPOPS-154
                    } else {
                        // i keep this, dunno if it is used somewhere, but should be removed
                        ceform.setMethod(""); 
                    }
                    return mapping.findForward("preview");
                }else{
                    
                     return mapping.findForward("print");
                    
                }
            }
        }
        
        
//        List<AmpEventType> eventTypeList = new ArrayList<AmpEventType>(); 
//        
//        AmpCategoryClass categoryClass = CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.EVENT_TYPE_KEY);   
//        Iterator<AmpCategoryValue> categoryClassIter = categoryClass.getPossibleValues().iterator();
//         while(categoryClassIter.hasNext()){
//          AmpEventType eventType = new AmpEventType();
//          AmpCategoryValue item = (AmpCategoryValue) categoryClassIter.next();
//           eventType.setName(item.getValue());
//           eventType.setId(item.getId());
//             Iterator<AmpCategoryValue> usedValues = item.getUsedValues().iterator();
//              while (usedValues.hasNext()){
//               AmpCategoryValue categoryValueItem = (AmpCategoryValue) usedValues.next();
//               eventType.setColor(categoryValueItem.getValue());
//           }
//            eventTypeList.add(eventType);
//        }
//        
        ceform.setEventTypesList(CategoryManagerUtil.getAmpEventColors());
        return mapping.findForward("success");
    }

   
    private AmpCalendar saveAmpCalendar(CalendarEventForm ceform, HttpServletRequest request) throws Exception{
        Object teamObj = request.getSession().getAttribute("teamHead");
        boolean isManager = (teamObj != null && ((String)teamObj).equalsIgnoreCase("yes"));
        try {
            Integer approved = ceform.getApprove();
            if (ceform.getAmpCalendarId() != null && ceform.getAmpCalendarId() > 0) {
                AmpDbUtil.deleteAmpCalendar(ceform.getAmpCalendarId());
            }

            AmpCalendar ampCalendar = new AmpCalendar();

            AmpCategoryValue value =    CategoryManagerUtil.getAmpCategoryValueFromDb(ceform.getSelectedEventTypeId());
            ampCalendar.setEventsType(value);
         
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
                        if(slAtts[i].lastIndexOf("g:")!=-1){
                            slAtts[i]=slAtts[i].substring(slAtts[i].lastIndexOf("g:")+2);
                        }
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
            Long templId = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.VISIBILITY_TEMPLATE);
            AmpFeaturesVisibility eventApprove = FeaturesUtil.getFeatureByName("Event Approve", "Calendar", templId);
            if (eventApprove == null || isManager){
                calendarItem.setApprove(new Integer(MessageConstants.CALENDAR_EVENT_APPROVED)); // 1 - Approve by default;
            } else {
                if (approved != null && approved == MessageConstants.CALENDAR_EVENT_APPROVED) {
                    calendarItem.setApprove(new Integer(MessageConstants.CALENDAR_EVENT_AWAITING_REAPPROVE)); // 2 - Awaiting Re-Approval;
                } else {
                    calendarItem.setApprove(new Integer(MessageConstants.CALENDAR_EVENT_AWAITING)); // 0 - Awaiting Approval;
                }
            }

            // fill calendar object

            calendarItems.add(calendarItem);
            calendar.setCalendarItem(calendarItems);

            if(ceform.getRecurrPeriod()!=null && ceform.getRecurrPeriod().longValue() != 0){           
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
                dtformat+=" HH:mm";
                 SimpleDateFormat sdf = new SimpleDateFormat(dtformat);
                 Date recurrStartDate=null;
                 Date recurrEndDate= null;
                 String endDate = null;
                 String startDate = null;
                 if(ceform.getRecurrStartDate()!=null && !ceform.getRecurrStartDate().equals("")){
                     
                     startDate = ceform.getRecurrStartDate() + " " + ceform.getRecurrSelectedStartTime();
                     recurrStartDate=sdf.parse(startDate);
                 }
                 if(ceform.getRecurrEndDate()!=null && !ceform.getRecurrEndDate().equals("")){
                     
                    endDate = ceform.getRecurrEndDate() + " " + ceform.getRecurrSelectedEndTime();
                    recurrEndDate= sdf.parse(endDate);
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

            if (ceform.getSelectedCalendarTypeId().equals(new Long(CalendarOptions.CALENDAR_TYPE_ETHIOPIAN)) ||
                    ceform.getSelectedCalendarTypeId().equals(new Long(CalendarOptions.CALENDAR_TYPE_ETHIOPIAN_FY))){
                CalendarConversor convert  = new CalendarConversor(ceform.getStartDate().YEAR);

                //Convert to Gregorian to save all events in the same format
                calendar.setStartDate(sdf.parse(AmpUtil.SimpleEthipianToGregorian(startDateTime, convert)));
                calendar.setEndDate(sdf.parse(AmpUtil.SimpleEthipianToGregorian(endDateTime, convert)));
            }else{
            calendar.setStartDate(sdf.parse(startDateTime));
            calendar.setEndDate(sdf.parse(endDateTime));
            }

            calPK.setCalendar(calendar);
            ampCalendar.setCalendarPK(calPK);
            //Private Event checkbox label states "Public Event"            
            ampCalendar.setPrivateEvent(ceform.isPrivateEvent());

            AmpDbUtil.updateAmpCalendar(ampCalendar);
            //Create new calendar event alert           
            CalendarEventSaveTrigger cet=new CalendarEventSaveTrigger(ampCalendar);
            ceform.setResetForm(true);
            return ampCalendar;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    private void loadAmpCalendar(CalendarEventForm ceform, HttpServletRequest request) throws CalendarException, WorkerException, ParseException {
        if (ceform.getAmpCalendarId() != null &&
            ceform.getAmpCalendarId() > 0 &&
            ceform.isResetForm()) {

            Long ampCalendarId = ceform.getAmpCalendarId();
            ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);
            String instanceId = moduleInstance.getInstanceName();
            Long siteId = moduleInstance.getSite().getId();
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
                        //  teams.add(team);                            
                        //} else {
                        //  guests.add(guest);
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
               /* if(!guests.isEmpty()){
                    String guest="---"+TranslatorWorker.translateText("Guest", langCode, site)+"---";
                    selectedAttsCol.add(new LabelValueBean(guest, "guest"));
                    selAtts.add("guest");
                }*/
                for(String guest: guests){
                    if(guest.indexOf("g:")!=-1){
                        guest=guest.substring(guest.lastIndexOf("g:")+2);
                    }                   
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
                ceform.setApprove(calendar.getFirstCalendarItem().getApprove() == null ? MessageConstants.CALENDAR_EVENT_APPROVED : calendar.getFirstCalendarItem().getApprove().intValue());
                // private event
                ceform.setPrivateEvent(ampCalendar.isPrivateEvent());
                
                if(ampCalendar.getEventsType()!=null){
                    AmpCategoryValue ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromDb(ampCalendar.getEventsType().getId());
                    if (ampCategoryValue != null){
                        //event type
                        ceform.setSelectedEventTypeName(ampCategoryValue.getValue());
                        // selected event type
                        ceform.setSelectedEventTypeId(ampCategoryValue.getId());
                    }
                }

                Collection<AmpOrganisation> orgs = new TreeSet<AmpOrganisation> ();
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
                DateBreakDown endRecurrDateBreakDown = null;
                
             
                    
                    Iterator iterRecevent = calendar.getRecurrCalEvent().iterator();
                    while(iterRecevent.hasNext()){
                        RecurrCalEvent rec = (RecurrCalEvent) iterRecevent.next();
                        if(rec.getRecurrEndDate() != null){
                            GregorianCalendar endRecurrDate = new GregorianCalendar();
                            endRecurrDate.setTime(rec.getRecurrEndDate());
                            endRecurrDateBreakDown = new DateBreakDown(endRecurrDate, ceform.getSelectedCalendarTypeId().intValue(),request);
                            ceform.setRecurrEndDate(endRecurrDateBreakDown.formatDateString());
                            ceform.setRecurrSelectedEndTime(endRecurrDateBreakDown.formatTimeString());
                        }
                        ceform.setRecurrPeriod(rec.getRecurrPeriod());
                        ceform.setTypeofOccurrence(rec.getTypeofOccurrence());
                        ceform.setOccurrWeekDays(rec.getOccurrWeekDays());
                        
                        if(rec.getTypeofOccurrence().equals( "year")){
                        ceform.setSelectedStartMonth("");
                        ceform.setSelectedStartYear(rec.getSelectedStartMonth());
                        }else if(rec.getTypeofOccurrence().equals( "month")){
                            ceform.setSelectedStartYear("");
                            ceform.setSelectedStartMonth(rec.getSelectedStartMonth());
                        }else{
                            ceform.setSelectedStartYear("");
                            ceform.setSelectedStartMonth("");
                        }
                        rec.getId();
                     
                    } 

                try {
                    startDateBreakDown = new DateBreakDown(startDate, ceform.getSelectedCalendarTypeId().intValue(),request);
                    endDateBreakDown = new DateBreakDown(endDate, ceform.getSelectedCalendarTypeId().intValue(),request);
                    
                    ceform.setSelectedStartDate(startDateBreakDown.formatDateString());
                    ceform.setSelectedStartTime(startDateBreakDown.formatTimeString());

                    ceform.setSelectedEndDate(endDateBreakDown.formatDateString());
                    ceform.setSelectedEndTime(endDateBreakDown.formatTimeString());
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                ceform.setPrivateEvent(ampCalendar.isPrivateEvent());
                ceform.setResetForm(false);
            }
        }
    }

    private Map<String, Team> loadRecepients() {
        Map<String, Team> teamMap = new HashMap<String, Team> ();
             
        List<AmpTeam> teams = (List<AmpTeam>) TeamUtil.getAllTeams();
        Map<Long, List<TeamMember>> allTeamsWithMembers = TeamMemberUtil.getAllTeamsWithMembers();
        
        if (teams != null && teams.size() > 0) {
            for (AmpTeam ampTeam : teams) {
                if (!teamMap.containsKey("t" + ampTeam.getAmpTeamId())) {
                    Team team = new Team();
                    team.setId(ampTeam.getAmpTeamId());
                    team.setName(ampTeam.getName());
                    team.setMembers(allTeamsWithMembers.get(team.getId()));
                    
                    teamMap.put("t" + team.getId(), team);
                }
            }
        }
    
        return teamMap;
    }
    
    private ActionErrors validateDate(String eventStartDate,String eventEndDate, CalendarEventForm form) throws Exception{   
        ActionErrors errors=new ActionErrors();
        String dtformat = FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);            
        if (dtformat == null) {
            dtformat = "dd/MM/yyyy";
        }
        dtformat+=" HH:mm";

        SimpleDateFormat sdf = new SimpleDateFormat(dtformat);
        Date stDate = null;
        Date endDate=null;
        Date endRecurrDate=null;
        
        try {
            stDate=sdf.parse(eventStartDate);
        } catch (Exception e) {
            errors.add("incorrectDate", new ActionMessage("error.calendar.emptyEventStartDate"));
        }
        
        try {
            endDate=sdf.parse(eventEndDate);
        } catch (Exception e) {
            errors.add("incorrectDate", new ActionMessage("error.calendar.emptyEventEndDate"));
        }
        
        try {
            endRecurrDate=sdf.parse(form.getRecurrEndDate() + " " + form.getRecurrSelectedEndTime());
        } catch (Exception e) {
            //errors.add("incorrectDate", new ActionMessage("error.calendar.emptyEventEndDate"));
        }
        
        if(stDate !=null && !stDate.equals(endDate) && !stDate.before(endDate)){
            errors.add("incorrectDate", new ActionMessage("error.calendar.endDateLessThanStartDate"));
        }
        
        if(endRecurrDate !=null && endRecurrDate.before(endDate)){
            errors.add("incorrectDate", new ActionMessage("error.calendar.endRecurrDateLessThanEndDate"));
        }
        
        
        Long diffDate = endDate.getTime() - stDate.getTime();
        Long diffDays = (diffDate / (24 * 60 * 60 * 1000));
        Long recurrDays = 0L;
        
        if (form.getTypeofOccurrence()!=null && form.getTypeofOccurrence()!="" && form.getRecurrPeriod()!=null && form.getRecurrPeriod()>0){
            if (form.getTypeofOccurrence().equalsIgnoreCase("day")) {
                recurrDays = form.getRecurrPeriod();
            }
            if (form.getTypeofOccurrence().equalsIgnoreCase("week")) {
                recurrDays = form.getRecurrPeriod() * 7;
            }
            if (form.getTypeofOccurrence().equalsIgnoreCase("month")) {
                recurrDays = form.getRecurrPeriod() * 30;
            }
            if (form.getTypeofOccurrence().equalsIgnoreCase("year")) {
                recurrDays = form.getRecurrPeriod() * 365;
            }
        }
        if ((recurrDays > 0) && (diffDays >= recurrDays)) {
            errors.add("incorrectDate", new ActionMessage("error.calendar.recurrPeriodLessThanEventDuration"));
        }
        
        return errors;
    }
    
    private ActionErrors validateEventInformation(String title,String[] selectedAttendees){
        ActionErrors errors=new ActionErrors();
        if(title==null || title.length()==0){
            errors.add("emptyTitle",new ActionMessage("error.calendar.emptyEventTitle"));
        }
        if(selectedAttendees==null || selectedAttendees.length==0){
            errors.add("noAttendees",new ActionMessage("error.calendar.noAttendees"));
        }
        return errors;
    }
    
}
