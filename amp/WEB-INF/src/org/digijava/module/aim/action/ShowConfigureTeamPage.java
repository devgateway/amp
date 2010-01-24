package org.digijava.module.aim.action;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.TeamPagesForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;

public class ShowConfigureTeamPage extends Action {
	private static Logger logger = Logger.getLogger(ShowConfigureTeamPage.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		TeamPagesForm tpForm = (TeamPagesForm) form;

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
		
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");

		if (!permitted) {
			return mapping.findForward("index");
		}

		Collection col = DbUtil.getAllConfigurablePages(tm.getTeamId());
		tpForm.setPages(col);

		return mapping.findForward("showConfigurePages");
	}
}