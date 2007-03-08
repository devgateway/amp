package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.form.ReportsForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

public class ShowTeamReports extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {
		ArrayList dbReturnSet = null;
		HttpSession session = request.getSession();

		ReportsForm rf = (ReportsForm) form;
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");

		if (tm == null) {
			Collection reports = ARUtil.getAllPublicReports();
			rf.setReports(reports);
		} else {

			if (tm.getTeamHead() == true)
				dbReturnSet = new ArrayList(TeamUtil.getAllTeamReports(tm
						.getTeamId()));
			else
				dbReturnSet = TeamMemberUtil.getAllMemberReports(tm
						.getMemberId());

			rf.setReports(dbReturnSet);
		}

		return mapping.findForward("forward");
	}
}
