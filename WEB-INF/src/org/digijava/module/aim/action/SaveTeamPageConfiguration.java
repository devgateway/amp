package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.TeamPagesForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;

public class SaveTeamPageConfiguration extends Action {

	private static Logger logger = Logger
			.getLogger(SaveTeamPageConfiguration.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		logger.debug("In SaveTeamPageConfiguration");
		
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
		
		TeamPagesForm tpForm = (TeamPagesForm) form;
		Long selFilters[] = tpForm.getSelFilters();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		try {
		    TeamUtil.updateTeamPageConfiguration(tm.getTeamId(),tpForm.getPageId(),selFilters);
		    tpForm.setUpdated(true);
		} catch (Exception e) {
		    logger.debug("Exception " + e);
		    e.printStackTrace(System.out);
		}
		
		if (session.getAttribute(Constants.DSKTP_FLTR_CHANGED) != null) {
			session.removeAttribute(Constants.DSKTP_FLTR_CHANGED);
		}
		return mapping.findForward("forward");
	}
}
