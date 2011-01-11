package org.digijava.module.visualization.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.visualization.form.VisualizationForm;

public class ApplyFilters extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm) form;
		String visualizationType = request.getParameter("type") == null ? "donor"
				: (String) request.getParameter("type");
		return null;

	}

	

}
