package org.digijava.module.message.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.form.AmpMessageForm;
import org.digijava.module.message.util.AmpMessageUtil;

public class ViewMessages extends TilesAction {
	
	public ActionForward execute(ComponentContext context, ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
    	TeamMember teamMember = new TeamMember();        	  
    	 // Get the current team member 
    	teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);    	
		AmpMessageForm messageForm=(AmpMessageForm)form;		
		messageForm.setEditingMessage(false);		
		List<AmpMessageState> msgstates= AmpMessageUtil.loadReceivedAndSavedMsgStatesForTm(teamMember.getMemberId(),false);
		messageForm.setMessagesForTm(msgstates);
		//separate message types: used on myMessages page on desktop
		int alertType=0;
		int msgType=0;
		int approvalType=0;
		int calEventType=0;
		for (AmpMessageState state : msgstates) {
			if(state.getMessage().getClassName().equals("u") && state.getRead()!=null&& state.getRead().equals(false)){
				msgType++;
			}else if(state.getMessage().getClassName().equals("a") && state.getRead()!=null&& state.getRead().equals(false)){
				alertType++;
			}else if(state.getMessage().getClassName().equals("c") && state.getRead()!=null&& state.getRead().equals(false)){
				calEventType ++;
			}else if(state.getMessage().getClassName().equals("p") && state.getRead()!=null && state.getRead().equals(false)){
				approvalType++;
			}
		}
		messageForm.setMsgType(msgType);
		messageForm.setAlertType(alertType);
		messageForm.setCalendarEventType(calEventType);
		messageForm.setApprovalType(approvalType);
		
		return null;
	}
	
	
}
