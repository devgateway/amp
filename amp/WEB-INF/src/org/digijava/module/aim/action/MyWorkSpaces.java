package org.digijava.module.aim.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.LoginForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;

public class MyWorkSpaces extends Action {

	private static Logger logger = Logger.getLogger(MyWorkSpaces.class);
	private static String SET_SET_DEFAULT = "default";
	private static String SET_SHOW = "show";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getParameter("action") != null) {
			String action = request.getParameter("action");
			if (action.equalsIgnoreCase(SET_SET_DEFAULT)) {
				return setAsDefaultTeam(mapping, form, request, response);
			}

			if (action.equalsIgnoreCase(SET_SHOW)) {
				return showTeams(mapping, form, request, response);
			}

		}
		return showTeams(mapping, form, request, response);
	}

	public ActionForward showTeams(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		LoginForm lForm = (LoginForm) form;
		
		Long id =null;
		TeamMember currentTeamMember =null;
		AmpTeamMember member = null;
		
		if (request.getParameter("id")!=null){
			id = Long.parseLong(request.getParameter("id"));
			member = TeamMemberUtil.getAmpTeamMember(id);
		}else{
			currentTeamMember = (TeamMember)session.getAttribute("currentMember");
			member = TeamMemberUtil.getAmpTeamMember(currentTeamMember.getMemberId());
		}
		  
	    Collection<AmpTeamMember> members = TeamMemberUtil.getAmpTeamMembersbyDgUser(member.getUser());
		lForm.setMembers(members);
		return mapping.findForward("selectTeam");
		
	}

	public ActionForward setAsDefaultTeam(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.parseLong(request.getParameter("id"));
		AmpTeamMember member = TeamMemberUtil.getAmpTeamMember(id);
		try {
			if (request.getParameter("unset") != null) {
				member.setByDefault(false);

			} else {
				Collection<AmpTeamMember> members = TeamMemberUtil.getAmpTeamMembersbyDgUser(member.getUser());
				for (AmpTeamMember ampTeamMember : members) {
					ampTeamMember.setByDefault(false);
					TeamMemberUtil.saveOrUpdateTM(ampTeamMember);
				}
				member.setByDefault(true);
			}

			TeamMemberUtil.saveOrUpdateTM(member);
		} catch (AimException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		return showTeams(mapping, form, request, response);
	}

}
