package org.digijava.module.visualization.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.visualization.dbentity.AmpDashboard;
import org.digijava.module.visualization.form.DashboardForm;
import org.digijava.module.visualization.util.DbUtil;


public class AddDashboard extends Action {
	private static Logger logger = Logger.getLogger(AddDashboard.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		DashboardForm dForm = (DashboardForm) form;
		request.getSession().setAttribute("dashboardform", dForm);
		if (request.getParameter("reset") != null && request.getParameter("reset").equalsIgnoreCase("true")) {
        	resetForm(dForm);
        	request.getSession().removeAttribute("reset");
		} else if (request.getParameter("dashboardId") != null && Long.valueOf(request.getParameter("dashboardId")) > 0){
			AmpDashboard dashboard = DbUtil.getDashboardById(Long.valueOf(request.getParameter("dashboardId")));
			dForm.setDashboard(dashboard);
			dForm.setDashboardId(dashboard.getId());
			dForm.setDashboardName(dashboard.getName());
			dForm.setDashGraphList(DbUtil.getDashboardGraphByDashboard(dashboard.getId()));
			dForm.setBaseType(dashboard.getBaseType());
			dForm.setPivot(dashboard.getPivot());
			dForm.setShowInMenu(dashboard.getShowInMenu());
			dForm.setShowAcronymForOrgNames(dashboard.getShowAcronymForOrgNames());
			dForm.setMaxYearFilter(dashboard.getMaxYearFilter());
			dForm.setMinYearFilter(dashboard.getMinYearFilter());
			dForm.setTransactionTypeFilter(dashboard.getTransactionTypeFilter());
		}
		dForm.setGraphList(DbUtil.getAllGraphs());
		return mapping.findForward("forward");
	}

	private void resetForm(DashboardForm dForm){
		dForm.setDashboard(null);
		dForm.setDashboardId(null);
		dForm.setDashboardList(null);
		dForm.setDashboardName(null);
		dForm.setDashGraphList(null);
		dForm.setGraphList(null);
		dForm.setBaseType(0);
		dForm.setPivot(0);
		dForm.setMaxYearFilter(null);
		dForm.setMinYearFilter(null);
	}
}
