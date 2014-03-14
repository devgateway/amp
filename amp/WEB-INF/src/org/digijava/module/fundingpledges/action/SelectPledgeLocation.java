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


	/**
	 * imports locations from a collection into a form. If collection is null, imports nothing
	 * @param pledgeForm
	 * @param locs
	 */
	protected void importLocationsIntoForm(PledgeForm pledgeForm, Collection<AmpCategoryValueLocations> locs)
	{
		if (locs != null)
		{		
			for (AmpCategoryValueLocations loc : locs)
				pledgeForm.importLocationForLocationsForm(loc);
		}
	}
	
	protected void filterDisplayedLocations(PledgeForm pledgeForm, Set<Long> forbiddenLocations)
	{
		for(int layer:pledgeForm.getLocationByLayers().keySet())
		{
			java.util.List<KeyValue> ids = pledgeForm.getLocationByLayers().get(layer);
			Iterator<KeyValue> idIt = ids.iterator(); // remove from the possible values the ones which are forbidden
			while (idIt.hasNext())
			{
				long acvl = Long.valueOf(idIt.next().getKey());
				if (forbiddenLocations.contains(acvl))
					idIt.remove();
			}
		}
	}
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,javax.servlet.http.HttpServletRequest request,javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		PledgeForm pledgeForm = (PledgeForm) form;
		
		pledgeForm.setNoMoreRecords(false);

        /* New Region Manager changes */
        if ( !"true".equals( request.getParameter("edit") ) ) {
        	pledgeForm.cleanLocationData(true);
            return mapping.findForward("forward");
        }

        if (pledgeForm.getLevelId() <= 0) {
            CategoryManagerUtil.removeAmpCategryBykey("implementation_level");
            pledgeForm.cleanLocationData(true);
            return mapping.findForward("forward");
        }

		String resetSelLocs = request.getParameter("resetSelLocs");
		if (resetSelLocs != null && resetSelLocs.equalsIgnoreCase("reset")) {
			pledgeForm.setSelectedLocs(null);
		}
	       
		Long impLocLevel = null;

		AmpCategoryValue implLocLevelValue = CategoryManagerUtil.getAmpCategoryValueFromDb(pledgeForm.getLevelId());      
        String selectedImplemLocationLevel = request.getParameter("implemLocationLevel");
        
        if (selectedImplemLocationLevel != null) // implementation level selected
		{
			CategoryManagerUtil.removeAmpCategryBykey("implementation_location"); // hack: reset category manager cache for this key
			impLocLevel = Long.parseLong(selectedImplemLocationLevel);
			if (impLocLevel <= 0)
			{
            	pledgeForm.cleanLocationData(false); // reset selected
            	return mapping.findForward("forward");
			}
			pledgeForm.setImplemLocationLevel(impLocLevel);
			pledgeForm.setImplLocationValue(CategoryManagerUtil.getAmpCategoryValueFromDb(impLocLevel));
		}
				
		String defaultCountryIso = FeaturesUtil.getDefaultCountryIso();
		
		//Map<Integer, Collection<KeyValue>> locationByLayers	= pledgeForm.getLocationByLayers();
        pledgeForm.getLocationByLayers().clear();
        
		AmpCategoryValue implLevel = CategoryManagerUtil.getAmpCategoryValueFromDb( pledgeForm.getLevelId() );
		if (implLevel != null &&
				CategoryConstants.IMPLEMENTATION_LEVEL_INTERNATIONAL.equalsCategoryValue(implLevel) &&
				CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.equalsCategoryValue(implLocLevelValue) )
		{
			// international level selected: show list of all countries in the DB			
			importLocationsIntoForm(pledgeForm, DynLocationManagerUtil.getLocationsByLayer(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY));
			return mapping.findForward("forward");
		}

        if (CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.equalsCategoryValue(pledgeForm.getImplLocationValue()))
        {
            // This case add only one country. Set all countries should be implemented as a separate case
            // Please add the condition here that loads either one country or all countries
            pledgeForm.setDefaultCountryIsSet(true);
            pledgeForm.importLocationForLocationsForm(DynLocationManagerUtil.getLocationByIso(defaultCountryIso, CategoryManagerUtil.getAmpCategoryValueFromDb(impLocLevel)));
        } else { // it is regional, e.g. region or below
            // again, this is a default country. Add logic that does the same for selected country
            //AmpCategoryValueLocations defCountry = DynLocationManagerUtil.getLocationByIso(defaultCountryIso, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY, false);           
            Set<Long> forbiddenLocations = DynLocationManagerUtil.getRecursiveChildrenOfCategoryValueLocations(pledgeForm.getAllSelectedLocations()); // any selected locations and any of their descendants or ascendats are forbidden
            forbiddenLocations.addAll(DynLocationManagerUtil.getRecursiveAscendantsOfCategoryValueLocations(pledgeForm.getAllSelectedLocations()));
            
            Collection<AmpCategoryValueLocations> levelLocations = DynLocationManagerUtil.getLocationsByLayer(pledgeForm.getImplLocationValue());
            importLocationsIntoForm(pledgeForm, levelLocations);
            filterDisplayedLocations(pledgeForm, forbiddenLocations);
        } //it is region

        //pledgeForm.setImplemLocationLevel(null);
        return mapping.findForward("forward");
	}
}
