package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

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


		/*if (eaForm.getImplementationLevel().equals("country")) {
			eaForm.setImpLevelValue(new Integer(1));
		} else if (eaForm.getImplementationLevel().equals("region")) {
			eaForm.setImpLevelValue(new Integer(2));
		} else if (eaForm.getImplementationLevel().equals("zone")) {
			eaForm.setImpLevelValue(new Integer(3));
		} else if (eaForm.getImplementationLevel().equals("woreda")) {
			eaForm.setImpLevelValue(new Integer(4));
		}		*/

		Integer impLevelValue;
		AmpCategoryValue implLocValue	= CategoryManagerUtil.getAmpCategoryValueFromDb( eaForm.getLocation().getImplemLocationLevel() );
		if (implLocValue != null) {
			impLevelValue	= new Integer ( implLocValue.getIndex() + 1 );
		}
		else
			impLevelValue 	= new Integer( 1 );

		eaForm.setImpLevelValue( impLevelValue );
/*
 * modified by Govind
 */


		String iso= FeaturesUtil.getDefaultCountryIso();
                if(iso!=null){
                  eaForm.setDefaultCountryIsSet(true);
                  String CountryName = null;
                  logger.info(" this is the ISO .... in iso " + iso);
                  Collection b = FeaturesUtil.getDefaultCountry(iso);
                  Iterator itr2 = b.iterator();
                  while (itr2.hasNext()) {
                    Country ampGS = (Country) itr2.next();
                    logger.info(" hope this is the correct country name one.. " +
                                ampGS.getCountryName());
                    CountryName = ampGS.getCountryName();
                  }

                  if (fill == null || fill.trim().length() == 0) {
                    eaForm.setCountry(CountryName);
                    eaForm.setImpCountry(iso);
                    eaForm.setRegions(LocationUtil.getAllRegionsUnderCountry(
                        iso));
                    /*eaForm.setCountry(Constants.COUNTRY);
                        eaForm.setImpCountry(Constants.COUNTRY_ISO);
                        eaForm.setRegions(LocationUtil.getAllRegionsUnderCountry(Constants.COUNTRY_ISO));*/
                  }
                  else {
                    if (fill.equals("zone")) {
                      if (eaForm.getImpRegion() != null) {
                        eaForm.setZones(LocationUtil.getAllZonesUnderRegion(
                            eaForm.getImpRegion()));
                        //eaForm.setRegions(LocationUtil.getAllRegionsUnderCountry(Constants.COUNTRY_ISO));
                        eaForm.setRegions(LocationUtil.
                                          getAllRegionsUnderCountry(iso));
                        eaForm.setImpZone(null);
                        eaForm.setImpMultiZone(null);
                        eaForm.setImpMultiWoreda(null);
                        eaForm.setImpWoreda(null);
                        logger.info("Zones set");
                        logger.info("Zones set size : " +
                                    eaForm.getZones().size());
                      }
                    }
                    else if (fill.equals("woreda")) {
                      if (eaForm.getImpZone() != null) {
                        eaForm.setWoredas(LocationUtil.getAllWoredasUnderZone(
                            eaForm.getImpZone()));
                        eaForm.setZones(LocationUtil.getAllZonesUnderRegion(
                            eaForm.getImpRegion()));
                        //eaForm.setRegions(LocationUtil.getAllRegionsUnderCountry(Constants.COUNTRY_ISO));
                        eaForm.setRegions(LocationUtil.
                                          getAllRegionsUnderCountry(iso));
                        eaForm.setImpWoreda(null);
                      }
                    }
                  }

                  logger.info("Region = " + eaForm.getImpRegion());
                  logger.info("Imp. level value = " + eaForm.getImpLevelValue());
                  logger.info("Imp. level = " + eaForm.getLocation().getImplemLocationLevel());

                }
                else{
                eaForm.setDefaultCountryIsSet(false);
              }


		return mapping.findForward("forward");
	}
}
