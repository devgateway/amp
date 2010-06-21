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

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		PledgeForm plForm = (PledgeForm) form;
		plForm.setNoMoreRecords(false);
		
		Integer impLevelValue;
		AmpCategoryValue implLocValue	= CategoryManagerUtil.getAmpCategoryValueFromDB( CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
		if ( implLocValue == null )
			implLocValue				= CategoryManagerUtil.getAmpCategoryValueFromDB( CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY );
		if (implLocValue != null) {
			impLevelValue	= new Integer ( implLocValue.getIndex() + 1 );
		}
		else
			impLevelValue 	= new Integer( 1 );

		plForm.setImpLevelValue( impLevelValue );
		
		/* New Region Manager changes */
		if ( !"true".equals( request.getParameter("edit") ) ) {
			plForm.setParentLocId(null);
			plForm.getLocationByLayers().clear();
			plForm.getSelectedLayers().clear();
		}
		String cIso= FeaturesUtil.getDefaultCountryIso();
		Long parentLocId			= plForm.getParentLocId();
		
		Map<Integer, Collection<KeyValue>> locationByLayers		= 
								plForm.getLocationByLayers();
		
        if(cIso!=null && parentLocId == null){ // Setting up the country
        	plForm.setDefaultCountryIsSet(true);
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
        
        while ( parentLocId != null ) {
        	AmpCategoryValueLocations parentLoc					= DynLocationManagerUtil.getLocation(parentLocId, true);
        	plForm.getSelectedLayers().put(parentLoc.getParentCategoryValue().getIndex(), parentLoc.getId() );
        	
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
        	int lastLayer=0;
        	if ( childrenLocs != null && childrenLocs.size() > 0 ) {
        		Integer currentLayer							= 
        			((AmpCategoryValueLocations)childrenLocs.toArray()[0]).getParentCategoryValue().getIndex();
        		
        		if ( currentLayer <= implLocValue.getIndex() ) { 
	        		ArrayList<KeyValue> childrenKV					= new ArrayList<KeyValue>(childrenLocs.size());
	        		for ( AmpCategoryValueLocations child : childrenLocs ) {
	        			boolean addLocationAllowed		= true;
	        			/**
	        			 * If we are on the last layer that needs to be shown, we have to 
	        			 * hide locations that are already selected
	        			 */
	        			if ( currentLayer == implLocValue.getIndex() && plForm.getSelectedLocs() != null ) {
	        				for ( FundingPledgesLocation l: plForm.getSelectedLocs() ) {
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
        			plForm.setNoMoreRecords(true);
        		}
        	}
        	
        }
		return mapping.findForward("forward");
	}
}
