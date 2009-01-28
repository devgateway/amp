package org.digijava.module.dataExchange.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.dataExchange.form.ExportForm;
import org.digijava.module.dataExchange.jaxb.ActivityType;
import org.digijava.module.dataExchange.util.ExportHelper;


public class ExportWizardAction extends DispatchAction {

	private static Logger log = Logger.getLogger(ExportWizardAction.class);

	public ActionForward prepear(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("ExportWizardAction.prepear call");
		
		ExportForm eForm = (ExportForm)form;
		
		eForm.setActivityTree(ExportHelper.getActivityStruct("activity","activityTree","activity",ActivityType.class,true));
		eForm.setTeamList(TeamUtil.getAllTeams());
		
		String forward = "default";
		return mapping.findForward(forward);

	}
	


}
