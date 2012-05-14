/*
 * GetDonorTeamActivities.java
 * Created : 07-Feb-2006
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.TeamActivitiesForm;
import org.digijava.module.aim.util.TeamUtil;

// Test
public class GetDonorTeamActivities extends Action {
	
	private static Logger logger = Logger.getLogger(GetDonorTeamActivities.class);
	
	private static final char TYPE_ASSIGNED = 'A';
	private static final char TYPE_UNASSIGNED = 'U';
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		TeamActivitiesForm taForm = (TeamActivitiesForm) form;
		String forward = null;
		
		if (request.getParameter("type") == null ||
				request.getParameter("type").charAt(0) == TYPE_ASSIGNED) {
			Collection col = TeamUtil.getDonorTeamActivities(taForm.getDnrTeamId());
			taForm.setActivities(col);
			forward = "assigned";
		} else if (request.getParameter("type") != null &&
				request.getParameter("type").charAt(0) == TYPE_UNASSIGNED) {
			Collection col = TeamUtil.getDonorUnassignedActivities(taForm.getDnrTeamId(),taForm.getTeamId());
			taForm.setActivities(col);
			forward = "unassigned";
		}
		
		List list = new ArrayList(taForm.getActivities());
		Collections.sort(list);
		taForm.setActivities(list);		
		
		AmpTeam ampTeam = TeamUtil.getAmpTeam(taForm.getDnrTeamId());
		taForm.setTeamName(ampTeam.getName());
		
		return mapping.findForward(forward);
	}
}