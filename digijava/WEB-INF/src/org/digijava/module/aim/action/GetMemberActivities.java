package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.TeamActivitiesForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;

public class GetMemberActivities extends Action {

	private static Logger logger = Logger.getLogger(GetMemberActivities.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		logger.debug("In get member activities");
		
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
		

		TeamActivitiesForm taForm = (TeamActivitiesForm) form;

		Long id = null;

		if (request.getParameter("id") != null) {
			id = new Long(Long.parseLong(request.getParameter("id")));
		} else if (request.getAttribute("memberId") != null) {
			id = (Long) request.getAttribute("memberId");
		} else if (session.getAttribute("currentMember") != null) {
			TeamMember tm = (TeamMember) session.getAttribute("currentMember");
			id = tm.getMemberId();
		}

		if (id != null) {
			AmpTeamMember ampMem = DbUtil.getAmpTeamMember(id);
			Collection col = DbUtil.getAllMemberActivities(id);
			List temp = (List) col;
			Collections.sort(temp);
			col = (Collection) temp;						
			taForm.setActivities(col);
			taForm.setMemberId(id);
			taForm.setMemberName(ampMem.getUser().getName());
			return mapping.findForward("forward");
		} else {
			return null;
		}
	}
}