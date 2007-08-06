package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.digijava.module.aim.form.ReportsForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ReportUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.dbentity.AmpReports;

public class ShowTeamReports extends Action {

	private static Logger logger	= Logger.getLogger( ShowTeamReports.class );
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

			if (tm.getTeamHead() == true){
				dbReturnSet = new ArrayList(TeamUtil.getAllTeamReports(tm
						.getTeamId()));
			}
			else {
				dbReturnSet = TeamMemberUtil.getAllMemberReports(tm
						.getMemberId());
				if (dbReturnSet == null) 
					dbReturnSet = new ArrayList();
				AmpReports defaultReport	= (AmpReports)session.getAttribute(Constants.DEFAULT_TEAM_REPORT);
				if (defaultReport != null) {
					dbReturnSet.add( session.getAttribute(Constants.DEFAULT_TEAM_REPORT) );
				}
				else
					logger.info("There is no default team report set!");
			}
			rf.setReports(dbReturnSet);
		}

		return mapping.findForward("forward");
	}
}
