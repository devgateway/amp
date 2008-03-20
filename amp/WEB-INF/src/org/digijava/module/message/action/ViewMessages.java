package org.digijava.module.message.action;

import org.apache.struts.tiles.actions.TilesAction;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.message.form.AmpMessageForm;
import org.digijava.module.message.util.AmpMessageUtil;

public class ViewMessages extends TilesAction {
	
	public ActionForward execute(ComponentContext context, ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
    	TeamMember teamMember = new TeamMember();        	  
    	 // Get the current team member 
    	teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
    	//get from helper teamMember class AmpTeamMember, to see this User's Id 
    	AmpTeamMember ampTeamMember=TeamMemberUtil.getAmpTeamMember(teamMember.getMemberId());
    	Long userId=ampTeamMember.getUser().getId();
		AmpMessageForm messageForm=(AmpMessageForm)form;
		messageForm.setAllMessages(null);
		messageForm.setEditingMessage(false);		
		List<AmpMessage> messages= AmpMessageUtil.getAllMessagesForCurrentUser(teamMember.getTeamId(),userId,teamMember.getMemberId());
		messageForm.setAllMessages(messages);
		return null;
	}
}
