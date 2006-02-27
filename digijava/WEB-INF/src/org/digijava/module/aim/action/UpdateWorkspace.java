/*
 * UpdateWorkspace.java
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.UpdateWorkspaceForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;

public class UpdateWorkspace extends Action {

	private static Logger logger = Logger.getLogger(UpdateWorkspace.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		logger.debug("In updateWorkspace()");
		
		boolean permitted = false;
		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") != null) {
			String key = (String) session.getAttribute("ampAdmin");
			if (key.equalsIgnoreCase("yes")) {
				permitted = true;
			} else {
				if (session.getAttribute("teamLeadFlag") != null) {
					key = (String) session.getAttribute("teamLeadFlag");
					if (key.equalsIgnoreCase("true")) {
						permitted = true;	
					}
				}
			}
		}
		if (!permitted) {
			return mapping.findForward("index");
		}
		if (session.getAttribute("ampWorkspaces") != null) {
			session.removeAttribute("ampWorkspaces");
		}

		try {
		
		UpdateWorkspaceForm uwForm = (UpdateWorkspaceForm) form;
		
		String event = request.getParameter("event");
		String dest = request.getParameter("dest");
		
			
		ActionErrors errors = new ActionErrors();
		AmpTeam newTeam = null;
		if (uwForm.getTeamName() != null) {
			newTeam = new AmpTeam();
			newTeam.setAccessType(uwForm.getWorkspaceType());
			if (uwForm.getDescription() != null &&
					uwForm.getDescription().trim().length() > 0) {
				newTeam.setDescription(uwForm.getDescription());	
			} else {
				newTeam.setDescription(" ");
			}
			newTeam.setName(uwForm.getTeamName());
			newTeam.setType(uwForm.getType());
		}
		
		if (event != null && event.trim().equalsIgnoreCase("add")) {
			logger.debug("Workspace Add!");
			uwForm.setActionEvent("add");
			if (newTeam != null) {
				boolean teamExist = TeamUtil.createTeam(newTeam,uwForm.getChildWorkspaces());
				if (teamExist) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"error.aim.updateWorkspace.teamNameAlreadyExist"));
					saveErrors(request, errors);
					logger.debug("Team name already exist. Error message saved to request");
					return mapping.getInputForward();
				} else {
					logger.debug("Team created");
				}
			}
		} else if (event != null && event.trim().equalsIgnoreCase("edit")) {
			uwForm.setActionEvent("edit");
			logger.debug("Workspace Update!");
			if (newTeam != null) {
				newTeam.setAmpTeamId(uwForm.getTeamId());
				if (newTeam.getAccessType().equalsIgnoreCase("Team") &&
						(uwForm.getChildWorkspaces() != null && 
								uwForm.getChildWorkspaces().size() > 0)) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"error.aim.updateWorkspace.childTeamsExistForTeam"));
					saveErrors(request, errors);
					return mapping.getInputForward();					
				}
				boolean teamExist = TeamUtil.updateTeam(newTeam,uwForm.getChildWorkspaces());
				if (teamExist) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"error.aim.updateWorkspace.teamNameAlreadyExist"));
					saveErrors(request, errors);
					logger.debug("Team name already exist. Error message saved to request");
					return mapping.getInputForward();
				} else {
					logger.debug("Team updated");
					uwForm.setUpdateFlag(true);
				}
				TeamMember tm = (TeamMember) session.getAttribute("currentMember");
				if (tm != null) {
					if (tm.getTeamId() != null) {
						session.removeAttribute("currentMember");
						tm.setTeamName(newTeam.getName());
						session.setAttribute("currentMember", tm);						
					}
				}
			}
		} else if (event != null && event.trim().equalsIgnoreCase("delete")) {
			logger.debug("Workspace Delete!");
			String tId = request.getParameter("tId");
			Long teamId = new Long(Long.parseLong(tId));
			TeamUtil.removeTeam(teamId);
			logger.debug("Workspace deleted");
			return mapping.findForward("admin");

		}
		//uwForm.setReset(true);
		//uwForm.reset(mapping,request);
		
		return mapping.findForward(dest);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return null;
	}
}