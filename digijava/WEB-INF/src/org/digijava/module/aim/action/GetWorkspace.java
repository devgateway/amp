package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.UpdateWorkspaceForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.Workspace;
import org.digijava.module.aim.util.TeamUtil;

public class GetWorkspace extends Action {

	private static Logger logger = Logger.getLogger(GetWorkspace.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		logger.debug("In GetWorkspace");
		
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

		String action = request.getParameter("actionEvent");
		Long teamId = null;

		logger.info("Action = " + uwForm.getActionEvent());

		if (request.getParameter("id") != null) {
			teamId = new Long(Long.parseLong(request.getParameter("id")));
			logger.debug("teamId : " + teamId);
		} else if (session.getAttribute("currentMember") != null) {
			TeamMember member = (TeamMember) session
					.getAttribute("currentMember");
			teamId = member.getTeamId();
		} else {
			return mapping.findForward("index");
		}

		Workspace workspace =TeamUtil.getWorkspace(teamId);
		
		if (workspace != null) {
			uwForm.setTeamId(new Long(workspace.getId()));
			uwForm.setTeamName(workspace.getName());
			uwForm.setType(workspace.getTeamCategory());
			uwForm.setWorkspaceType(workspace.getWorkspaceType());
			uwForm.setDescription(workspace.getDescription());
			uwForm.setChildWorkspaces(workspace.getChildWorkspaces());
			logger.debug("values set.");
		}

		if (action != null && action.equals("edit")) {
			uwForm.setActionEvent("edit");
			return mapping.findForward("showWorkspace");
		} else if (action != null && action.equals("delete")) {
			// check whether the team contain any members.
			if (workspace.isHasMembers()) {
				uwForm.setDeleteFlag("teamMembersExist");
			} else {
				if (workspace.isHasActivities()) {
					uwForm.setDeleteFlag("activitiesExist");
				} else {
					uwForm.setDeleteFlag("delete");
				}
			}
			uwForm.setActionEvent("delete");
			return mapping.findForward("showWorkspace");
		} else if (action != null && action.equals("view")) {
			uwForm.setActionEvent("view");
			return mapping.findForward("showWorkspace");
		} else if (action == null || action.equals("editFromTeamPage")) {
			logger.debug("no action set");
			uwForm.setActionEvent("editFromTeamPage");
			return mapping.findForward("forward");
		} else {
			return null;
		}
	}
}
