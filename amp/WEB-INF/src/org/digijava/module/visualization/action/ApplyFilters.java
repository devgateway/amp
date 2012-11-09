package org.digijava.module.visualization.action;

import java.util.ArrayList;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.visualization.form.VisualizationForm;

public class ApplyFilters extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm) form;
		String visualizationType = request.getParameter("type") == null ? "donor"
				: (String) request.getParameter("type");
		
		String orgIdsStr = request.getParameter("orgIds");
		String[] orgIdsSpl = orgIdsStr.split("-");
		
		ArrayList<AmpOrganisation> orgs = new ArrayList<AmpOrganisation>();
		for (int i = 0; i < orgIdsSpl.length; i++) {
			if (orgIdsSpl[i]!=null && !orgIdsSpl[i].equals("null")) {
				orgs.add(DbUtil.getOrganisation(Long.valueOf(orgIdsSpl[i])));
			}
		}
		//visualizationForm.getFilter().setOrganizationsSelected(orgs);

		String secIdsStr = request.getParameter("secIds");
		String[] secIdsSpl = secIdsStr.split("-");
		
		ArrayList<AmpSector> secs = new ArrayList<AmpSector>();
		for (int i = 0; i < secIdsSpl.length; i++) {
			if (secIdsSpl[i]!=null && !secIdsSpl[i].equals("null")) {
				secs.add(SectorUtil.getAmpSector(Long.valueOf(secIdsSpl[i])));
			}
		}
		visualizationForm.getFilter().setSectorsSelected(secs);

		return mapping.findForward(visualizationType);

	}

}
