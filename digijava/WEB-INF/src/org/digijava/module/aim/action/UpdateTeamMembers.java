package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.form.TeamMemberForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

public class UpdateTeamMembers extends Action {

	private static Logger logger = Logger.getLogger(UpdateTeamMembers.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		logger.debug("In update teammembers");
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
		

		try {
			TeamMemberForm upForm = (TeamMemberForm) form;
			ActionErrors errors = new ActionErrors();
			AmpTeam ampTeam = TeamUtil.getAmpTeam(upForm.getTeamId());
			if (upForm.getAction() != null
					&& upForm.getAction().trim().equals("edit")) {
				logger.debug("In edit team member");

				AmpTeamMember ampMember = new AmpTeamMember();
				ampMember.setAmpTeamMemId(upForm.getTeamMemberId());
				AmpTeamMemberRoles role = DbUtil.getAmpTeamMemberRole(upForm
						.getRole());
				AmpTeamMemberRoles teamLead = DbUtil.getAmpTeamHeadRole();
				if (role.getRole().equals(teamLead.getRole())) {
					logger.debug("team name = " + ampTeam.getName());
					if (ampTeam.getTeamLead() != null) {
						upForm.setAmpRoles(DbUtil.getAllTeamMemberRoles());
						errors
								.add(
										ActionErrors.GLOBAL_ERROR,
										new ActionError(
												"error.aim.addTeamMember.teamLeadAlreadyExist"));

						saveErrors(request, errors);

						return mapping.getInputForward();

					}

				}

				ampMember.setAmpMemberRole(role);
				if (upForm.getReadPerms() != null
						&& upForm.getReadPerms().equals("on")) {
					ampMember.setReadPermission(new Boolean(true));
				} else {
					ampMember.setReadPermission(new Boolean(false));

				}

				if (upForm.getWritePerms() != null
						&& upForm.getWritePerms().equals("on")) {
					ampMember.setWritePermission(new Boolean(true));
				} else {
					ampMember.setWritePermission(new Boolean(false));
				}
				if (upForm.getDeletePerms() != null
						&& upForm.getDeletePerms().equals("on")) {
					ampMember.setDeletePermission(new Boolean(true));
				} else {
					ampMember.setDeletePermission(new Boolean(false));
				}

				ampMember.setUser(UserUtils.getUser(upForm.getUserId()));
				ampMember.setAmpTeam(ampTeam);
				Collection col = DbUtil.getAllMemberAmpActivities(upForm
						.getTeamMemberId());
				Set temp = new HashSet();
				temp.addAll(col);
				ampMember.setActivities(temp);

				DbUtil.update(ampMember);
				AmpTeamMember ampTeamHead = DbUtil.getTeamHead(ampTeam
						.getAmpTeamId());

				if (ampTeam == null) {
					logger.debug("ampTeam is null");
				}

				if (ampTeamHead != null) {
					ampTeam.setTeamLead(ampTeamHead);
				} else {
					ampTeam.setTeamLead(null);
				}
				DbUtil.update(ampTeam);

				if (ampTeam != null) {
					request.setAttribute("teamId", ampTeam.getAmpTeamId());
					return mapping.findForward("forward");
				}
			} else if (upForm.getAction() != null
					&& upForm.getAction().trim().equals("delete")) {
				logger.debug("In delete team member");
				AmpTeamMember ampMember = new AmpTeamMember();
				ampMember.setAmpTeamMemId(upForm.getTeamMemberId());

				AmpTeamMember ampTeamHead = DbUtil.getTeamHead(ampTeam
						.getAmpTeamId());
				if (ampTeamHead != null) {
					if (ampTeamHead.getAmpTeamMemId().equals(
							upForm.getTeamMemberId())) {
						logger.debug("team lead set to null");
						ampTeam.setTeamLead(null);
					} else {
						logger.debug("team lead remains same = "
								+ ampTeamHead.getUser().getName());
						ampTeam.setTeamLead(ampTeamHead);
					}
					DbUtil.update(ampTeam);
				}
				AmpApplicationSettings ampAppSettings = DbUtil
						.getMemberAppSettings(upForm.getTeamMemberId());
				if (ampAppSettings == null) {
					logger.debug("ampAppSettings is null");
				} else {
					logger.debug("got ampAppSettings");
				}
				DbUtil.delete(ampAppSettings);
				logger.debug("AppSettings deleted");
				DbUtil.delete(ampMember);
				logger.debug("TeamMember deleted");

				if (ampTeam != null) {
					request.setAttribute("teamId", ampTeam.getAmpTeamId());
					return mapping.findForward("forward");
				}
			}

		} catch (Exception e) {
			logger.error("Exception  :" + e);
		}

		return null;

	}

}
