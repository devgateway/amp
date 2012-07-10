package org.digijava.module.visualization.action;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.visualization.dbentity.AmpDashboard;
import org.digijava.module.visualization.dbentity.AmpDashboardGraph;
import org.digijava.module.visualization.dbentity.AmpGraph;
import org.digijava.module.visualization.form.DashboardForm;
import org.digijava.module.visualization.util.DbUtil;


public class SaveDashboard extends Action {
	private static Logger logger = Logger.getLogger(SaveDashboard.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		DashboardForm dForm = (DashboardForm) form;

		AmpDashboard dashboard = null;
		if (dForm.getDashboard()==null) {
			dashboard = new AmpDashboard();
		} else {
			dashboard = dForm.getDashboard();
		}
		
		dashboard.setId(dForm.getDashboardId());
		dashboard.setName(dForm.getDashboardName());
		dashboard.setBaseType(dForm.getBaseType());
		String graphs = request.getParameter("graphs");
		String grs[] = graphs.split("_");
		dForm.setDashGraphList(new ArrayList<AmpDashboardGraph>());
		for (int i = 0; i < grs.length; i++) {
			if (grs[i].length()>0){
				AmpGraph graph = DbUtil.getGraphById(Long.valueOf(grs[i]));
				AmpDashboardGraph dashGraph = new AmpDashboardGraph();
				dashGraph.setGraph(graph);
				dashGraph.setOrder(i+1);
				dForm.getDashGraphList().add(dashGraph);
			}
		}
		if (dForm.getDashboardId()!=null && dForm.getDashboardId()!=0) {
			DbUtil.updateDashboard(dashboard, dForm);
		} else {
			DbUtil.saveDashboard(dashboard, dForm);
		}
		return mapping.findForward("forward");

	}

}
