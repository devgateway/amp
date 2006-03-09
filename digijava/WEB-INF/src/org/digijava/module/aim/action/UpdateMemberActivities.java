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
import org.digijava.module.aim.form.TeamActivitiesForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;

public class UpdateMemberActivities extends Action {

	private static Logger logger = Logger
			.getLogger(UpdateMemberActivities.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		logger.debug("In update member activities");
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

		try {
		
		TeamActivitiesForm taForm = (TeamActivitiesForm) form;

		Long id = null;
		TeamMember tm = null;

		if (session.getAttribute("currentMember") != null) {
			tm = (TeamMember) session.getAttribute("currentMember");
			id = tm.getTeamId();
		} else {
			return mapping.findForward("index");
		}

		if (taForm.getAddActivity() != null) {
			/* show all unassigned activities */
			Collection col = null;
			if (tm.getTeamType().equalsIgnoreCase(Constants.DEF_DNR_PERSPECTIVE)) {
				col = TeamUtil.getUnassignedDonorMemberActivities(id,
						taForm.getMemberId());
			} else {
				col = TeamUtil.getUnassignedMemberActivities(id,
						taForm.getMemberId());
			}
			List temp = (List) col;
			Collections.sort(temp);
			col = (Collection) temp;			
			taForm.setActivities(col);
			taForm.setTeamId(tm.getTeamId());
			return mapping.findForward("showAddActivity");
		} else if (taForm.getRemoveActivity() != null) {
			/* remove all selected activities */
			
			TeamUtil.removeActivitiesFromMember(taForm.getMemberId(),
					taForm.getSelActivities());
			
			return mapping.findForward("forward");
		} else if (taForm.getAssignActivity() != null) {
			/* add the selected activities to the team list */
			
			TeamUtil.assignActivitiesToMember(taForm.getMemberId(),
					taForm.getSelActivities());

			return mapping.findForward("forward");
			
		} else {
			return mapping.findForward(null);
		}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return null;
	}
}