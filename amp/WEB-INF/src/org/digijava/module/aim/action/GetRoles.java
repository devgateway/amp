package org.digijava.module.aim.action;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.form.UpdateRoleForm;
import org.digijava.module.aim.util.TeamMemberUtil;

public class GetRoles extends Action {

	private static Logger logger = Logger.getLogger(GetRoles.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		UpdateRoleForm urForm = (UpdateRoleForm) form;

		logger.debug("In get role");
		String action = request.getParameter("action");
		Long roleId = null;

		if (request.getParameter("id") != null) {

			roleId = new Long(Long.parseLong(request.getParameter("id")));
			AmpTeamMemberRoles ampRole = TeamMemberUtil.getAmpTeamMemberRole(roleId);
			if (ampRole != null) {
				urForm.setRoleId(roleId);
				urForm.setRole(ampRole.getRole());
				urForm.setDescription(ampRole.getDescription());
				if (ampRole.getReadPermission().booleanValue() == true) {
					urForm.setReadPermission("on");
				}
				if (ampRole.getWritePermission().booleanValue() == true) {
					urForm.setWritePermission("on");
				}
				if (ampRole.getDeletePermission().booleanValue() == true) {
					urForm.setDeletePermission("on");
				}
			}
		}

		if (action != null && action.equals("edit")) {
			urForm.setAction("edit");
			return mapping.findForward("showRoles");
		} else if (action != null && action.equals("delete")) {
			Iterator itr = TeamMemberUtil.getMembersUsingRole(roleId).iterator();
			if (itr.hasNext()) {
				urForm.setFlag("membersAssigned");
			} else {
				urForm.setFlag("delete");
			}
			urForm.setAction("delete");
			return mapping.findForward("showRoles");
		} else if (action != null && action.equals("view")) {
			urForm.setAction("view");
			return mapping.findForward("showRoles");
		} else if (action == null) {
			logger.debug("no action set");
			return mapping.findForward("rolesManager");
		} else {
			return null;
		}
	}
}

