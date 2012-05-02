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
import org.digijava.kernel.dbentity.Country;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.EditActivityForm.Location;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
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
		
		String resetSelLocs=request.getParameter("resetSelLocs"); 
		if(resetSelLocs!=null && resetSelLocs.equalsIgnoreCase("reset")){
			pledgeForm.setSelectedLocs(null);
		}

		Integer impLevelValue;
		Long impLocLevel=null;
		if(request.getParameter("implemLocationLevel")!=null){
			impLocLevel=new Long(request.getParameter("implemLocationLevel"));
			pledgeForm.setImplemLocationLevel(impLocLevel);			
		}
		pledgeForm.setLevelId(CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.IMPLEMENTATION_LEVEL_NATIONAL).getId());
		AmpCategoryValue implLocValue	= CategoryManagerUtil.getAmpCategoryValueFromDb( pledgeForm.getImplemLocationLevel() );
		if ( implLocValue == null )
			implLocValue				= CategoryManagerUtil.getAmpCategoryValueFromDb( CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY).getId()+1);
		if (implLocValue != null) {
			impLevelValue	= new Integer ( implLocValue.getIndex() + 1 );
		}
		else
			impLevelValue 	= new Integer( 1 );

		pledgeForm.setImpLevelValue( impLevelValue );
		
		/* New Region Manager changes */
		if ( !"true".equals( request.getParameter("edit") ) ) {
			pledgeForm.setParentLocId(null);
			pledgeForm.getLocationByLayers().clear();
			pledgeForm.getSelectedLayers().clear();
		}
		String cIso= FeaturesUtil.getDefaultCountryIso();
		
		Long parentLocId			= pledgeForm.getParentLocId();
		
		Map<Integer, Collection<KeyValue>> locationByLayers		= 	pledgeForm.getLocationByLayers();
		
		AmpCategoryValue implLevel              = CategoryManagerUtil.getAmpCategoryValueFromDb( pledgeForm.getLevelId() );
		if ( implLevel!=null &&
				CategoryManagerUtil.equalsCategoryValue(implLevel, CategoryConstants.IMPLEMENTATION_LEVEL_INTERNATIONAL) &&
				CategoryManagerUtil.equalsCategoryValue(implLocValue, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY) )  {
			Collection<AmpCategoryValueLocations> countries =
				DynLocationManagerUtil.getLocationsByLayer(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
			if ( countries != null ) {
				Integer countryIndex    = null;
				ArrayList<KeyValue> countriesKV                         = new ArrayList<KeyValue>();
				for (AmpCategoryValueLocations country: countries) {
					countryIndex    = (countryIndex==null)?country.getParentCategoryValue().getIndex():countryIndex;
					KeyValue countryKV                                              = new KeyValue(country.getId().toString() , country.getName() );
					countriesKV.add(countryKV);
				}
				locationByLayers.put(countryIndex, countriesKV);
			}
			return mapping.findForward("forward");
		}
	
		
        if(cIso!=null && parentLocId == null){ // Setting up the country
        	pledgeForm.setDefaultCountryIsSet(true);
        	AmpCategoryValueLocations defCountry		= DynLocationManagerUtil.getLocationByIso(cIso, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
        	Integer countryLayerIndex						= defCountry.getParentCategoryValue().getIndex();
        	KeyValue countryKV							= new KeyValue(defCountry.getId().toString() , defCountry.getName() );
        	ArrayList<KeyValue> countries				= new ArrayList<KeyValue>();
        	countries.add(countryKV);
        	locationByLayers.put(countryLayerIndex, countries);
        	if ( countryLayerIndex < implLocValue.getIndex() ) {
        		parentLocId			= defCountry.getId();
        	}
        }
        if ( parentLocId == 0 ) {
        	AmpCategoryValueLocations defCountry		= DynLocationManagerUtil.getLocationByIso(cIso, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
        	parentLocId			= defCountry.getId();
    	}
        int lastLayer=0;
        while ( parentLocId != null ) {
        	AmpCategoryValueLocations parentLoc					= DynLocationManagerUtil.getLocation(parentLocId, true);
        	pledgeForm.getSelectedLayers().put(parentLoc.getParentCategoryValue().getIndex(), parentLoc.getId() );
        	
        	Iterator<Entry<Integer, Collection<KeyValue>>> entryIter		= locationByLayers.entrySet().iterator();
        	while ( entryIter.hasNext() ) {
        		/**
        		 * In case the user changes a higher layer location all layers below need to be cleared.
        		 */
        		if ( entryIter.next().getKey() > parentLoc.getParentCategoryValue().getIndex() ) {
        			entryIter.remove();
        		}
        	}
        	
        	parentLocId											= null;
        	Collection<AmpCategoryValueLocations> childrenLocs 	= parentLoc.getChildLocations();
        	if ( childrenLocs != null && childrenLocs.size() > 0 ) {
        		Integer currentLayer = (parentLoc.getParentCategoryValue().getIndex()) + 1;
        			//((AmpCategoryValueLocations)childrenLocs.toArray()[0]).getParentCategoryValue().getIndex();
        		
        		if ( currentLayer <= implLocValue.getIndex() ) { 
	        		ArrayList<KeyValue> childrenKV					= new ArrayList<KeyValue>(childrenLocs.size());
	        		for ( AmpCategoryValueLocations child : childrenLocs ) {
	        			boolean addLocationAllowed		= true;
	        			/**
	        			 * If we are on the last layer that needs to be shown, we have to 
	        			 * hide locations that are already selected
	        			 */
	        			if ( currentLayer == implLocValue.getIndex() && pledgeForm.getSelectedLocs() != null ) {
	        				for ( FundingPledgesLocation l: pledgeForm.getSelectedLocs() ) {
	        					if ( child.equals(l.getLocation()) ) {
	        						addLocationAllowed	= false;
	        						break;
	        					}
	        				}
	        			}
	        			if ( addLocationAllowed )
	        				childrenKV.add( new KeyValue(child.getId().toString(), child.getName() ) );
	        		}
	        		lastLayer=currentLayer;
	        		locationByLayers.put(currentLayer, childrenKV);
	        		
	        		if ( childrenLocs.size() == 1 ) {
	        			AmpCategoryValueLocations newParent	= (AmpCategoryValueLocations)childrenLocs.toArray()[0];
	        			parentLocId							= newParent.getId();
	        		}
        		}
     
        	} else{
        		if (lastLayer < implLocValue.getIndex()){
        			pledgeForm.setNoMoreRecords(true);
        		}
        	}
        	
        }
        pledgeForm.setImplemLocationLevel(null);
        return mapping.findForward("forward");
	}
}
