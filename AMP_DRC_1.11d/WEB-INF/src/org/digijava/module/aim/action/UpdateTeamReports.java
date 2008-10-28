package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.ReportsForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

public class UpdateTeamReports extends Action {

	private static Logger logger = Logger.getLogger(UpdateTeamReports.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		logger.debug("In update team reports");

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

		ReportsForm raForm = (ReportsForm) form;

		Long id = null;
		TeamMember tm = null;

		boolean tabs = !raForm.isShowReportList();
		
		if (session.getAttribute("currentMember") != null) {
			tm = (TeamMember) session.getAttribute("currentMember");
			id = tm.getTeamId();
		}
		if (raForm.getRemoveReports() != null) {
			/* remove all selected reports */
			Long selReports[] = raForm.getSelReports();
			DbUtil.removeTeamReports(selReports,tm.getTeamId());
			raForm.setRemoveReports(null);

			/** setting the addReport property so that the system
			 * shows you the unassigned report list
			 */
			raForm.setAddReport(null);
			if(!raForm.isShowReportList())
				return mapping.findForward("forwardDesktopTabList");
			return mapping.findForward("forward");
		}
		if (raForm.getAddReport() != null) {
			/* show all unassigned reports */

			Collection col = TeamUtil.getAllUnassignedTeamReports(id, tabs);
			raForm.setReports(col);
			raForm.setTeamId(tm.getTeamId());
			raForm.setAddReport(null);
			return mapping.findForward("showAddReport");
		}
		else if (raForm.getAssignReports() != null) {
			/* add the selected reports to the team list */

			logger.info("in assign reports");
			Long selReports[] = raForm.getSelReports();
			DbUtil.addTeamReports(selReports,tm.getTeamId());
			raForm.setAssignReports(null);

			if(!raForm.isShowReportList())
				return mapping.findForward("forwardDesktopTabList");
			return mapping.findForward("forward");
		} else {
			return mapping.findForward(null);
		}
	}

	public boolean canDelete(Long repId) {
		logger.debug("In can delete");
		Iterator itr = DbUtil.getMembersUsingReport(repId).iterator();
		if (itr.hasNext()) {
			logger.debug("return false");
			return false;
		} else {
			logger.debug("return true");
			return true;
		}

	}
}

