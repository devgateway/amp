package org.digijava.module.message.helper;


import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendarAttendee;
import org.digijava.module.calendar.util.AmpDbUtil;
import org.digijava.module.message.dbentity.AmpAlert;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.dbentity.Approval;
import org.digijava.module.message.dbentity.CalendarEvent;
import org.digijava.module.message.dbentity.TemplateAlert;
import org.digijava.module.message.triggers.ActivityActualStartDateTrigger;
import org.digijava.module.message.triggers.ActivityCurrentCompletionDateTrigger;
import org.digijava.module.message.triggers.ActivityDisbursementDateTrigger;
import org.digijava.module.message.triggers.ActivityFinalDateForContractingTrigger;
import org.digijava.module.message.triggers.ActivityFinalDateForDisbursementsTrigger;
import org.digijava.module.message.triggers.ActivityProposedApprovalDateTrigger;
import org.digijava.module.message.triggers.ActivityProposedCompletionDateTrigger;
import org.digijava.module.message.triggers.ActivityProposedStartDateTrigger;
import org.digijava.module.message.triggers.ActivitySaveTrigger;
import org.digijava.module.message.triggers.ApprovedActivityTrigger;
import org.digijava.module.message.triggers.CalendarEventSaveTrigger;
import org.digijava.module.message.triggers.CalendarEventTrigger;
import org.digijava.module.message.triggers.NotApprovedActivityTrigger;
import org.digijava.module.message.triggers.UserRegistrationTrigger;
import org.digijava.module.message.util.AmpMessageUtil;

public class AmpMessageWorker {

