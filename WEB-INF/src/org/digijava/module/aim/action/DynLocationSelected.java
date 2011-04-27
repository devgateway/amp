/**
 * 
 */
package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil.HelperLocationAncestorLocationNamesAsc;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * @author Alex Gartner
 *
 */
public class DynLocationSelected extends Action {
	private static Logger logger = Logger.getLogger(DynLocationSelected.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws Exception {
		
		EditActivityForm eaForm 	= (EditActivityForm) form;
		
	    AmpCategoryValue implLocValue	= CategoryManagerUtil.getAmpCategoryValueFromDb( eaForm.getLocation().getImplemLocationLevel() );
		AmpCategoryValue implLevel		= CategoryManagerUtil.getAmpCategoryValueFromDb( eaForm.getLocation().getLevelId() );
		String cIso						= FeaturesUtil.getDefaultCountryIso();
		AmpCategoryValueLocations defCountry	= DynLocationManagerUtil.getLocationByIso(cIso, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
		boolean defCountryInSelection   = false;
		ArrayList<AmpCategoryValueLocations>    userSelectedLocsColl = new ArrayList<AmpCategoryValueLocations>();
		boolean setFullPercForDefaultCountry    					= false;
		
		String gsAllowPercentages	=  FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ALLOW_PERCENTAGES_FOR_ALL_COUNTRIES );
		if ( !"true".equals( gsAllowPercentages ) &&
				implLevel!=null && implLocValue!=null &&
				CategoryManagerUtil.equalsCategoryValue(implLevel, CategoryConstants.IMPLEMENTATION_LEVEL_INTERNATIONAL) &&
				CategoryManagerUtil.equalsCategoryValue(implLocValue, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY)
		) {
			setFullPercForDefaultCountry            = true;
		}
		
		
		/**
		 * TODO NEEDS REFACTORING ! CURRENTLY WORKED ONLY FOR ACTIVITY FORM.SHOULD BE RE-WRITTEN LIKE IT'S IN 2.0, WITH IT'S FORM E.T.C.
		 */
		if(request.getParameter("userSelectedLocs")!=null){
			String[] selectedLocIds=request.getParameter("userSelectedLocs").split(",");
			Long [] userSelLocsId = new Long[selectedLocIds.length];
			for (int i = 0; i < selectedLocIds.length; i++) {
				userSelLocsId[i]=new Long(selectedLocIds[i]);
			}
			eaForm.getLocation().setUserSelectedLocs(userSelLocsId);
		}
		Long [] userSelectedLocs	= eaForm.getLocation().getUserSelectedLocs(); 
		if ( userSelectedLocs != null ) {
			for (int i=0; i<userSelectedLocs.length; i++) {
				AmpCategoryValueLocations ampCVLocation		= DynLocationManagerUtil.getLocation( userSelectedLocs[i], false);
				userSelectedLocsColl.add(ampCVLocation);
				if ( ampCVLocation.getId().longValue()==defCountry.getId().longValue() )
					defCountryInSelection   = true;
			}
			
			if ( !defCountryInSelection && setFullPercForDefaultCountry ) {
				userSelectedLocsColl.add(defCountry);
			}
			for (AmpCategoryValueLocations ampCVLocation: userSelectedLocsColl ) {
				
				Location location							= new Location();
				location.setAmpCVLocation(ampCVLocation);
				location.setAncestorLocationNames( DynLocationManagerUtil.getParents(ampCVLocation) );
				location.setLocationName(ampCVLocation.getName());
				location.setLocId( ampCVLocation.getId() );
				location.setPercent("0");
				
				if ( setFullPercForDefaultCountry ) {
					if ( CategoryManagerUtil.equalsCategoryValue(ampCVLocation.getParentCategoryValue(),
							CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY) ) {
						
						if ( ampCVLocation.getId().longValue() == defCountry.getId().longValue() ) {
							location.setPercent("100");
						}
						else
							location.setPercentageBlocked(true);

					}
				}
				
				if ( eaForm.getLocation().getSelectedLocs() == null ){
                                    List<Location> locs = new ArrayList<Location>();
                                    eaForm.getLocation().setSelectedLocs(locs);
				}
				if ( !eaForm.getLocation().getSelectedLocs().contains(location) ){
					if (eaForm.getLocation().getSelectedLocs().size()==0 && !setFullPercForDefaultCountry ) {
						location.setPercent("100");
					}
					eaForm.getLocation().getSelectedLocs().add(location);
				}
				AmpCategoryValueLocations ampRegion			= DynLocationManagerUtil.getAncestorByLayer(ampCVLocation, 
																CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
				if ( ampRegion != null ){
					if ( eaForm.getFunding().getFundingRegions() == null )
						eaForm.getFunding().setFundingRegions( new ArrayList<AmpCategoryValueLocations>() );
					eaForm.getFunding().getFundingRegions().add(ampRegion);
				}
			}
                      if (eaForm.getLocation().getSelectedLocs() != null) {
                        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
                        List<Location> locs = new ArrayList<Location>();
                        locs.addAll(eaForm.getLocation().getSelectedLocs());
                        Collections.sort(locs, new HelperLocationAncestorLocationNamesAsc(langCode));
                        eaForm.getLocation().setSelectedLocs(locs);
                    }
		}
		
		if(request.getParameter("forwardForOrg")!=null){
			request.getSession().setAttribute("locations", eaForm.getLocation().getSelectedLocs());
			return mapping.findForward("forwardForOrg");
		}else{
			return mapping.findForward("forward");
		}
	}
}
