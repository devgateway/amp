package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.RegionalFunding;

public class RemoveRegionalFunding extends Action {

	private static Logger logger = Logger.getLogger(RemoveRegionalFunding.class);

	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		EditActivityForm eaForm = (EditActivityForm) form;
		
		String regIdStr = request.getParameter("regId");
		long regId = -1;
		if (regIdStr != null) {
			try {
				regId = Long.parseLong(regIdStr);
			} catch (NumberFormatException nfe) {
				logger.error("Invalid region Id :" + regIdStr);
			}
		}

		RegionalFunding rf = new RegionalFunding();
		rf.setRegionId(regId);
		eaForm.getFunding().getRegionalFundings().remove(rf);
		return mapping.findForward("forward");
	}
}

