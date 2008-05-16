package org.digijava.module.message.helper;

import java.util.Calendar;

import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.message.dbentity.AmpAlert;
import org.digijava.module.message.dbentity.SystemMessage;
import org.digijava.module.message.util.AmpMessageUtil;

public class AmpMessageWorker {
    public static void processEvent(Event e) {
	//TODO: implement alert checking and message generation
    }
    
	public static void createAmpMessageForActivityCreation (Long ampActivityId,Long teamId) throws AimException,Exception {
		Long teamLeaderId=TeamMemberUtil.getTeamHead(teamId).getAmpTeamMemId();
		//SystemMessage message=new SystemMessage();
		AmpAlert alert=new AmpAlert();
		alert.setName("Created New Activity");
		alert.setDescription("Created New Activity with Id= "+ampActivityId);		
		//message.setReceiverType("TM"); //team Member
		//message.setReceiverId(teamLeaderId);
		//message.setSelectedTeamId(teamId); //So the member(if he is in 2 or more teams) will see a message only for current team 
		alert.setSenderType(MessageConstants.SENDER_TYPE_ACTIVITY); 
		alert.setSenderId(ampActivityId);
		alert.setPriorityLevel(MessageConstants.PRIORITY_LEVEL_MEDIUM); // by default priority level of such system message is Medium
		//message.setRead(false);
		Calendar cal=Calendar.getInstance();
		alert.setCreationDate(cal.getTime());
		AmpMessageUtil.saveOrUpdateMessage(alert);		
	}
	
	
}