    public static void processEvent(Event e) throws Exception {
        String triggerClassName = e.getTrigger().getName();
        List<TemplateAlert> tempAlerts = AmpMessageUtil.getTemplateAlerts(triggerClassName);
        if (tempAlerts != null && !tempAlerts.isEmpty()) {
            for (TemplateAlert template : tempAlerts) {
                //AmpAlert newAlert=createAlertFromTemplate(template);
                AmpMessage newMsg = null;

                AmpAlert newAlert = new AmpAlert();
                Approval newApproval = new Approval();
                CalendarEvent newEvent = new CalendarEvent();

                if (e.getTrigger().equals(ActivitySaveTrigger.class)) { //<------ Someone created new Activity
                    newMsg = processActivitySaveEvent(e, newAlert, template);
                } else if (e.getTrigger().equals(UserRegistrationTrigger.class)) { //<----- Registered New User
                    newMsg = proccessUserRegistrationEvent(e, newAlert, template);
                } else if (e.getTrigger().equals(ActivityDisbursementDateTrigger.class)) {
                    newMsg = processActivityDisbursementDateComingEvent(e, newAlert, template);
                } else if (e.getTrigger().equals(CalendarEventTrigger.class)) {
                    newMsg = proccessCalendarEvent(e, newEvent, template,false);
                }else if(e.getTrigger().equals(CalendarEventSaveTrigger.class)){
                	 newMsg = proccessCalendarEvent(e, newEvent, template,true);
                }else if (e.getTrigger().equals(ApprovedActivityTrigger.class)) {
                    newMsg = processApprovedActivityEvent(e, newApproval, template);
                } else if (e.getTrigger().equals(NotApprovedActivityTrigger.class)) {
                    newMsg = processNotApprovedActivityEvent(e, newApproval, template);
                }else if (e.getTrigger().equals(ActivityActualStartDateTrigger.class)) {
                    newMsg = processActivityActualStartDateEvent(e, newAlert, template);
                }else if (e.getTrigger().equals(ActivityCurrentCompletionDateTrigger.class)) {
                    newMsg = processActivityCurrentCompletionDateEvent(e, newAlert, template);
                }else if (e.getTrigger().equals(ActivityFinalDateForContractingTrigger.class)) {
                    newMsg = processActivityFinalDateForContractingEvent(e, newAlert, template);
                }else if (e.getTrigger().equals(ActivityFinalDateForDisbursementsTrigger.class)) {
                    newMsg = processActivityFinalDateForDisbursementsEvent(e, newAlert, template);
                }else if (e.getTrigger().equals(ActivityProposedApprovalDateTrigger.class)) {
                    newMsg = processActivityProposedApprovalDateEvent(e, newAlert, template);
                }else if (e.getTrigger().equals(ActivityProposedCompletionDateTrigger.class)) {
                    newMsg = processActivityProposedCompletionDateEvent(e, newAlert, template);
                }else if (e.getTrigger().equals(ActivityProposedStartDateTrigger.class)) {
                    newMsg = processActivityProposedStartDateEvent(e, newAlert, template);
                }

                AmpMessageUtil.saveOrUpdateMessage(newMsg);
                /**
                 * getting states according to tempalteId
                 * New Requirement for ApprovedActiviti and Activity waiting approval.only teamLeader(in case approval is needed) and
                 *  activity creator/updater should get an alert regardless of receivers list in template
                 */
                if(e.getTrigger().equals(ApprovedActivityTrigger.class) || e.getTrigger().equals(NotApprovedActivityTrigger.class)) {
                    defineReceiversForApprovedAndNotApprovedActivities(e.getTrigger(), newMsg,(Long)e.getParameters().get(NotApprovedActivityTrigger.PARAM_ACTIVIY_CREATOR_TEAM));
                }else if(e.getTrigger().equals(CalendarEventTrigger.class)) {
                    defineReceiversForCalendarEvents(e, template, newMsg,false);
                }else if(e.getTrigger().equals(CalendarEventSaveTrigger.class)){
                	defineReceiversForCalendarEvents(e, template, newMsg,true);
                }
                else if(e.getTrigger().equals(ActivitySaveTrigger.class)) {
                    defineActivityCreationReceievrs(template, newMsg);
                }else if(e.getTrigger().equals(ActivityActualStartDateTrigger.class)) {
                    defineReceievrsByActivityTeam(template, newMsg,new Long(e.getParameters().get(ActivityActualStartDateTrigger.PARAM_TEAM_ID).toString()));
                }else if(e.getTrigger().equals(ActivityCurrentCompletionDateTrigger.class)) {
                    defineReceievrsByActivityTeam(template, newMsg,new Long(e.getParameters().get(ActivityCurrentCompletionDateTrigger.PARAM_TEAM_ID).toString()));
                }else if(e.getTrigger().equals(ActivityFinalDateForContractingTrigger.class)) {
                    defineReceievrsByActivityTeam(template, newMsg,new Long(e.getParameters().get(ActivityFinalDateForContractingTrigger.PARAM_TEAM_ID).toString()));
                }else if(e.getTrigger().equals(ActivityFinalDateForDisbursementsTrigger.class)) {
                    defineReceievrsByActivityTeam(template, newMsg,new Long(e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_TEAM_ID).toString()));
                }else if(e.getTrigger().equals(ActivityProposedApprovalDateTrigger.class)) {
                    defineReceievrsByActivityTeam(template, newMsg,new Long(e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_TEAM_ID).toString()));
                }else if(e.getTrigger().equals(ActivityProposedCompletionDateTrigger.class)) {
                    defineReceievrsByActivityTeam(template, newMsg,new Long(e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_TEAM_ID).toString()));
                }else if(e.getTrigger().equals(ActivityProposedStartDateTrigger.class)) {
                    defineReceievrsByActivityTeam(template, newMsg,new Long(e.getParameters().get(ActivityProposedStartDateTrigger.PARAM_TEAM_ID).toString()));
                }else{ //<-- currently for else is left user registration or activity disbursement date triggers
                    List<AmpMessageState> statesRelatedToTemplate = null;
                    statesRelatedToTemplate = AmpMessageUtil.loadMessageStates(template.getId());
                    HashMap<Long, AmpMessageState> msgStateMap = new HashMap<Long, AmpMessageState> ();
                    if (statesRelatedToTemplate != null && statesRelatedToTemplate.size() > 0) {
                        for (AmpMessageState state : statesRelatedToTemplate) {
                            createMsgState(state, newMsg);
                            if (!msgStateMap.containsKey(state.getMemberId())) {
                                msgStateMap.put(state.getMemberId(), state);
                            }
                        }
                        sendMailes(msgStateMap.values());
                    }
                }
            }
        }
    }

    /**
     *	Calendar Event processing
     */
    private static CalendarEvent proccessCalendarEvent(Event e, CalendarEvent event, TemplateAlert template,boolean saveActionWasCalled) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;        

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(UserRegistrationTrigger.PARAM_NAME));
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(CalendarEventTrigger.PARAM_URL) + "\">View Event</a>");
            event.setObjectURL(partialURL + e.getParameters().get(CalendarEventTrigger.PARAM_URL));
        }
        if(saveActionWasCalled){
        	event.setSenderType(MessageConstants.SENDER_TYPE_USER);
        	AmpTeamMember tm=(AmpTeamMember)e.getParameters().get(CalendarEventSaveTrigger.SENDER);
        	event.setSenderId(tm.getAmpTeamMemId());
        	event.setSenderName(tm.getUser().getFirstNames()+" "+tm.getUser().getLastName()+"<"+tm.getUser().getEmail()+">;"+tm.getAmpTeam().getName());
        	//put event's start/end dates in map.
        	myHashMap.put(MessageConstants.START_DATE, (String) e.getParameters().get(CalendarEventSaveTrigger.EVENT_START_DATE));
        	myHashMap.put(MessageConstants.END_DATE, (String) e.getParameters().get(CalendarEventSaveTrigger.EVENT_END_DATE));
        }else{
        	event.setSenderType(MessageConstants.SENDER_TYPE_USER_MANAGER);
        }
        
        CalendarEvent newEvent = createEventFromTemplate(template, myHashMap, event);

        String receivers = new String();

        HashMap<String, String> emailes=new HashMap<String, String>();

        Long calId = new Long(e.getParameters().get(CalendarEventTrigger.PARAM_ID).toString());
        AmpCalendar ampCal = AmpDbUtil.getAmpCalendar(calId);
        Set<AmpCalendarAttendee> att = ampCal.getAttendees();
        if (att != null) {
            for (AmpCalendarAttendee ampAtt : att) {
                if (ampAtt.getMember() != null) {
                    AmpTeamMember member = ampAtt.getMember();
                    User user = member.getUser();
                    receivers += ", " + user.getFirstNames() + " " + user.getLastName() + "<" + user.getEmail() + ">;" + member.getAmpTeam().getName() + ";";

                    if (!emailes.containsKey(user.getEmail())) {
                        emailes.put(user.getEmail(), ampCal.getCalendarPK().getCalendar().getFirstCalendarItem().getTitle());
                    }
                }
                if(ampAtt.getGuest()!=null){ //guests e-mails should also be included in receivers list
                	receivers+=", <"+ampAtt.getGuest().substring(2)+">;";
                }
            }            
        }
        //In case this event is created when new calendar event was added, message should go to it's creator too
        //so in the receivers list we should also add it's creator (AMP-3775)
        if(saveActionWasCalled){
        	AmpTeamMember member = ampCal.getMember();
            User user = member.getUser();
            receivers += ", " + user.getFirstNames() + " " + user.getLastName() + "<" + user.getEmail() + ">;" + member.getAmpTeam().getName() + ";";
        }        
        
        newEvent.setReceivers(receivers.substring(", ".length()));

        return newEvent;
    }

    /**
     *	Not Approved Activity Event processing
     */
    private static Approval processNotApprovedActivityEvent(Event e, Approval approval, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
        partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(NotApprovedActivityTrigger.PARAM_NAME));
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(NotApprovedActivityTrigger.PARAM_SAVED_BY)).getUser().getName());
        myHashMap.put(MessageConstants.OBJECT_TEAM,  ((Long)e.getParameters().get(NotApprovedActivityTrigger.PARAM_ACTIVIY_CREATOR_TEAM)).toString());
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(NotApprovedActivityTrigger.PARAM_URL) + "\">activity URL</a>");
            approval.setObjectURL(partialURL + e.getParameters().get(NotApprovedActivityTrigger.PARAM_URL));
        }
        approval.setSenderId( ( (AmpTeamMember) e.getParameters().get(NotApprovedActivityTrigger.PARAM_SAVED_BY)).getAmpTeamMemId());
        approval.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createApprovalFromTemplate(template, myHashMap, approval, true);
    }

    /**
     *	Approved Activity Event processing
     */
    private static Approval processApprovedActivityEvent(Event e, Approval approval, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent();
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
        partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ApprovedActivityTrigger.PARAM_NAME));
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ApprovedActivityTrigger.PARAM_SAVED_BY)).getUser().getName());
        myHashMap.put(MessageConstants.OBJECT_TEAM,  ((Long)e.getParameters().get(ApprovedActivityTrigger.PARAM_ACTIVIY_CREATOR_TEAM)).toString());
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ApprovedActivityTrigger.PARAM_URL) + "\">activity URL</a>");
            approval.setObjectURL(partialURL + e.getParameters().get(ApprovedActivityTrigger.PARAM_URL));
        }
        approval.setSenderId( ( (AmpTeamMember) e.getParameters().get(ApprovedActivityTrigger.PARAM_SAVED_BY)).getAmpTeamMemId());
        approval.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createApprovalFromTemplate(template, myHashMap, approval, false);
    }

    private static AmpAlert processActivitySaveEvent(Event e, AmpAlert alert, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent();
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
            partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
                     }*/
        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivitySaveTrigger.PARAM_NAME));
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_BY)).getUser().getName());
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ActivitySaveTrigger.PARAM_URL) + "\">activity URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(ActivitySaveTrigger.PARAM_URL));
        }
        alert.setSenderId( ( (AmpTeamMember) e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_BY)).getAmpTeamMemId());
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityActualStartDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent();
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
        partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityActualStartDateTrigger.PARAM_NAME));
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ActivityActualStartDateTrigger.PARAM_URL) + "\">activity URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(ActivityActualStartDateTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityCurrentCompletionDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
        partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityCurrentCompletionDateTrigger.PARAM_NAME));
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ActivityCurrentCompletionDateTrigger.PARAM_URL) + "\">activity URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(ActivityCurrentCompletionDateTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityFinalDateForContractingEvent(Event e, AmpAlert alert, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
            partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityFinalDateForContractingTrigger.PARAM_NAME));
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ActivityFinalDateForContractingTrigger.PARAM_URL) + "\">activity URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(ActivityFinalDateForContractingTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityFinalDateForDisbursementsEvent(Event e, AmpAlert alert, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
        partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_NAME));
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_URL) + "\">activity URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityProposedApprovalDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
        partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_NAME));
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_URL) + "\">activity URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityProposedCompletionDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
        partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_NAME));
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_URL) + "\">activity URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityProposedStartDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
        partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityProposedStartDateTrigger.PARAM_NAME));
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ActivitySaveTrigger.PARAM_URL) + "\">activity URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(ActivityProposedStartDateTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    /**
     *	User Registration Event processing
     */
    private static AmpAlert proccessUserRegistrationEvent(Event e, AmpAlert alert, TemplateAlert template) {
        //url
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
        partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(UserRegistrationTrigger.PARAM_NAME));
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(UserRegistrationTrigger.PARAM_URL) + "\">User Profile URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(UserRegistrationTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_USER_MANAGER);

        return createAlertFromTemplate(template, myHashMap, alert);
    }

    /**
     * Activity's disbursement date Event processing
     */
    private static AmpAlert processActivityDisbursementDateComingEvent(Event e, AmpAlert alert, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
        partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_NAME));
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_URL) + "\">activity URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);
    }

    private static Approval createApprovalFromTemplate(TemplateAlert template, HashMap<String, String> myMap, Approval newApproval, boolean needsApproval) {
        newApproval.setName(DgUtil.fillPattern(template.getName(), myMap));
        newApproval.setDescription(DgUtil.fillPattern(template.getDescription(), myMap));
        newApproval.setDraft(false);
        newApproval.setCreationDate(new Date());
        //receivers
        String receivers;
        AmpTeamMember tm = TeamMemberUtil.getAmpTeamMember(newApproval.getSenderId());
        receivers = tm.getUser().getFirstNames() + " " + tm.getUser().getLastName() + "<" + tm.getUser().getEmail() + ">;" + tm.getAmpTeam().getName() + ";";
        if (needsApproval) {
            //team lead can't be null,because if team has no leader,then no trigger will be invoked
            String teamId=myMap.get(MessageConstants.OBJECT_TEAM);
            AmpTeamMember teamHead=TeamMemberUtil.getTeamHead(Long.parseLong(teamId));
            receivers += ", " + teamHead.getUser().getFirstNames() + " " + teamHead.getUser().getLastName() + "<" + teamHead.getUser().getEmail() + ">;" + teamHead.getAmpTeam().getName() + ";";
        }
        newApproval.setReceivers(receivers);
        return newApproval;
    }

    /**
     * created different kinds of alerts(not approvals or calendar events )
     */
    private static AmpAlert createAlertFromTemplate(TemplateAlert template, HashMap<String, String> myMap, AmpAlert newAlert) {
        newAlert.setName(DgUtil.fillPattern(template.getName(), myMap));
        newAlert.setDescription(DgUtil.fillPattern(template.getDescription(), myMap));
        newAlert.setReceivers(template.getReceivers());
        newAlert.setDraft(false);
        Calendar cal = Calendar.getInstance();
        newAlert.setCreationDate(cal.getTime());
        return newAlert;
    }

    private static CalendarEvent createEventFromTemplate(TemplateAlert template, HashMap<String, String> myMap, CalendarEvent newEvent) {
        newEvent.setName(DgUtil.fillPattern(template.getName(), myMap));
        newEvent.setDescription(DgUtil.fillPattern(template.getDescription(), myMap));
        newEvent.setReceivers(template.getReceivers());
        newEvent.setDraft(false);
        Calendar cal = Calendar.getInstance();
        newEvent.setCreationDate(cal.getTime());
        return newEvent;
    }

    /**
     * this method defines approval receivers and creates corresponding AmpMessageStates.
     */
    private static void defineReceiversForApprovedAndNotApprovedActivities(Class triggerClass, AmpMessage approval,Long teamId) throws Exception {
        AmpMessageState state = new AmpMessageState();
        state.setMemberId(approval.getSenderId());
        createMsgState(state, approval);
        if (triggerClass.equals(NotApprovedActivityTrigger.class)) {
            AmpTeamMember teamHead=TeamMemberUtil.getTeamHead(teamId);
            if (teamHead != null) {
                Long teamHeadId = teamHead.getAmpTeamMemId();
                state = new AmpMessageState();
                state.setMemberId(teamHeadId);
                createMsgState(state, approval);
            }
        }
    }

    /**
     * this method defines calendar event receivers and creates corresponding AmpMessageStates.
     */
    private static void defineReceiversForCalendarEvents(Event e, TemplateAlert template, AmpMessage calEvent,boolean  saveActionWasCalled) throws Exception {
        HashMap<Long, AmpMessageState> temMsgStateMap = new HashMap<Long, AmpMessageState> ();

        List<AmpMessageState> lstMsgStates = AmpMessageUtil.loadMessageStates(template.getId());
        if (lstMsgStates != null) {
            for (AmpMessageState state : lstMsgStates) {
                if (!temMsgStateMap.containsKey(state.getMemberId())) {
                    temMsgStateMap.put(state.getMemberId(), state);
                }
            }
        }

        HashMap<Long, AmpMessageState> eventMsgStateMap = new HashMap<Long, AmpMessageState> ();
        Long calId = new Long(e.getParameters().get(CalendarEventTrigger.PARAM_ID).toString());
        AmpCalendar ampCal = AmpDbUtil.getAmpCalendar(calId);
        Set<AmpCalendarAttendee> att = ampCal.getAttendees();
        if (att != null) {
            for (AmpCalendarAttendee ampAtt : att) {
                if (ampAtt.getMember() != null) {
                    AmpTeamMember member = ampAtt.getMember();
                    if (!eventMsgStateMap.containsKey(member.getAmpTeamMemId())) {
                        AmpMessageState state = new AmpMessageState();
                        state.setMemberId(member.getAmpTeamMemId());
                        state.setSenderId(calEvent.getSenderId());
                        eventMsgStateMap.put(state.getMemberId(), state);
                    }
                }

            }
        }

        HashMap<Long, AmpMessageState> msgStateMap = new HashMap<Long, AmpMessageState> ();
        
        //calendar event creator should also get a message (AMP-3775)
        if(saveActionWasCalled){
        	AmpTeamMember calEventcreator=ampCal.getMember();
            AmpMessageState msgState = new AmpMessageState();
            msgState.setMemberId(calEventcreator.getAmpTeamMemId());
            msgState.setSenderId(calEvent.getSenderId());
            msgStateMap.put(msgState.getMemberId(), msgState);
        }
        
        
        for (AmpMessageState state : temMsgStateMap.values()) {
            if (eventMsgStateMap.containsKey(state.getMemberId())) {
                msgStateMap.put(state.getMemberId(), state);
            }
        }
       
        
        for (AmpMessageState state : msgStateMap.values()) {
            createMsgState(state, calEvent);
        }
    }

    /**
     * this method defines alert receivers and creates corresponding AmpMessageStates.
     */

    private static void defineReceievrsByActivityTeam(TemplateAlert template, AmpMessage alert, Long teamId) throws Exception {
        List<AmpMessageState> statesRelatedToTemplate = null;
        //get the member who created an activity. it's the current member    

        statesRelatedToTemplate = AmpMessageUtil.loadMessageStates(template.getId());
        if (statesRelatedToTemplate != null && statesRelatedToTemplate.size() > 0) {
            //create receivers list for activity
            String receivers;
            Collection<TeamMember> teamMembers =TeamMemberUtil.getAllTeamMembers(teamId);
            receivers = fillTOfieldForReceivers(teamMembers, statesRelatedToTemplate);
            alert.setReceivers(receivers);

            for (AmpMessageState state : statesRelatedToTemplate) {
                //get receiver Team Member.
                AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMember(state.getMemberId());
                /**
                 * Alert about new activity creation should get only members of the same team in which activity was created,if this team is listed as receivers in template.
                 */
                if (teamMember.getAmpTeam().getAmpTeamId().equals(teamId)) {
                    createMsgState(state, alert);
            }
    }
        }
    }

    private static void defineActivityCreationReceievrs(TemplateAlert template, AmpMessage alert) throws Exception {
        List<AmpMessageState> statesRelatedToTemplate = null;
        //get the member who created an activity. it's the current member
        AmpTeamMember activityCreator = TeamMemberUtil.getAmpTeamMember(alert.getSenderId());

        statesRelatedToTemplate = AmpMessageUtil.loadMessageStates(template.getId());
        if (statesRelatedToTemplate != null && statesRelatedToTemplate.size() > 0) {
            //create receivers list for activity
            String receivers;
            Collection<TeamMember> teamMembers = TeamMemberUtil.getAllTeamMembers(activityCreator.getAmpTeam().getAmpTeamId());
            receivers = fillTOfieldForReceivers(teamMembers, statesRelatedToTemplate);
            alert.setReceivers(receivers);

            for (AmpMessageState state : statesRelatedToTemplate) {
                //get receiver Team Member.
                AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMember(state.getMemberId());
                /**
                 * Alert about new activity creation should get only members of the same team in which activity was created,if this team is listed as receivers in template.
                 */
                if (teamMember.getAmpTeam().getAmpTeamId().equals(activityCreator.getAmpTeam().getAmpTeamId())) {
                    createMsgState(state, alert);
                }
            }
        }
    }

    private static void createMsgState(AmpMessageState state, AmpMessage newMsg) throws Exception {
        AmpMessageState newState = new AmpMessageState();
        newState.setMessage(newMsg);
        newState.setMemberId(state.getMemberId());
        newState.setRead(false);
        //will this message be visible in user's mailbox
        if (AmpMessageUtil.isInboxFull(newMsg.getClass(), state.getMemberId())) {
            newState.setMessageHidden(true);
        } else {
            newState.setMessageHidden(false);
        }
        try {
            AmpMessageUtil.saveOrUpdateMessageState(newState);
            sendMail(newState);
        } catch (AimException e) {
            e.printStackTrace();
        }
    }

    /*
     * This function is used to create receivers String, which will be shown on view message page in TO: section
     */
    private static String fillTOfieldForReceivers(Collection<TeamMember> teamMembers, List<AmpMessageState> states) {
        String receivers = "";
        for (AmpMessageState state : states) {
            for (TeamMember tm : teamMembers) {
                if (state.getMemberId().equals(tm.getMemberId())) {
                    receivers += tm.getMemberName() + " " + "<" + tm.getEmail() + ">;" + tm.getTeamName() + ";" + ", ";
                }
            }
        }
        return receivers;
    }

    private static void sendMailes(Collection<AmpMessageState> statesRelatedToTemplate) throws Exception {
        for(AmpMessageState state:statesRelatedToTemplate){
            sendMail(state);
        }
    }

    private static void sendMailes(HashMap<String, String> emails) throws Exception {
        if(emails!=null){
            for (String email : emails.keySet()) {
            	AmpMessageSettings messageSettings;
				messageSettings = AmpMessageUtil.getMessageSettings();
				Long sendMail = messageSettings.getEmailMsgs();
                if(sendMail.intValue() == 1){
                	sendMail("system@digijava.org",email,"New alert","UTF8",emails.get(email));
                }
            }
        }
    }

    private static void sendMail(AmpMessageState state) throws Exception {
        AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMember(state.getMemberId());
        if (teamMember != null) {
        	AmpMessageSettings messageSettings = AmpMessageUtil.getMessageSettings();
            Long sendMail = messageSettings.getEmailMsgs();
            if(sendMail.intValue() == 1){
            	User user = teamMember.getUser();
                sendMail("system@digijava.org",user.getEmail(),"New alert","UTF8",state.getMessage().getDescription());
            }
        }
    }

    private static void sendMail(String from, String to, String subject, String charset, String text) {
        try {
            InternetAddress[] ito = new InternetAddress[] {new InternetAddress(to)};

            Address[] cc = null;
            Address[] bcc = null;
            boolean asHtml = true;
            boolean log = true;
            boolean rtl = false;

            DgEmailManager.sendMail(ito, from, cc, bcc, subject, text, charset, asHtml, log, rtl);
        } catch (Exception ex) {

        }
    }
}
