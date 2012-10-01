package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.exception.dynlocation.DuplicateLocationCodeException;
import org.digijava.module.aim.form.NewAddLocationForm;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 *
 * @author medea
 */
public class AddNewLocation extends Action {

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        
    	ActionMessages errors		= new ActionMessages();
    	
        NewAddLocationForm addRegForm = (NewAddLocationForm) form;
        if (addRegForm.getEvent().equals("add")) {
            addRegForm.setCode(null);
            addRegForm.setGeoCode(null);
            addRegForm.setName(null);
            addRegForm.setIso(null);
            addRegForm.setIso3(null);
            addRegForm.setDescription(null);
            addRegForm.setGsLat(null);
            addRegForm.setGsLong(null);
            addRegForm.setEditedId(null);
            return mapping.findForward("addEdit");
        } else {
            if (addRegForm.getEvent().equals("edit")) {
                AmpCategoryValueLocations location = LocationUtil.getAmpCategoryValueLocationById(addRegForm.getEditedId());
                addRegForm.setCode(location.getCode());
                addRegForm.setGeoCode(location.getGeoCode());
                addRegForm.setName(location.getName());
                addRegForm.setIso(location.getIso());
                addRegForm.setIso3(location.getIso3());
                addRegForm.setDescription(location.getDescription());
                addRegForm.setGsLat(location.getGsLat());
                addRegForm.setGsLong(location.getGsLong());
                addRegForm.setParentCatValId(location.getParentCategoryValue().getId());
                return mapping.findForward("addEdit");
            } else {
                AmpCategoryValueLocations location = null;
                if (addRegForm.getEditedId() == null) {
                    location = new AmpCategoryValueLocations();
                    AmpCategoryValueLocations parentLoc=null;
					if(addRegForm.getParentLocationId()!=-1) parentLoc = DynLocationManagerUtil.getLocation(addRegForm.getParentLocationId(), true);
                    AmpCategoryValue parentCatVal = CategoryManagerUtil.getAmpCategoryValueFromDb(addRegForm.getParentCatValId());
                    location.setParentLocation(parentLoc);
                    location.setParentCategoryValue(parentCatVal);
                } else {
                    location = LocationUtil.getAmpCategoryValueLocationById(addRegForm.getEditedId());
                }
                location.setCode(addRegForm.getCode());
                location.setGeoCode(addRegForm.getGeoCode());
                location.setName(addRegForm.getName());
                location.setIso(addRegForm.getIso());
                location.setIso3(addRegForm.getIso3());
                location.setDescription(addRegForm.getDescription());
                location.setGsLat(addRegForm.getGsLat());
                location.setGsLong(addRegForm.getGsLong());
                
                try {
                	
                	if (addRegForm.getEditedId() != null)
                		LocationUtil.saveLocation(location, true);
                	else
                		LocationUtil.saveLocation(location, false);
                	
                	if ( CategoryManagerUtil.equalsCategoryValue(location.getParentCategoryValue(), 
                					CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY) ) {
                		DynLocationManagerUtil.synchronizeCountries();
                	}
                }
                catch (DuplicateLocationCodeException e) {
                	errors.add("title" ,  
                			new ActionMessage("error.aim.addLocation.duplicateCode", TranslatorWorker.translateText("Duplicate", request) + " " +  TranslatorWorker.translateText(e.getCodeType(), request)) 
                	);
					this.saveErrors(request, errors);
					return mapping.findForward("addEdit");
				}

                return mapping.findForward("added");

            }

        }

    }
}
