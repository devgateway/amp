/*
 * ShowAddTeamMember.java 
 */

package org.digijava.module.aim.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.form.TeamMemberForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.um.util.AmpUserUtil;

public class ShowAddTeamMember extends Action {
	
	private static Logger logger = Logger.getLogger(
			ShowAddTeamMember.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		
		TeamMemberForm upMemForm = (TeamMemberForm) form;

		String teamId = request.getParameter("teamId");
		String fromPage = request.getParameter("fromPage");
		
		if (teamId != null && fromPage != null) {
			try {
				Long id = new Long(Long.parseLong(teamId));	
				int frmPage = Integer.parseInt(fromPage);
				AmpTeam ampTeam = TeamUtil.getAmpTeam(id);
				upMemForm.setTeamId(id);
				upMemForm.setTeamName(ampTeam.getName());
				Collection<AmpTeamMemberRoles> roles=null;
				if(ampTeam.getAccessType().equals(Constants.ACCESS_TYPE_MNGMT)){
					roles=TeamMemberUtil.getAllTeamMemberRoles(false);
				}
				else{
					roles=TeamMemberUtil.getAllTeamMemberRoles();
				}
				upMemForm.setAmpRoles(roles);
				upMemForm.setPermissions("default");
				upMemForm.setFromPage(frmPage);
				upMemForm.setallUser(AmpUserUtil.getAllUsers(false));
				if (frmPage == 1) {
					return mapping.findForward("showAddFromAdmin");	
				} else {
					return mapping.findForward("showAddFromTeam");	
				}
			} catch (NumberFormatException nfe) {
				logger.error("NumberFormatException from ShowAddTeamMember action");
				logger.error("Trying to parse " + teamId + " to Long and " +
						fromPage + "to int");
			}
		}
		return mapping.findForward("index");			
	}
}
