package org.digijava.module.message.helper;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.digijava.kernel.util.DgUtil;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.message.dbentity.AmpAlert;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.dbentity.TemplateAlert;
import org.digijava.module.message.util.AmpMessageUtil;
import org.digijava.module.message.dbentity.Approval;
import java.util.Date;
import org.digijava.module.message.dbentity.AmpMessage;

public class AmpMessageWorker {

    public static void processEvent(Event e) throws Exception{
    	String triggerClassName=e.getTrigger().getName();
    	List<TemplateAlert> tempAlerts=AmpMessageUtil.getTemplateAlerts(triggerClassName);
    	if(tempAlerts!=null && !tempAlerts.isEmpty()){
    		for (TemplateAlert template : tempAlerts) {
    			//AmpAlert newAlert=createAlertFromTemplate(template);
                AmpMessage newMsg=null;

    			AmpAlert newAlert=new AmpAlert();
                Approval newApproval=new Approval();

    			if(e.getTrigger().equals(ActivitySaveTrigger.class)){//<------ Someone created new Activity
    				newMsg=processActivitySaveEvent(e,newAlert,template);
    			}else if(e.getTrigger().equals(UserRegistrationTrigger.class)){//<----- Registered New User
    				newMsg=proccessUserRegistrationEvent(e,newAlert,template);
    			}else if(e.getTrigger().equals(ActivityDisbursementDateTrigger.class)){
    				newMsg=processActivityDisbursementDateComingEvent(e,newAlert,template);
                }else if(e.getTrigger().equals(CalendarEventTrigger.class)){
    				newMsg=proccessCalendarEvent(e,newAlert,template);
    			}else if(e.getTrigger().equals(ApprovedActivityTrigger.class)){
                    newMsg=processApprovedActivityEvent(e,newApproval,template);
                }else if(e.getTrigger().equals(NotApprovedActivityTrigger.class)){
                    newMsg=processNotApprovedActivityEvent(e,newApproval,template);
                }

    			AmpMessageUtil.saveOrUpdateMessage(newMsg);

    			//getting states according to tempalteId
    			List<AmpMessageState> statesRelatedToTemplate=null;
    			statesRelatedToTemplate=AmpMessageUtil.loadMessageStates(template.getId());
    			if(statesRelatedToTemplate!=null && statesRelatedToTemplate.size()>0){
    				for (AmpMessageState state : statesRelatedToTemplate) {
    					createMsgState(state,newMsg);
    				}
    			}
			}
    	}
    }

