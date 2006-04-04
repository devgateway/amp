/*
 * GetDonorTeams.java
 * Created : 06-Feb-2006
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.DonorTeamsForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;

/**
 * Loads the Donor Teams and thier respective Team leaders
 * of a particular MOFED team
 * @author priyajith
 *
 */

public class GetDonorTeams extends Action {
	private static Logger logger = Logger.getLogger(GetDonorTeams.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		DonorTeamsForm dtForm = (DonorTeamsForm) form;
		HttpSession session = request.getSession();
		
		String tempId = request.getParameter("teamId");
		Long tId = null;
		if (tempId != null) {
			tId = new Long(Long.parseLong(tempId));	
		} else {
			TeamMember tm = (TeamMember) session.getAttribute("currentMember");
			tId = tm.getTeamId();
		}
		
		
		dtForm.setTeamId(tId);
		dtForm.setDonorTeams(TeamUtil.getDonorTeams(tId));
		return mapping.findForward("forward");
	}
}
