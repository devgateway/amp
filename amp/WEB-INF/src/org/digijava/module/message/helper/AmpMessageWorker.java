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

public class AmpMessageWorker {
	
    public static void processEvent(Event e) throws Exception{	
    	String triggerClassName=e.getTrigger().getName();
    	List<TemplateAlert> tempAlerts=AmpMessageUtil.getTemplateAlerts(triggerClassName);
    	if(tempAlerts!=null && tempAlerts.size()>0){
    		for (TemplateAlert template : tempAlerts) {
    			//AmpAlert newAlert=createAlertFromTemplate(template);
    			AmpAlert newAlert=new AmpAlert();
    			if(e.getTrigger().equals(ActivitySaveTrigger.class)){//<------ Someone created new Activity
    				newAlert=processActivitySaveEvent(e,newAlert,template);
    			}else if(e.getTrigger().equals(UserRegistrationTrigger.class)){//<----- Registered New User
    				newAlert=proccessUserRegistrationEvent(e,newAlert,template);
    			}else if(e.getTrigger().equals(ActivityDisbursementDateTrigger.class)){
    				newAlert=processActivityDisbursementDateComingEvent(e,newAlert,template);
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
	 *	Save Activity Event processing	 
	 */
    private static AmpAlert processActivitySaveEvent(Event e,AmpAlert alert,TemplateAlert template){
    	String partialURL=null;
    	if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)!=null){
			partialURL=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)+"/";			
		}
    	HashMap<String, String> myHashMap=new HashMap<String, String>();
    	myHashMap.put(MessageConstants.OBJECT_NAME,(String)e.getParameters().get(ActivitySaveTrigger.PARAM_NAME));    	  
    	myHashMap.put(MessageConstants.OBJECT_AUTHOR, (String)e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_BY));    	
    	//url
    	if(partialURL!=null){
    		myHashMap.put(MessageConstants.OBJECT_URL,partialURL+e.getParameters().get(ActivitySaveTrigger.PARAM_URL));
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
    		myHashMap.put(MessageConstants.OBJECT_URL, partialURL+e.getParameters().get(UserRegistrationTrigger.PARAM_URL));
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
    		myHashMap.put(MessageConstants.OBJECT_URL,partialURL+e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_URL));
			alert.setObjectURL(partialURL+e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_URL));
		}
    	alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
    	return createAlertFromTemplate(template, myHashMap,alert);
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
