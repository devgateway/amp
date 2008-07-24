package org.digijava.module.widget.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.widget.form.SectorByDonorTeaserForm;

/**
 * Pie chart on GIS page.
 * @author Irakli Kobiashvili
 *
 */
public class SectorsByDonorTeaser extends TilesAction {

	@Override
	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SectorByDonorTeaserForm tForm = (SectorByDonorTeaserForm)form;
		tForm.setSelectedYear("2008");
		tForm.setSelectedDonor(new Long(-1));
		Collection<AmpOrganisation> donors = DbUtil.getDonors();
		tForm.setDonors(donors);
		
		return null;
	}

}
