package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.List;

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
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.form.TeamMemberForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

public class AssignUsersToWorkspace extends Action {
	private static Logger logger = Logger.getLogger(AssignUsersToWorkspace.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws java.lang.Exception {
		
		String redirectWhere=request.getParameter("addedFrom");
		
		TeamMemberForm tmForm = (TeamMemberForm) form;
		String[] selectedUserIdsAndRoles=getCheckedUserIdsAndRoles(tmForm.getUserIdsWithRoles());
		AmpTeam ampTeam=TeamUtil.getAmpTeam(tmForm.getTeamId());
		for (int i = 0; i < selectedUserIdsAndRoles.length; i++) {
			String userIdAndRole=selectedUserIdsAndRoles[i];
			Long userId= new Long(userIdAndRole.substring(0, userIdAndRole.indexOf("_")));
			Long roleId= new Long(userIdAndRole.substring(userIdAndRole.indexOf("_")+1));
			
			User user=UserUtils.getUser(userId);
			/**
			 * check for errors
			 */
			ActionErrors errors = new ActionErrors();
			Site site = RequestUtils.retreiveSiteDomain(request).getSite();        
	        boolean siteAdmin = UserUtils.isAdmin(user, site);
	        if(siteAdmin){ // should  be impossible to add admin
	        	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.aim.addTeamMember.teamMemberIsAdmin"));
	        	saveErrors(request, errors);
	        	tmForm.setSomeError(true);
				logger.debug("Member is Admin");
				request.getSession().setAttribute("redirectTo", redirectWhere);
				return mapping.findForward("error");
	        }
	        
	        AmpTeamMemberRoles role = TeamMemberUtil.getAmpTeamMemberRole(roleId);
			if (role != null) {
				AmpTeamMember newMember = new AmpTeamMember();
				newMember.setUser(user);
				newMember.setAmpTeam(ampTeam);
				newMember.setAmpMemberRole(role);
				
				// add the default application settings for the user  
				AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(ampTeam.getAmpTeamId());
				AmpApplicationSettings newAppSettings = new AmpApplicationSettings();
				//newAppSettings.setTeam(ampAppSettings.getTeam());
				newAppSettings.setTeam(newMember.getAmpTeam());
				newAppSettings.setMember(newMember);
				newAppSettings.setDefaultRecordsPerPage(ampAppSettings.getDefaultRecordsPerPage());
				newAppSettings.setCurrency(ampAppSettings.getCurrency());
				newAppSettings.setFiscalCalendar(ampAppSettings	.getFiscalCalendar());
				newAppSettings.setLanguage(ampAppSettings.getLanguage());
				newAppSettings.setUseDefault(new Boolean(true));
				newAppSettings.setValidation(ampAppSettings.getValidation());
				try{
					TeamUtil.addTeamMember(newMember,newAppSettings,site);
				}catch (Exception e){
						e.printStackTrace();
						logger.error("error when trying to add a new member: " + newMember.getUser().getEmail() + " from team: "+ newMember.getAmpTeam().getName());
				}
			}
		}
		tmForm.setEmail(null);
		tmForm.setRole(null);
		tmForm.setTeamName(null);
//		if (tmForm.getFromPage() != 1) {
//			tmForm.setAmpRoles(null);
//		}
		tmForm.setPermissions(null);
		tmForm.setUserIdsWithRoles(null);
		tmForm.setTeamLeaderExists(null);
		tmForm.setWorkspaceManagerRoleId(null);
		tmForm.setTeamHead(null);
                HttpSession session = request.getSession();
                session.setAttribute("fromPage", tmForm.getFromPage());
                session.setAttribute("selectedRow", tmForm.getSelectedRow());
		
		return mapping.findForward(redirectWhere);

	}
	
	
	private String[] getCheckedUserIdsAndRoles(String [] submittedValues){		
		List<String> container=new ArrayList<String>();
		for (int i = 0; i < submittedValues.length; i++) {
			if(!submittedValues[i].equals("-1")){
				container.add(submittedValues[i]);
			}
		}
		String[] retVal=container.toArray(new String[container.size()]);
		return retVal;
	}
}
