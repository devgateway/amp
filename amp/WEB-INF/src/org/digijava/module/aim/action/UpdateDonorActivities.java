/*
 * UpdateDonorActivities
 * Created : 07-Feb-2006
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.TeamActivitiesForm;
import org.digijava.module.aim.util.TeamUtil;

// Test
public class UpdateDonorActivities extends Action {
	
	private static Logger logger = Logger.getLogger(UpdateDonorActivities.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form, 
			HttpServletRequest request,HttpServletResponse response) throws Exception {

		TeamActivitiesForm taForm = (TeamActivitiesForm) form;
		
		String action = request.getParameter("actionEvent");
		if (action != null && action.equalsIgnoreCase("delete")) {
			TeamUtil.removeActivitiesFromDonor(taForm.getDnrTeamId(),taForm.getSelActivities());
		} else {
			TeamUtil.assignActivitiesToDonor(taForm.getDnrTeamId(),taForm.getSelActivities());	
		}
		taForm.setSelActivities(null);
		return mapping.findForward("forward");
	}
}
