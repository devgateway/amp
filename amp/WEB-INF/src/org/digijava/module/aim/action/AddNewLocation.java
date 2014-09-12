package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.dbentity.AmpLocationIndicatorValue;
import org.digijava.module.aim.exception.dynlocation.DuplicateLocationCodeException;
import org.digijava.module.aim.form.NewAddLocationForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.GPISetupUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 *
 * @author medea
 */
public class AddNewLocation extends Action {

	private static final Logger logger = Logger.getLogger(AddNewLocation.class);
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
                Collection <AmpLocationIndicatorValue> indicatorValues = getIndicatorValuesListForCategory (location.getParentCategoryValue().getId(),location);
                addRegForm.setLocationIndicatorValues (indicatorValues);
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
            } 
            if (addRegForm.getEvent().equals("saveLocationValues")) {
            	
            	Enumeration<String> params = request.getParameterNames();
        		while (params.hasMoreElements()) {
        			String paramName = params.nextElement().toString();
        			if (paramName.startsWith("indicator_")) {
        				String indId = paramName.substring(10);
        				Collection <AmpLocationIndicatorValue> values = addRegForm.getLocationIndicatorValues ();
        				for (AmpLocationIndicatorValue value :values) {
        					if (value.getIndicator().getId().equals(new Long (indId)) && 
        						value.getLocation().getId().equals(addRegForm.getEditedId())) {
        						value.setValue(new Double(request.getParameter(paramName)));
        						DbUtil.saveOrUpdateObject(value);
        						break;
        					}
        				}
        			}
        		}
        		
            	addRegForm.setEvent(null);
            	return mapping.findForward("addEdit");
            }
         
            else {
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
                	
                	if (CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.equalsCategoryValue(location.getParentCategoryValue())) {
                		DynLocationManagerUtil.synchronizeCountries();
                	}
                }
                catch (DuplicateLocationCodeException e) {
                	errors.add("title" ,  
                			new ActionMessage("error.aim.addLocation.duplicateCode", TranslatorWorker.translateText("Duplicate") + " " +  TranslatorWorker.translateText(e.getCodeType())) 
                	);
					this.saveErrors(request, errors);
					return mapping.findForward("addEdit");
				}

                return mapping.findForward("added");

            }

        }

    }
    
    private Collection <AmpLocationIndicatorValue> getIndicatorValuesListForCategory (Long id,AmpCategoryValueLocations location) {
    	 List <AmpIndicatorLayer> indicatorLayerList = DynLocationManagerUtil.getIndicatorByCategoryValueId (location.getParentCategoryValue().getId());
    	 List <AmpLocationIndicatorValue> values = DynLocationManagerUtil.getLocationIndicatorValueByLocation (location);
    	 Map <Long,AmpLocationIndicatorValue> indicatorValueMap = new HashMap<Long,AmpLocationIndicatorValue>(); 
    	 for (AmpIndicatorLayer indicator:indicatorLayerList) {
         	AmpLocationIndicatorValue value = new AmpLocationIndicatorValue();
         	value.setLocation(location);
         	value.setIndicator(indicator);
         	indicatorValueMap.put(indicator.getId(),value);
         }
    	 for (AmpLocationIndicatorValue value: values) {
    		 indicatorValueMap.put(value.getIndicator().getId(), value);
    	 }
    	 return indicatorValueMap.values();
    }
}
