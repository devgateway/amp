package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LocationUtil;

public class SelectLocation extends Action {

	private static Logger logger = Logger.getLogger(SelectLocation.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		EditActivityForm eaForm = (EditActivityForm) form;
		
		if (request.getParameter("locationReset") != null
				&& request.getParameter("locationReset").equals("false")) {
			eaForm.setLocationReset(false);
		} else {
			eaForm.setLocationReset(true);
			logger.info("calling reset");
			eaForm.reset(mapping, request);
		}

		String fill = request.getParameter("fill");
		
				
		if (eaForm.getImplementationLevel().equals("country")) {
			eaForm.setImpLevelValue(new Integer(1));
		} else if (eaForm.getImplementationLevel().equals("region")) {
			eaForm.setImpLevelValue(new Integer(2));
		} else if (eaForm.getImplementationLevel().equals("zone")) {
			eaForm.setImpLevelValue(new Integer(3));
		} else if (eaForm.getImplementationLevel().equals("woreda")) {
			eaForm.setImpLevelValue(new Integer(4));
		}		
		
		if (fill == null || fill.trim().length() == 0) {
			eaForm.setCountry(Constants.COUNTRY);
			eaForm.setImpCountry(Constants.COUNTRY_ISO);
			eaForm.setRegions(LocationUtil.getAllRegionsUnderCountry(Constants.COUNTRY_ISO));
		} else {
			if (fill.equals("zone")) {
				if (eaForm.getImpRegion() != null) {
					eaForm.setZones(LocationUtil.getAllZonesUnderRegion(eaForm.getImpRegion()));
					eaForm.setRegions(LocationUtil.getAllRegionsUnderCountry(Constants.COUNTRY_ISO));
					eaForm.setImpZone(null);
					eaForm.setImpMultiWoreda(null);
					eaForm.setImpWoreda(null);					
					logger.info("Zones set");
					logger.info("Zones set size : " + eaForm.getZones().size());
				}
			} else if (fill.equals("woreda")) {
				if (eaForm.getImpZone() != null) {
					eaForm.setWoredas(LocationUtil.getAllWoredasUnderZone(eaForm.getImpZone()));
					eaForm.setZones(LocationUtil.getAllZonesUnderRegion(eaForm.getImpRegion()));
					eaForm.setRegions(LocationUtil.getAllRegionsUnderCountry(Constants.COUNTRY_ISO));
					eaForm.setImpWoreda(null);
				}
			}
		}
		
		logger.info("Region = " + eaForm.getImpRegion());
		logger.info("Imp. level value = " + eaForm.getImpLevelValue());
		logger.info("Imp. level = " + eaForm.getImplementationLevel());
		
		return mapping.findForward("forward");
	}
}
