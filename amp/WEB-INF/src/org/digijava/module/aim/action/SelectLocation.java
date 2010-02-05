package org.digijava.module.aim.action;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.form.AddOrgForm;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.SelectLocationForm;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class SelectLocation extends SelectorAction{
	private static Logger logger = Logger.getLogger(SelectLocation.class);

	public ActionForward selectorEnd(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws Exception {

		SelectLocationForm 		selectTestForm 	= (SelectLocationForm) form;
		EditActivityForm 	eaForm 	= (EditActivityForm) getForm(request,"aimEditActivityForm");
                AddOrgForm	editOrgForm 	= (AddOrgForm) getForm(request,"aimAddOrgForm");
                
        AmpCategoryValue implLocValue	= CategoryManagerUtil.getAmpCategoryValueFromDb( selectTestForm.getImplemLocationLevel() );
        AmpCategoryValue implLevel		= CategoryManagerUtil.getAmpCategoryValueFromDb( selectTestForm.getImplemLevel() );
        String cIso						= FeaturesUtil.getDefaultCountryIso();
        AmpCategoryValueLocations defCountry		= DynLocationManagerUtil.getLocationByIso(cIso, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);

		Long [] userSelectedLocs		= selectTestForm.getUserSelectedLocs();
		boolean defCountryInSelection	= false;
		ArrayList<AmpCategoryValueLocations>	userSelectedLocsColl = new ArrayList<AmpCategoryValueLocations>();
		if ( userSelectedLocs != null ) {
			for (int i=0; i<userSelectedLocs.length; i++) {
				AmpCategoryValueLocations ampCVLocation		= DynLocationManagerUtil.getLocation( userSelectedLocs[i], false);
				userSelectedLocsColl.add(ampCVLocation);
				if ( ampCVLocation.getId().longValue()==defCountry.getId().longValue() ) 
					defCountryInSelection	= true;
			}
			if ( !defCountryInSelection && implLevel!=null && implLocValue!=null &&
					CategoryManagerUtil.equalsCategoryValue(implLevel, CategoryConstants.IMPLEMENTATION_LEVEL_INTERNATIONAL) && 
					CategoryManagerUtil.equalsCategoryValue(implLocValue, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY) ) {
				userSelectedLocsColl.add(defCountry);
			}
			for (AmpCategoryValueLocations ampCVLocation: userSelectedLocsColl ) {
				Location location							= new Location();
				location.setAmpCVLocation(ampCVLocation);
				location.setAncestorLocationNames( DynLocationManagerUtil.getParents(ampCVLocation) );
				location.setLocationName(ampCVLocation.getName());
				location.setLocId( ampCVLocation.getId() );
				location.setPercent(0);
				
				if ( implLevel!=null && implLocValue!=null &&
						CategoryManagerUtil.equalsCategoryValue(implLevel, CategoryConstants.IMPLEMENTATION_LEVEL_INTERNATIONAL) && 
						CategoryManagerUtil.equalsCategoryValue(implLocValue, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY) ) {
					if ( CategoryManagerUtil.equalsCategoryValue(ampCVLocation.getParentCategoryValue(), 
							CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY) ) {
						location.setPercentageBlocked(true);
						if ( ampCVLocation.getId().longValue() == defCountry.getId().longValue() )
							location.setPercent(100);
					}
				}
				
				
				if (eaForm != null) {
					if (eaForm.getLocation().getSelectedLocs() == null) {
						eaForm.getLocation().setSelectedLocs(new ArrayList<Location>());
					}
					if (!eaForm.getLocation().getSelectedLocs().contains(location)) {
						eaForm.getLocation().getSelectedLocs().add(location);
					}
					AmpCategoryValueLocations ampRegion			= DynLocationManagerUtil.getAncestorByLayer(ampCVLocation,
							CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
					if ( ampRegion != null ){
						if ( eaForm.getFunding().getFundingRegions() == null )
							eaForm.getFunding().setFundingRegions( new ArrayList<AmpCategoryValueLocations>() );
						eaForm.getFunding().getFundingRegions().add(ampRegion);
					}
				} else {
					if (editOrgForm != null) {
						if (editOrgForm.getSelectedLocs() == null) {
							editOrgForm.setSelectedLocs(new ArrayList<Location>());
						}
						if (!editOrgForm.getSelectedLocs().contains(location)) {
							editOrgForm.getSelectedLocs().add(location);
						}
					}
				}


			}
		}

		return null;
	}
}
