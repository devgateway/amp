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

		UpdateWorkspaceForm uwForm = (UpdateWorkspaceForm) form;
		uwForm.setReset(false);

		try {
			
		logger.info("In update workspace");
		logger.info("Action :" + uwForm.getActionEvent());

		if (uwForm.getActionEvent() == null || uwForm.getActionEvent().equals("")) {
			uwForm.setChildWorkspaces(null);
			uwForm.setWorkspaceType(null);
			uwForm.setActionEvent("add");
		}

		if (session.getAttribute("ampWorkspaces") != null) {
			session.removeAttribute("ampWorkspaces");
		}

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

		logger.info("Action :" + uwForm.getActionEvent());		
		if (uwForm.getActionEvent().equals("add")) {
			logger.info("Adding teams ...");
			if (newTeam != null) {
				boolean teamExist = TeamUtil.createTeam(newTeam,uwForm.getChildWorkspaces());
				if (teamExist) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"error.aim.updateWorkspace.teamNameAlreadyExist"));
					saveErrors(request, errors);
					return mapping.getInputForward();					
				}
				logger.debug("workspace added");
				uwForm.setReset(true);
				uwForm.reset(mapping,request);
				return mapping.findForward("workspaceManager");
			}
			return mapping.findForward("showAdd");
		} else if (uwForm.getActionEvent().equals("edit")
				|| uwForm.getActionEvent().equals("editFromTeamPage")) {
			logger.info("Action from edit = " + uwForm.getActionEvent());
			if (newTeam != null) {
				newTeam.setAmpTeamId(uwForm.getTeamId());
				logger.info("Editing an activity " + newTeam.getAmpTeamId());
				boolean teamExist = TeamUtil.updateTeam(newTeam,uwForm.getChildWorkspaces());
				logger.info("Team updated, teamExist :" + teamExist);
				if (teamExist) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"error.aim.updateWorkspace.teamNameAlreadyExist"));
					saveErrors(request, errors);
					return mapping.getInputForward();	
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
			logger.info("Action from edit = " + uwForm.getActionEvent());
			if (uwForm.getActionEvent().equals("editFromTeamPage")) {
				logger.debug("returning editFromTeamPage");
				return mapping.findForward("workspaceOverview");
			} else {
				uwForm.setReset(true);
				uwForm.reset(mapping,request);				
				return mapping.findForward("workspaceManager");
			}
		} else if (uwForm.getActionEvent().equals("delete")) {
			if (uwForm.getTeamId() != null) {
				
				TeamUtil.removeTeam(uwForm.getTeamId());
				logger.debug("workspace deleted");
			}
			uwForm.setReset(true);
			uwForm.reset(mapping,request);				
			return mapping.findForward("workspaceManager");
		} else
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return null; // to be removed while removing try catch
	}
}
