package org.digijava.module.visualization.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.visualization.form.DashboardForm;
import org.digijava.module.visualization.util.DbUtil;


public class ShowDashboardsList extends Action {
	private static Logger logger = Logger.getLogger(ShowDashboardsList.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		DashboardForm dForm = (DashboardForm) form;
		
		dForm.setDashboardList(DbUtil.getAllDashboards());
		return mapping.findForward("forward");

	}

}
