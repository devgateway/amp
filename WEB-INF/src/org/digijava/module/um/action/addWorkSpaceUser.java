package org.digijava.module.um.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.um.form.AddUserForm;

public class addWorkSpaceUser extends Action{
	private static Logger logger = Logger.getLogger(addWorkSpaceUser.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws java.lang.Exception {
		
		AddUserForm upMemForm = (AddUserForm) form;
		ActionErrors errors = new ActionErrors();
		logger.debug("In add members");
		
		AmpTeam ampTeam = TeamUtil.getAmpTeam(upMemForm.getTeamId());
		User user = org.digijava.module.aim.util.DbUtil.getUser(upMemForm.getEmail());

		/* check if the user have entered an invalid user id */
		if (user == null) {
			upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"error.aim.addTeamMember.invalidUser"));
			saveErrors(request, errors);

			return mapping.findForward("forward");	
		}

		/* if user havent specified the role for the new member */
		if (upMemForm.getRole() == null) {
			upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"error.aim.addTeamMember.roleNotSelected"));
			saveErrors(request, errors);
			return mapping.findForward("forward");			
		}
		
		/* check if user have selected role as Team Lead when a team lead 
		 * already exist for the team */
		if (ampTeam.getTeamLead() != null &&
				ampTeam.getTeamLead().getAmpMemberRole().getAmpTeamMemRoleId().equals(upMemForm.getRole())) {
			upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError(
					"error.aim.addTeamMember.teamLeadAlreadyExist"));
			saveErrors(request, errors);
			return mapping.findForward("forward");			
		}
		
		/* check if user is already part of the selected team */
		if (TeamUtil.isMemberExisting(upMemForm.getTeamId(),upMemForm.getEmail())) {
			upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError(
					"error.aim.addTeamMember.teamMemberAlreadyExist"));
			saveErrors(request, errors);
			logger.debug("Member is already existing");
			return mapping.findForward("forward");			
		}
		/*check if user is not admin; as admin he can't be part of a workspace*/
		if (upMemForm.getEmail().equalsIgnoreCase("admin@amp.org")) {
			upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("error.aim.addTeamMember.teamMemberIsAdmin"));
			saveErrors(request, errors);
			logger.debug("Member is already existing");
			return mapping.findForward("forward");
		}
		
		AmpTeamMemberRoles role = TeamMemberUtil.getAmpTeamMemberRole(upMemForm.getRole());
		if (role != null) {
			AmpTeamMember newMember = new AmpTeamMember();
			newMember.setUser(user);
			newMember.setAmpTeam(ampTeam);
			newMember.setAmpMemberRole(role);
			// add the default permissions
			newMember.setReadPermission(role.getReadPermission());
			newMember.setWritePermission(role.getWritePermission());
			newMember.setDeletePermission(role.getDeletePermission());
			// add the default application settings for the user  
			AmpApplicationSettings ampAppSettings = org.digijava.module.aim.util.DbUtil
					.getTeamAppSettings(ampTeam.getAmpTeamId());
			AmpApplicationSettings newAppSettings = new AmpApplicationSettings();
			//newAppSettings.setTeam(ampAppSettings.getTeam());
			newAppSettings.setTeam(newMember.getAmpTeam());
			newAppSettings.setMember(newMember);
			newAppSettings.setDefaultRecordsPerPage(ampAppSettings
					.getDefaultRecordsPerPage());
			newAppSettings.setCurrency(ampAppSettings.getCurrency());
			newAppSettings.setFiscalCalendar(ampAppSettings
					.getFiscalCalendar());
			newAppSettings.setLanguage(ampAppSettings.getLanguage());
			newAppSettings.setUseDefault(new Boolean(true));
			Site site = RequestUtils.getSite(request);
			try{
				TeamUtil.addTeamMember(newMember,newAppSettings,site);
			}catch (Exception e){
					e.printStackTrace();
					logger.error("error when trying to add a new member: " + newMember.getUser().getEmail() + " from team: "
							+ newMember.getAmpTeam().getName());
			}
		
		}
		return mapping.findForward("forward");
	}	
}
