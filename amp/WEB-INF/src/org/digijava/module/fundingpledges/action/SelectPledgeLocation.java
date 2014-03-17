package org.digijava.module.fundingpledges.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesLocation;
import org.digijava.module.fundingpledges.form.PledgeForm;

public class SelectPledgeLocation extends Action {

	private static Logger logger = Logger.getLogger(SelectPledgeLocation.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,javax.servlet.http.HttpServletRequest request,javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		PledgeForm pledgeForm = (PledgeForm) form;
		
		String extraAction = request.getParameter("extraAction");
		if (extraAction != null)
		{
			// NEW ajax calls + dummy "please display form fragment" calls
			if (extraAction.equals("render_locations_list") || extraAction.equals("render_locations_add"))
				return mapping.findForward("forward");
		}
		

        if ( !"true".equals( request.getParameter("edit") ) ) { // no "edit" mentioned and not an "display form fragment" call -> this is a simple page load -> reset valid locations
        	pledgeForm.cleanLocationData(true);
            return mapping.findForward("forward");
        }

        // gone till here -> "edit=true" but NOT a "display something please" call": let's see what we have to do
        if (pledgeForm.getLevelId() <= 0) {
        	// implementation level changed to "not selected"
            CategoryManagerUtil.removeAmpCategryBykey("implementation_level");
            pledgeForm.cleanLocationData(true);
            return mapping.findForward("forward");
        }

		String resetSelLocs = request.getParameter("resetSelLocs");
		if (resetSelLocs != null && resetSelLocs.equalsIgnoreCase("reset")) { // asked to reset locations?
			pledgeForm.setSelectedLocs(null);
		}
	       
		Long impLocLevel = null;

		AmpCategoryValue implLocLevelValue = CategoryManagerUtil.getAmpCategoryValueFromDb(pledgeForm.getLevelId());      
        String selectedImplemLocationLevel = request.getParameter("implemLocationLevel");
        
        if (selectedImplemLocationLevel != null) // implementation level selected
		{
			impLocLevel = Long.parseLong(selectedImplemLocationLevel);
			CategoryManagerUtil.removeAmpCategryBykey("implementation_location"); // hack: reset category manager cache for this key
			if (impLocLevel <= 0)
			{
            	pledgeForm.cleanLocationData(false); // reset selected
            	return mapping.findForward("forward");
			}
			pledgeForm.setImplemLocationLevel(impLocLevel);
			pledgeForm.setImplLocationValue(CategoryManagerUtil.getAmpCategoryValueFromDb(impLocLevel));
		}
				
		//String defaultCountryIso = FeaturesUtil.getDefaultCountryIso();
		        
		final AmpCategoryValue implLevel = CategoryManagerUtil.getAmpCategoryValueFromDb( pledgeForm.getLevelId() );
		if (implLevel != null &&
				CategoryConstants.IMPLEMENTATION_LEVEL_INTERNATIONAL.equalsCategoryValue(implLevel) &&
				CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.equalsCategoryValue(implLocLevelValue) )
		{
			// international level selected: show list of all countries in the DB			
			//importLocationsIntoForm(pledgeForm, DynLocationManagerUtil.getLocationsByLayer(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY));
			return mapping.findForward("forward");
		}

        if (CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.equalsCategoryValue(pledgeForm.getImplLocationValue()))
        {
            // This case add only one country. Set all countries should be implemented as a separate case
            // Please add the condition here that loads either one country or all countries
            //pledgeForm.setDefaultCountryIsSet(true);
           // pledgeForm.importLocationForLocationsForm(DynLocationManagerUtil.getLocationByIso(defaultCountryIso, CategoryManagerUtil.getAmpCategoryValueFromDb(impLocLevel)));
            return mapping.findForward("forward");
        } 
        
        return mapping.findForward("forward");
	}
}
