package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.TeamMemberForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

public class UpdateTeamMemberList extends Action {

	private static Logger logger = Logger.getLogger(UpdateTeamMemberList.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		logger.debug("In update teammember list");
		TeamMemberForm tmForm = (TeamMemberForm) form;

		HttpSession session = request.getSession();
		if (session.getAttribute("currentMember") != null) {
			TeamMember tm = (TeamMember) session.getAttribute("currentMember");
			if (tmForm.getAddMember() != null) {
				tmForm.setTeamId(tm.getTeamId());
				tmForm.setAmpRoles(DbUtil.getAllTeamMemberRoles());
				tmForm.setPermissions("default");
				tmForm.setAction("teamWorkspaceSetup");
				return mapping.findForward("showAdd");
			} else if (tmForm.getRemoveMember() != null) {
				logger.debug("in remove member");
				Long selMembers[] = tmForm.getSelMembers();
				for (int i = 0; i < selMembers.length; i++) {
					if (selMembers[i] != null) {
						Long memId = selMembers[i];
						AmpTeamMember ampMember = new AmpTeamMember();
						ampMember.setAmpTeamMemId(memId);
						AmpTeamMember ampTeamHead = DbUtil.getTeamHead(tm
								.getTeamId());
						AmpTeam ampTeam = TeamUtil.getAmpTeam(tm.getTeamId());
						if (ampTeamHead.getAmpTeamMemId().equals(memId)) {
							logger.debug("team lead set to null");
							ampTeam.setTeamLead(null);
						} else {
							logger.debug("team lead remains same = "
									+ ampTeamHead.getUser().getName());
							ampTeam.setTeamLead(ampTeamHead);
						}
						DbUtil.update(ampTeam);
						AmpApplicationSettings ampAppSettings = DbUtil
								.getMemberAppSettings(tmForm.getTeamMemberId());
						if (ampAppSettings == null) {
							logger.debug("ampAppSettings is null");
						} else {
							logger.debug("got ampAppSettings");
						}
						DbUtil.delete(ampAppSettings);
						logger.debug("AppSettings deleted");
						DbUtil.delete(ampMember);
						logger.debug("TeamMember deleted");
					}
				}
				return mapping.findForward("forward");
			} else {
				return mapping.findForward(null);
			}
		} else {
			return mapping.findForward(null);
		}
	}
}
