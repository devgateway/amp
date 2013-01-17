package org.digijava.module.visualization.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.visualization.dbentity.AmpDashboard;
import org.digijava.module.visualization.form.DashboardForm;
import org.digijava.module.visualization.util.DbUtil;


public class RemoveDashboard extends Action {
	private static Logger logger = Logger.getLogger(RemoveDashboard.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		DashboardForm dForm = (DashboardForm) form;
		if (request.getParameter("dashboardId") != null && Long.valueOf(request.getParameter("dashboardId")) > 0){
			AmpDashboard dashboard = DbUtil.getDashboardById(Long.valueOf(request.getParameter("dashboardId")));
			DbUtil.removeDashboard(dashboard);
		}
		return mapping.findForward("forward"); 
	}

}
