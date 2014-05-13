package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.form.DynLocationManagerForm;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class DynamicLocationManager extends MultiAction {
	private static Logger logger	= Logger.getLogger(DynamicLocationManager.class);
	
	@Override
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors				= new ActionMessages();
		request.setAttribute("myErrors", errors);
		DynLocationManagerForm myForm	= (DynLocationManagerForm) form;
		
		String hideEmptyCountriesStr				= request.getParameter("hideEmptyCountriesAction");
		if ( "false".equals(hideEmptyCountriesStr) ) {
			myForm.setHideEmptyCountries(false);
		}
		
		AmpCategoryClass implLocClass	= CategoryManagerUtil.loadAmpCategoryClassByKey( CategoryConstants.IMPLEMENTATION_LOCATION_KEY );
		myForm.setImplementationLocation(implLocClass);
		
		return modeSelect(mapping, form, request, response);
	}

	@Override
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynLocationManagerForm myForm	= (DynLocationManagerForm) form;
		if ( myForm.getTreeStructure() != null || myForm.getDeleteLocationId() != null)
			modeProcess(mapping, myForm, request, response);
		return modeShow(mapping, myForm, request, response);
	}

	public ActionForward modeShow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors				= (ActionMessages) request.getAttribute("myErrors");
		DynLocationManagerForm myForm	= (DynLocationManagerForm) form;
		
		String errorString		= CategoryManagerUtil.checkImplementationLocationCategory();
		if ( errorString != null ) {
			ActionMessage error	= (ActionMessage) new ActionMessage("error.aim.categoryManager.implLocProblem", errorString);
			errors.add("title", error);
			myForm.setImportantErrorAppeared(true);
		}
		else {
		
			
			AmpCategoryClass implLocClass	= myForm.getImplementationLocation();
			
			Collection<AmpCategoryValueLocations> rootLocations	 = null;
			if ( implLocClass == null || implLocClass.getPossibleValues() == null || implLocClass.getPossibleValues().size() == 0 ) {
				errors.add("title", new ActionMessage("error.aim.dynRegionManager.implLocationCategMissing" ) );
			}
			
			rootLocations						= DynLocationManagerUtil.getHighestLayerLocations(implLocClass, myForm, errors);
//			myForm.setFirstLevelLocations(rootLocations);
			myForm.setFirstLevelLocations(
					this.correctStructure(rootLocations, implLocClass.getPossibleValues(), 0, myForm.getHideEmptyCountries() )
			);
			myForm.setNumOfLayers( implLocClass.getPossibleValues().size() );
			myForm.setFirstLayerId( implLocClass.getPossibleValues().get(0).getId() );
		}
		this.saveErrors(request, errors);
		return mapping.findForward("forward");
	}
	
	private void insertParentForSpecifiedLocs( Collection<AmpCategoryValueLocations> children, AmpCategoryValueLocations newParent ) {
		AmpCategoryValueLocations oldParent			= ((AmpCategoryValueLocations)children.toArray()[0]).getParentLocation();
		
		/* The new parents point of view */
		newParent.setParentLocation( oldParent );
		newParent.getChildLocations().addAll(children);
		
		/* The old parents point of view */
		if ( oldParent != null ) {
			oldParent.getChildLocations().removeAll(children);
			oldParent.getChildLocations().add(newParent);
		}
		
		/* The children point of view */
		Iterator<AmpCategoryValueLocations> iter	= children.iterator();
		while ( iter.hasNext() ) {
			iter.next().setParentLocation(newParent);
		}
	}
	private Collection<AmpCategoryValueLocations> correctStructure( Collection<AmpCategoryValueLocations> sameParentLocations, 
							List<AmpCategoryValue> implLocValues, int layer, boolean hideEmptyCountries) {
		if ( sameParentLocations == null || sameParentLocations.size() == 0 )
			return null;
		int largestLayerIndex											= 0;
		int countryLayerIndex											= 0;
		try {
			AmpCategoryValue countryLayer = CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getAmpCategoryValueFromDB();
			countryLayerIndex											= countryLayer.getIndex();
		} catch (Exception e) {
			e.printStackTrace();
		}
		HashMap<Integer, Set<AmpCategoryValueLocations>> layerToLocMap	= new HashMap<Integer, Set<AmpCategoryValueLocations>>();
		
		Collection<AmpCategoryValueLocations> myLocations				= sameParentLocations;
		Iterator<AmpCategoryValueLocations> iter						= sameParentLocations.iterator();

		while ( iter.hasNext() ) {
			AmpCategoryValueLocations loc	= iter.next();
			int currentLayer				= loc.getParentCategoryValue().getIndex();
			largestLayerIndex				= (currentLayer>largestLayerIndex)? currentLayer:largestLayerIndex;
			
			Set<AmpCategoryValueLocations> layerSet		= layerToLocMap.get(currentLayer);
			if ( layerSet == null ) {
				layerSet					= new TreeSet<AmpCategoryValueLocations>(DynLocationManagerUtil.alphabeticalLocComp);
				layerToLocMap.put(currentLayer, layerSet);
			}
			if ( hideEmptyCountries && currentLayer == countryLayerIndex && loc.getChildLocations().size() == 0 )
				continue;
			layerSet.add(loc);
		}
		for ( int i=largestLayerIndex; i > layer; i--){
			Set<AmpCategoryValueLocations> layerSet		= layerToLocMap.get(i);
			if ( layerSet != null ) {
				AmpCategoryValueLocations newLoc		= new AmpCategoryValueLocations();
				newLoc.setId(-1L);
				newLoc.setName( "Unspecified" );
				newLoc.setParentCategoryValue( implLocValues.get(i-1) );
				newLoc.setChildLocations( new TreeSet<AmpCategoryValueLocations>(DynLocationManagerUtil.alphabeticalLocComp) );
				Set<AmpCategoryValueLocations> nextLayerSet		= layerToLocMap.get(i-1);
				if ( nextLayerSet == null ) {
					nextLayerSet					= new TreeSet<AmpCategoryValueLocations>(DynLocationManagerUtil.alphabeticalLocComp);
					layerToLocMap.put(i-1, nextLayerSet);
				}
				nextLayerSet.add(newLoc);
				insertParentForSpecifiedLocs(layerSet, newLoc);
			}
		}
		
		
		iter								= sameParentLocations.iterator();
		while ( iter.hasNext() ) {
			AmpCategoryValueLocations loc	= iter.next();
			if ( loc.getChildLocations() != null && loc.getChildLocations().size() > 0 ) {
				correctStructure(loc.getChildLocations(), implLocValues, loc.getParentCategoryValue().getIndex()+1, hideEmptyCountries);
			}
		}
		
		return layerToLocMap.get(0);

	/*	HashMap<Integer, AmpCategoryValueLocations> unspecifiedLocs		= new HashMap<Integer, AmpCategoryValueLocations>();
		while ( iter.hasNext() ) {
			AmpCategoryValueLocations loc	= iter.next();
			int currentLayer				= loc.getParentCategoryValue().getIndex();
			if ( currentLayer > layer ) {
				for ( int i=1; i<=currentLayer-layer ; i++) { // for each dummy location we need to add
					AmpCategoryValueLocations newLoc		= unspecifiedLocs.get( currentLayer-i );
					if ( newLoc == null ) {
						newLoc										= new AmpCategoryValueLocations();
						newLoc.setId(-1L);
						newLoc.setName( "Unspecified" );
						newLoc.setParentCategoryValue( implLocValues.get(currentLayer-i) );
						newLoc.setChildLocations( new TreeSet<AmpCategoryValueLocations>(DynLocationManagerUtil.alphabeticalLocComp) );
						
						newLoc.setParentLocation( loc.getParentLocation() );
						if ( newLoc.getParentLocation() != null )
							newLoc.getParentLocation().getChildLocations().add(newLoc);
						
						unspecifiedLocs.put(currentLayer-i, newLoc);
						
						AmpCategoryValueLocations lowerLayerLoc		= unspecifiedLocs.get(currentLayer-i-1);
						if ( lowerLayerLoc != null ) {
							lowerLayerLoc.setParentLocation(newLoc);
							newLoc.getChildLocations().add(lowerLayerLoc);
						}
						
					}
				}
				AmpCategoryValueLocations parentLoc			= loc.getParentLocation();
				AmpCategoryValueLocations newParentLoc		= unspecifiedLocs.get(currentLayer-1);
				newParentLoc.getChildLocations().add(loc);
				loc.setParentLocation(newParentLoc);
				if ( parentLoc != null ) {
					parentLoc.getChildLocations().remove(loc);
					myLocations					= parentLoc.getChildLocations();
				}
				else {
					myLocations					= new TreeSet<AmpCategoryValueLocations>(DynLocationManagerUtil.alphabeticalLocComp);
//					myLocations.add(newLoc);
				}
				
			}
		}
		
		iter	= rootLocations.iterator();
		while ( iter.hasNext() ) {
			AmpCategoryValueLocations loc		= iter.next();
			correctStructure(loc.getChildLocations(), implLocValues, loc.getParentCategoryValue().getIndex()+1);
		}
		return myLocations;*/
	}

	public ActionForward modeProcess(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		DynLocationManagerForm myForm	= (DynLocationManagerForm) form;
		ActionMessages errors				= (ActionMessages) request.getAttribute("myErrors");
		
		DynLocationManagerUtil.clearRegionsOfDefaultCountryCache();
		
		if ( myForm.getTreeStructure() != null && myForm.getTreeStructure().length() > 0)
			DynLocationManagerUtil.saveStructure(myForm.getTreeStructure(), myForm.getUnorgLocations(), myForm.getImplementationLocation(), errors);
		else if ( myForm.getDeleteLocationId() != null  && myForm.getDeleteLocationId() > 0)
			DynLocationManagerUtil.deleteLocation(myForm.getDeleteLocationId(), errors);
		
		return null;
	}
}
