package org.digijava.module.fundingpledges.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
		pledgeForm.setNoMoreRecords(false);


        /* New Region Manager changes */
        if ( !"true".equals( request.getParameter("edit") ) ) {
            pledgeForm.setImplemLocationLevel(-1l);
            pledgeForm.setLevelId(-1l);
            pledgeForm.setParentLocId(null);
            pledgeForm.getLocationByLayers().clear();
            pledgeForm.getSelectedLayers().clear();

            // this if for FundingPledgesLocation. Not sure why this is in this code
            //pledgeForm.setSelectedLocs(null);
            pledgeForm.setUserSelectedLocs(null);

            return mapping.findForward("forward");
        }

        if (pledgeForm.getLevelId() <= 0) {
            CategoryManagerUtil.removeAmpCategryBykey("implementation_level");

            pledgeForm.setImplemLocationLevel(-1l);
            pledgeForm.setLevelId(-1l);
            pledgeForm.setParentLocId(null);
            pledgeForm.getLocationByLayers().clear();
            pledgeForm.getSelectedLayers().clear();

            // this if for FundingPledgesLocation. Not sure why this is in this code
            //pledgeForm.setSelectedLocs(null);
            pledgeForm.setUserSelectedLocs(null);

            return mapping.findForward("forward");
        }

		String resetSelLocs = request.getParameter("resetSelLocs");
		if (resetSelLocs != null && resetSelLocs.equalsIgnoreCase("reset")) {
			pledgeForm.setSelectedLocs(null);
		}

		Integer impLevelValue = null;
		Long impLocLevel = null;


        AmpCategoryValue implLocLevelValue = CategoryManagerUtil.getAmpCategoryValueFromDb(pledgeForm.getLevelId());

		if (request.getParameter("implemLocationLevel") != null) {

            CategoryManagerUtil.removeAmpCategryBykey("implementation_location");

			impLocLevel = Long.parseLong(request.getParameter("implemLocationLevel"));
            if (impLocLevel > 0) {
                pledgeForm.setImplemLocationLevel(impLocLevel);
                pledgeForm.setImplLocationValue(CategoryManagerUtil.getAmpCategoryValueFromDb(impLocLevel));
            } else {
                pledgeForm.setParentLocId(null);
                pledgeForm.getLocationByLayers().clear();
                pledgeForm.getSelectedLayers().clear();

                // this if for FundingPledgesLocation. Not sure why this is in this code
                // pledgeForm.setSelectedLocs(null);
                pledgeForm.setUserSelectedLocs(null);

                return mapping.findForward("forward");
            }
		}

		pledgeForm.setImpLevelValue( impLevelValue );
		
		String defaultCountryIso = FeaturesUtil.getDefaultCountryIso();
		
		Map<Integer, Collection<KeyValue>> locationByLayers	= pledgeForm.getLocationByLayers();
		
		AmpCategoryValue implLevel = CategoryManagerUtil.getAmpCategoryValueFromDb( pledgeForm.getLevelId() );
		if (implLevel != null &&
				CategoryManagerUtil.equalsCategoryValue(implLevel, CategoryConstants.IMPLEMENTATION_LEVEL_INTERNATIONAL) &&
				CategoryManagerUtil.equalsCategoryValue(implLocLevelValue, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY) )  {
			Collection<AmpCategoryValueLocations> countries =
				DynLocationManagerUtil.getLocationsByLayer(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
			if (countries != null) {
				Integer countryIndex = null;
				ArrayList<KeyValue> countriesKV = new ArrayList<KeyValue>();
				for (AmpCategoryValueLocations country : countries) {
					countryIndex = (countryIndex == null) ? country.getParentCategoryValue().getIndex() : countryIndex;
					KeyValue countryKV = new KeyValue(country.getId().toString(), country.getName());
					countriesKV.add(countryKV);
				}
				locationByLayers.put(countryIndex, countriesKV);
			}
			return mapping.findForward("forward");
		}
	
		 /*
        if(defaultCountryIso!=null && (parentLocId == null || parentLocId == 0)) { // Setting up the country
        	pledgeForm.setDefaultCountryIsSet(true);
        	AmpCategoryValueLocations defCountry		= DynLocationManagerUtil.getLocationByIso(defaultCountryIso, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
        	Integer countryLayerIndex						= defCountry.getParentCategoryValue().getIndex();
        	KeyValue countryKV							= new KeyValue(defCountry.getId().toString() , defCountry.getName() );
        	ArrayList<KeyValue> countries				= new ArrayList<KeyValue>();
        	countries.add(countryKV);
        	locationByLayers.put(countryLayerIndex, countries);
        	if ( countryLayerIndex < implLocValue.getIndex() ) {
        		parentLocId			= defCountry.getId();
        	}
        }*/
        if (CategoryManagerUtil.equalsCategoryValue(pledgeForm.getImplLocationValue(), CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY)) {
            locationByLayers.clear();
        	AmpCategoryValueLocations defCountry = DynLocationManagerUtil.getLocationByIso(defaultCountryIso, CategoryManagerUtil.getAmpCategoryValueFromDb(impLocLevel));

            // This case add only one country. Set all countries should be implemented as a separate case
            // Please add the condition here that loads either one country or all countries
            pledgeForm.setDefaultCountryIsSet(true);
            Integer countryLayerIndex = defCountry.getParentCategoryValue().getIndex();
            KeyValue countryKV = new KeyValue(defCountry.getId().toString() , defCountry.getName() );
            ArrayList<KeyValue> countries = new ArrayList<KeyValue>();
            countries.add(countryKV);
            locationByLayers.put(countryLayerIndex, countries);

        } else { //it is region
            locationByLayers.clear();
            // again, this is a default country. Add logic that does the same for selected country

            // These lines won't work here because the location in CategoryManagerUtil.categoryValuesByKey is already set to "REGION"
            // AmpCategoryValueLocations defCountry = DynLocationManagerUtil.getLocationByIso(defaultCountryIso, CategoryManagerUtil.getAmpCategoryValueFromDb(76l));
            // AmpCategoryValueLocations defCountry = DynLocationManagerUtil.getLocationByIso(defaultCountryIso, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
            AmpCategoryValueLocations defCountry = DynLocationManagerUtil.getLocationByIso(defaultCountryIso, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY, false);

            Long parentLocId = defCountry.getId();
            int lastLayer = 0;

            while (parentLocId != null && parentLocId > 0) {
                AmpCategoryValueLocations parentLoc	= DynLocationManagerUtil.getLocation(parentLocId, true);
                pledgeForm.getSelectedLayers().put(parentLoc.getParentCategoryValue().getIndex(), parentLoc.getId() );

                Iterator<Entry<Integer, Collection<KeyValue>>> entryIter = locationByLayers.entrySet().iterator();
                while ( entryIter.hasNext() ) {
                    /**
                     * In case the user changes a higher layer location all layers below need to be cleared.
                     */
                    if ( entryIter.next().getKey() > parentLoc.getParentCategoryValue().getIndex() ) {
                        entryIter.remove();
                    }
                }

                parentLocId	= null;
                Collection<AmpCategoryValueLocations> childrenLocs 	= parentLoc.getChildLocations();
                if ( childrenLocs != null && childrenLocs.size() > 0 ) {
                    Integer currentLayer = (parentLoc.getParentCategoryValue().getIndex()) + 1;
                        //((AmpCategoryValueLocations)childrenLocs.toArray()[0]).getParentCategoryValue().getIndex();

                    // this is actually why municipalities are not loaded
                    if ( /*currentLayer <= implLocLevelValue.getIndex() */true) {
                        ArrayList<KeyValue> childrenKV					= new ArrayList<KeyValue>(childrenLocs.size());
                        for ( AmpCategoryValueLocations child : childrenLocs ) {
                            boolean addLocationAllowed		= true;
                            /**
                             * If we are on the last layer that needs to be shown, we have to
                             * hide locations that are already selected
                             */
                            if ( currentLayer == implLocLevelValue.getIndex() && pledgeForm.getSelectedLocs() != null ) {
                                for ( FundingPledgesLocation l: pledgeForm.getSelectedLocs() ) {
                                    if ( child.equals(l.getLocation()) ) {
                                        addLocationAllowed	= false;
                                        break;
                                    }
                                }
                            }
                            if ( addLocationAllowed ) {
                                childrenKV.add(new KeyValue(child.getId().toString(), child.getName()));
                            }
                        }

                        lastLayer = currentLayer;
                        locationByLayers.put(currentLayer, childrenKV);

                        if ( childrenLocs.size() == 1 ) {
                            AmpCategoryValueLocations newParent	= (AmpCategoryValueLocations)childrenLocs.toArray()[0];
                            parentLocId	= newParent.getId();
                        }
                    }

                } else {
                    if (lastLayer < implLocLevelValue.getIndex()) {
                        pledgeForm.setNoMoreRecords(true);
                    }
                }

            }
        } //it is region

        //pledgeForm.setImplemLocationLevel(null);
        return mapping.findForward("forward");
	}
}
