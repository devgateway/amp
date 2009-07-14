package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.SelectLocationForm;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class SelectLocation extends SelectorAction{
	
	
	private static Logger logger = Logger.getLogger(SelectLocation.class);

	public ActionForward startSelect(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws Exception {

		SelectLocationForm location = (SelectLocationForm) form;

		
		if (!location.isReseted()) {
			location.setReseted(false);
			logger.info("calling reset");
			location.reset(mapping, request);
		}


		Integer impLevelValue;
		AmpCategoryValue implLocValue	= CategoryManagerUtil.getAmpCategoryValueFromDb( location.getImplemLocationLevel() );
		if ( implLocValue == null )
			implLocValue				= CategoryManagerUtil.getAmpCategoryValueFromDB( CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY );
		if (implLocValue != null) {
			impLevelValue	= new Integer ( implLocValue.getIndex() + 1 );
		}
		else
			impLevelValue 	= new Integer( 1 );

		location.setImpLevelValue( impLevelValue );
		
		/* New Region Manager changes */
		if ( !"true".equals( request.getParameter("edit") ) ) {
			location.setParentLocId(null);
			location.getLocationByLayers().clear();
			location.getSelectedLayers().clear();
		}
		String cIso= FeaturesUtil.getDefaultCountryIso();
		Long parentLocId			= location.getParentLocId();
		
		Map<Integer, Collection<KeyValue>> locationByLayers		=  location.getLocationByLayers();
		
        if(cIso!=null && parentLocId == null){ // Setting up the country
        	location.setDefaultCountryIsSet(true);
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
        	location.getSelectedLayers().put(parentLoc.getParentCategoryValue().getIndex(), parentLoc.getId() );
        	
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
	        			if ( currentLayer == implLocValue.getIndex() && location.getSelectedLocs() != null ) {
	        				for ( org.digijava.module.aim.helper.Location l: location.getSelectedLocs() ) {
	        					if ( child.equals(l.getAmpCVLocation()) ) {
	        						addLocationAllowed	= false;
	        						break;
	        					}
	        				}
	        			}
	        			if ( addLocationAllowed )
	        				childrenKV.add( new KeyValue(child.getId().toString(), child.getName() ) );
	        		}
	        		
	        		locationByLayers.put(currentLayer, childrenKV);
	        		
	        		if ( childrenLocs.size() == 1 ) {
	        			AmpCategoryValueLocations newParent	= (AmpCategoryValueLocations)childrenLocs.toArray()[0];
	        			parentLocId							= newParent.getId();
	        		}
        		}
     
        	}
        }

		return mapping.findForward("forward");
	}
	
	public ActionForward endSelect(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws Exception {
		
		SelectLocationForm 		selectTestForm 	= (SelectLocationForm) form;
		EditActivityForm 	eaForm 	= (EditActivityForm) getForm(request,"aimEditActivityForm");
		
		Long [] userSelectedLocs	= selectTestForm.getUserSelectedLocs(); 
		if ( userSelectedLocs != null ) {
			for (int i=0; i<userSelectedLocs.length; i++) {
				AmpCategoryValueLocations ampCVLocation		= DynLocationManagerUtil.getLocation( userSelectedLocs[i], false);
				Location location							= new Location();
				location.setAmpCVLocation(ampCVLocation);
				location.setAncestorLocationNames( DynLocationManagerUtil.getParents(ampCVLocation) );
				location.setLocationName(ampCVLocation.getName());
				location.setLocId( ampCVLocation.getId() );
				location.setPercent(""); 
				
				if ( eaForm.getLocation().getSelectedLocs() == null )
					eaForm.getLocation().setSelectedLocs( new ArrayList<Location>() );
				if ( !eaForm.getLocation().getSelectedLocs().contains(location) )
					eaForm.getLocation().getSelectedLocs().add(location);
				

				AmpCategoryValueLocations ampRegion			= DynLocationManagerUtil.getAncestorByLayer(ampCVLocation, 
																CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
				if ( ampRegion != null ){
					if ( eaForm.getFunding().getFundingRegions() == null )
						eaForm.getFunding().setFundingRegions( new ArrayList<AmpCategoryValueLocations>() );
					eaForm.getFunding().getFundingRegions().add(ampRegion);
				}

			}
		}
		
		return null;
	}	
}