    /**
     *	Calendar Event Event processing
     */
    private static AmpAlert proccessCalendarEvent(Event e, AmpAlert alert,TemplateAlert template){
        //url
        String partialURL=null;
        if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)!=null){
            partialURL=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)+"/";
        }

        HashMap<String, String> myHashMap=new HashMap<String, String>();
        myHashMap.put(MessageConstants.OBJECT_NAME,(String)e.getParameters().get(UserRegistrationTrigger.PARAM_NAME));
        if(partialURL!=null){
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\""+partialURL+e.getParameters().get(CalendarEventTrigger.PARAM_URL)+"\">View Event</a>");
            alert.setObjectURL(partialURL+e.getParameters().get(CalendarEventTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_USER_MANAGER);

        return createAlertFromTemplate(template, myHashMap,alert);
    }
    /**
     *	Not Approved Activity Event processing
     */
    private static Approval processNotApprovedActivityEvent(Event e,Approval approval,TemplateAlert template){
        String partialURL=null;
        if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)!=null){
            partialURL=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)+"/";
        }
        HashMap<String, String> myHashMap=new HashMap<String, String>();
        myHashMap.put(MessageConstants.OBJECT_NAME,(String)e.getParameters().get(NotApprovedActivityTrigger.PARAM_NAME));
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ((AmpTeamMember)e.getParameters().get(NotApprovedActivityTrigger.PARAM_SAVED_BY)).getUser().getName());
        //url
        if(partialURL!=null){
            myHashMap.put(MessageConstants.OBJECT_URL,"<a href=\""+partialURL+e.getParameters().get(NotApprovedActivityTrigger.PARAM_URL)+"\">activity URL</a>");
            approval.setObjectURL(partialURL+e.getParameters().get(NotApprovedActivityTrigger.PARAM_URL));
        }
        approval.setSenderId(((AmpTeamMember)e.getParameters().get(NotApprovedActivityTrigger.PARAM_SAVED_BY)).getAmpTeamMemId());
        approval.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createApprovalFromTemplate(template, myHashMap,approval);
    }

    /**
     *	Approved Activity Event processing
     */
    private static Approval processApprovedActivityEvent(Event e,Approval approval,TemplateAlert template){
        String partialURL=null;
        if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)!=null){
            partialURL=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)+"/";
        }
        HashMap<String, String> myHashMap=new HashMap<String, String>();
        myHashMap.put(MessageConstants.OBJECT_NAME,(String)e.getParameters().get(ApprovedActivityTrigger.PARAM_NAME));
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ((AmpTeamMember)e.getParameters().get(ApprovedActivityTrigger.PARAM_SAVED_BY)).getUser().getName());
        //url
        if(partialURL!=null){
            myHashMap.put(MessageConstants.OBJECT_URL,"<a href=\""+partialURL+e.getParameters().get(ApprovedActivityTrigger.PARAM_URL)+"\">activity URL</a>");
            approval.setObjectURL(partialURL+e.getParameters().get(ApprovedActivityTrigger.PARAM_URL));
        }
        approval.setSenderId(((AmpTeamMember)e.getParameters().get(ApprovedActivityTrigger.PARAM_SAVED_BY)).getAmpTeamMemId());
        approval.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createApprovalFromTemplate(template, myHashMap,approval);
    }

    /**
	 *	Save Activity Event processing
	 */
    private static AmpAlert processActivitySaveEvent(Event e,AmpAlert alert,TemplateAlert template){
    	String partialURL=null;
    	if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)!=null){
			partialURL=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)+"/";
		}
    	HashMap<String, String> myHashMap=new HashMap<String, String>();
    	myHashMap.put(MessageConstants.OBJECT_NAME,(String)e.getParameters().get(ActivitySaveTrigger.PARAM_NAME));
    	myHashMap.put(MessageConstants.OBJECT_AUTHOR, ((AmpTeamMember)e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_BY)).getUser().getName());
    	//url
    	if(partialURL!=null){
    		myHashMap.put(MessageConstants.OBJECT_URL,"<a href=\""+partialURL+e.getParameters().get(ActivitySaveTrigger.PARAM_URL)+"\">activity URL</a>");
			alert.setObjectURL(partialURL+e.getParameters().get(ActivitySaveTrigger.PARAM_URL));
		}
    	alert.setSenderId(((AmpTeamMember)e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_BY)).getAmpTeamMemId());
    	alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
    	return createAlertFromTemplate(template, myHashMap,alert);

    }

	/**
	 *	User Registration Event processing
	 */
    private static AmpAlert proccessUserRegistrationEvent(Event e, AmpAlert alert,TemplateAlert template){
    	//url
    	String partialURL=null;
    	if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)!=null){
			partialURL=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)+"/";
		}

    	HashMap<String, String> myHashMap=new HashMap<String, String>();
    	myHashMap.put(MessageConstants.OBJECT_NAME,(String)e.getParameters().get(UserRegistrationTrigger.PARAM_NAME));
    	if(partialURL!=null){
    		myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\""+partialURL+e.getParameters().get(UserRegistrationTrigger.PARAM_URL)+"\">User Profile URL</a>");
    		alert.setObjectURL(partialURL+e.getParameters().get(UserRegistrationTrigger.PARAM_URL));
    	}
    	alert.setSenderType(MessageConstants.SENDER_TYPE_USER_MANAGER);

    	return createAlertFromTemplate(template, myHashMap,alert);
    }

	/**
	 * Activity's disbursement date Event processing
	 */
    private static AmpAlert processActivityDisbursementDateComingEvent(Event e, AmpAlert alert,TemplateAlert template){
    	String partialURL=null;
    	if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)!=null){
			partialURL=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)+"/";
		}
    	HashMap<String, String> myHashMap=new HashMap<String, String>();
    	myHashMap.put(MessageConstants.OBJECT_NAME,(String)e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_NAME));
    	//url
    	if(partialURL!=null){
    		myHashMap.put(MessageConstants.OBJECT_URL,"<a href=\""+partialURL+e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_URL)+"\">activity URL</a>");
			alert.setObjectURL(partialURL+e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_URL));
		}
    	alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
    	return createAlertFromTemplate(template, myHashMap,alert);
    }

    private static Approval createApprovalFromTemplate(TemplateAlert template,HashMap<String, String> myMap,Approval newApproval){
        newApproval.setName(DgUtil.fillPattern(template.getName(), myMap));
        newApproval.setDescription(DgUtil.fillPattern(template.getDescription(), myMap));
        newApproval.setReceivers(template.getReceivers());
        newApproval.setDraft(false);
        newApproval.setCreationDate(new Date());
        return newApproval;
	}

    private static AmpAlert createAlertFromTemplate(TemplateAlert template,HashMap<String, String> myMap,AmpAlert newAlert){
		newAlert.setName(DgUtil.fillPattern(template.getName(), myMap));
		newAlert.setDescription(DgUtil.fillPattern(template.getDescription(), myMap));
		newAlert.setReceivers(template.getReceivers());
		newAlert.setDraft(false);
		Calendar cal=Calendar.getInstance();
		newAlert.setCreationDate(cal.getTime());
		return newAlert;
	}


	private static void createMsgState(AmpMessageState state,AmpMessage newMsg){
		AmpMessageState newState=new AmpMessageState();
		newState.setMessage(newMsg);
		newState.setMemberId(state.getMemberId());
		newState.setRead(false);
		try {
			AmpMessageUtil.saveOrUpdateMessageState(newState);
		} catch (AimException e) {
			e.printStackTrace();
		}
	}

}
