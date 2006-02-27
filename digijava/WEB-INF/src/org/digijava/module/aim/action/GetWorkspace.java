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
		uwForm.setUpdateFlag(false);
		
		String dest = request.getParameter("dest");
		String id = request.getParameter("tId");
		logger.debug("dest = " + dest);
		logger.debug("id = " + id);
		Long teamId = new Long(0);
		try {
			if (id != null && id.trim().length() > 0) {
				teamId = new Long(Long.parseLong(id));	
				if (teamId.longValue() == -1) {
					TeamMember tm = (TeamMember) session.getAttribute("currentMember");
					teamId = tm.getTeamId();
				}
			} else {
				uwForm.setActionEvent("add");
				return mapping.findForward("showAddWorkspace");				
			}
		} catch (NumberFormatException nfe) {
			// incorrect id.
		}

		Workspace workspace =TeamUtil.getWorkspace(teamId);
		if (workspace != null) {
			uwForm.setTeamId(new Long(workspace.getId()));
			uwForm.setTeamName(workspace.getName());
			uwForm.setType(workspace.getTeamCategory());
			uwForm.setWorkspaceType(workspace.getWorkspaceType());
			uwForm.setDescription(workspace.getDescription());
			uwForm.setChildWorkspaces(workspace.getChildWorkspaces());
			uwForm.setActionEvent("edit");
		}			

		logger.debug("Dest value = " + dest);
		return mapping.findForward(dest);
	}
}
