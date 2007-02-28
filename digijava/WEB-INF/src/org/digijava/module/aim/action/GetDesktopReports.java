package org.digijava.module.aim.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

public class GetDesktopReports extends TilesAction {

	public ActionForward execute(ComponentContext context,
			ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
		if (tm != null) {
				Collection reports;
				if (tm.getTeamHead() == true) {
					reports = TeamUtil.getAllTeamReports(tm.getTeamId());
				} else {
					reports = TeamMemberUtil.getAllMemberReports(tm.getMemberId());
				}
				session.setAttribute(Constants.MY_REPORTS,reports);
				session.setAttribute(Constants.TEAM_ID,tm.getTeamId());
				if(tm.getTeamHead()) session.setAttribute(Constants.TEAM_Head,"yes");
				session.setAttribute(Constants.TEAM_Head,"no");
				System.out.println(session.getAttribute("teamHead"));
			
		}
		return null;
	}
}
