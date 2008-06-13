package org.digijava.module.message.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.message.dbentity.AmpAlert;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.dbentity.TemplateAlert;
import org.digijava.module.message.util.AmpMessageUtil;

public class AmpMessageWorker {
	
    public static void processEvent(Event e) throws Exception{	
    	String triggerClassName=e.getTrigger().getName();
    	List<TemplateAlert> tempAlerts=AmpMessageUtil.getTemplateAlerts(triggerClassName);
    	if(tempAlerts!=null && tempAlerts.size()>0){
    		for (TemplateAlert template : tempAlerts) {
    			AmpAlert newAlert=createAlertFromTemplate(template);
    			if(e.getTrigger().equals(ActivitySaveTrigger.class)){//<------ Someone created new Activity
    				newAlert=processActivitySaveEvent(e,newAlert);
    			}else if(e.getTrigger().equals(UserRegistrationTrigger.class)){//<----- Registered New User
    				newAlert=proccessUserRegistrationEvent(e,newAlert);
    			}
    			AmpMessageUtil.saveOrUpdateMessage(newAlert);
    			
    			//getting states according to tempalteId
    			List<AmpMessageState> statesRelatedToTemplate=null;
    			statesRelatedToTemplate=AmpMessageUtil.loadMessageStates(template.getId());
    			if(statesRelatedToTemplate!=null && statesRelatedToTemplate.size()>0){
    				for (AmpMessageState state : statesRelatedToTemplate) {
    					createMsgState(state,newAlert);
    				}
    			}
			}
    		
    	}
    	
    }
    
    /**
     *	User Registration Event processing
     */
	private static AmpAlert proccessUserRegistrationEvent(Event e, AmpAlert alert){
		alert.setName(alert.getName()+e.getParameters().get(UserRegistrationTrigger.PARAM_NAME));
		Calendar cal=Calendar.getInstance();
		alert.setCreationDate(cal.getTime());
		
		if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)!=null){
			String partialURL=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)+"/";
			alert.setObjectURL(partialURL+e.getParameters().get(UserRegistrationTrigger.PARAM_URL));
		}	
		alert.setSenderType(MessageConstants.SENDER_TYPE_USER_MANAGER);
		return alert;
	}
	
	/**
	 *	Save Activity Event processing	 
	 */
	private static AmpAlert processActivitySaveEvent(Event e, AmpAlert alert){
		alert.setName(alert.getName()+e.getParameters().get(ActivitySaveTrigger.PARAM_NAME));
		alert.setCreationDate((Date)e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_DATE));
		alert.setSenderId(((AmpTeamMember)e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_BY)).getAmpTeamMemId());
		if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)!=null){
			String partialURL=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)+"/";
			alert.setObjectURL(partialURL+e.getParameters().get(ActivitySaveTrigger.PARAM_URL));
		}	
		alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
		return alert;
	}
	
	private static AmpAlert createAlertFromTemplate(TemplateAlert template){
		AmpAlert newAlert=new AmpAlert();
		newAlert.setName(template.getDescription());
		newAlert.setReceivers(template.getReceivers());	
		newAlert.setDraft(false);		
		return newAlert;
	}
	
	private static void createMsgState(AmpMessageState state,AmpAlert newAlert){
		AmpMessageState newState=new AmpMessageState();
		newState.setMessage(newAlert);
		newState.setMemberId(state.getMemberId());
		newState.setRead(false);		
		try {
			AmpMessageUtil.saveOrUpdateMessageState(newState);
		} catch (AimException e) {			
			e.printStackTrace();
		}
	}
	
}
