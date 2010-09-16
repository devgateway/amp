package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.form.UpdateRoleForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;

public class UpdateRoles extends Action {

	private static Logger logger = Logger.getLogger(UpdateRoles.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String) session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}

		UpdateRoleForm urForm = (UpdateRoleForm) form;

		logger.debug("In updtate roles");

		if (urForm.getAction() == null || urForm.getAction().equals("")) {
			urForm.setAction("add");
		}

		ActionMessages errors = new ActionMessages();

		if (session.getAttribute("ampRoles") != null) {
			session.removeAttribute("ampRoles");
		}

		if (urForm.getAction().equals("add")) {
			if (urForm.getRole() != null) {
				AmpTeamMemberRoles ampRoles = null;
				ampRoles = TeamMemberUtil.getAmpRoleByName(urForm.getRole());
				if (ampRoles != null) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"error.aim.updateRoles.roleAlreadyExist"));
					saveErrors(request, errors);
					return mapping.getInputForward();
				}
				ampRoles = new AmpTeamMemberRoles();
				ampRoles.setRole(urForm.getRole());
				if (urForm.getDescription() == null
						|| urForm.getDescription().trim().equals("")) {
					ampRoles.setDescription(" ");
				} else {
					ampRoles.setDescription(urForm.getDescription());
				}
				if (urForm.getReadPermission() != null
						&& urForm.getReadPermission().equals("on")) {
					ampRoles.setReadPermission(new Boolean(true));
				} else {
					ampRoles.setReadPermission(new Boolean(false));
				}
				if (urForm.getWritePermission() != null
						&& urForm.getWritePermission().equals("on")) {
					ampRoles.setWritePermission(new Boolean(true));
				} else {
					ampRoles.setWritePermission(new Boolean(false));
				}
				if (urForm.getDeletePermission() != null
						&& urForm.getDeletePermission().equals("on")) {
					ampRoles.setDeletePermission(new Boolean(true));
				} else {
					ampRoles.setDeletePermission(new Boolean(false));
				}
				if ((urForm.getTeamHead() != null && urForm.getTeamHead().equals("on"))
						|| urForm.getRole().equalsIgnoreCase("Team Leader")) {
					ampRoles.setTeamHead(new Boolean(true));
				} else {
					ampRoles.setTeamHead(new Boolean(false));
				}
				DbUtil.add(ampRoles);
				logger.debug("role added");
				return mapping.findForward("roles");
			} else {
				AmpTeamMemberRoles ampRole = TeamMemberUtil.getAmpTeamHeadRole();
				if (ampRole == null) {
					urForm.setTeamHeadFlag("no");
				} else {
					urForm.setTeamHeadFlag("yes");
				}
				urForm.setReadPermission("true");
				return mapping.findForward("showAdd");
			}
		} else if (urForm.getAction().equals("edit")) {
			if (urForm.getRoleId() != null) {
				AmpTeamMemberRoles ampRoles = new AmpTeamMemberRoles();
				ampRoles.setAmpTeamMemRoleId(urForm.getRoleId());
				ampRoles.setRole(urForm.getRole());

				if (urForm.getDescription() == null
						|| urForm.getDescription().trim().equals("")) {
					ampRoles.setDescription(" ");
				} else {
					ampRoles.setDescription(urForm.getDescription());
				}
				if (urForm.getReadPermission() != null
						&& urForm.getReadPermission().equals("on")) {
					ampRoles.setReadPermission(new Boolean(true));
				} else {
					ampRoles.setReadPermission(new Boolean(false));
				}
				if (urForm.getWritePermission() != null
						&& urForm.getWritePermission().equals("on")) {
					ampRoles.setWritePermission(new Boolean(true));
				} else {
					ampRoles.setWritePermission(new Boolean(false));
				}
				if (urForm.getDeletePermission() != null
						&& urForm.getDeletePermission().equals("on")) {
					ampRoles.setDeletePermission(new Boolean(true));
				} else {
					ampRoles.setDeletePermission(new Boolean(false));
				}
				if ((urForm.getTeamHead() != null && urForm.getTeamHead().equals("on"))
						|| urForm.getRole().equalsIgnoreCase("Workspace Manager")) {
					ampRoles.setTeamHead(new Boolean(true));
				} else {
					ampRoles.setTeamHead(new Boolean(false));
				}
				DbUtil.update(ampRoles);
				logger.debug("role updated");
			}
			return mapping.findForward("roles");
		} else if (urForm.getAction().equals("delete")) {
			if (urForm.getRoleId() != null) {
				AmpTeamMemberRoles ampRoles = new AmpTeamMemberRoles();
				ampRoles.setAmpTeamMemRoleId(urForm.getRoleId());
				DbUtil.delete(ampRoles);
				logger.debug("role deleted");
			}
			logger.debug("mapping to roles");
			return mapping.findForward("roles");
		} else
			return null;
	}
}
