/**
 * 
 */
package org.digijava.module.aim.action;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;

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
				Location location							= new Location();
				location.setAmpCVLocation(ampCVLocation);
				location.setAncestorLocationNames( DynLocationManagerUtil.getParents(ampCVLocation) );
				location.setLocationName(ampCVLocation.getName());
				location.setLocId( ampCVLocation.getId() );
				location.setPercent("");
				
				if ( eaForm.getLocation().getSelectedLocs() == null ){
					eaForm.getLocation().setSelectedLocs( new ArrayList<Location>() );
				}
				if ( !eaForm.getLocation().getSelectedLocs().contains(location) ){
					if (eaForm.getLocation().getSelectedLocs().size()==0) {
						location.setPercent("100");
					} else {
						location.setPercent("0");
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
		}
		
		if(request.getParameter("forwardForOrg")!=null){
			request.getSession().setAttribute("locations", eaForm.getLocation().getSelectedLocs());
			return mapping.findForward("forwardForOrg");
		}else{
			return mapping.findForward("forward");
		}
	}
}
