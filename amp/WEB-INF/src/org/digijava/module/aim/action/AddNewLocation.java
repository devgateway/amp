package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.dbentity.AmpLocationIndicatorValue;
import org.digijava.module.aim.exception.dynlocation.DuplicateLocationCodeException;
import org.digijava.module.aim.form.NewAddLocationForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
        
        ActionMessages errors = new ActionMessages();
        
        NewAddLocationForm addRegForm = (NewAddLocationForm) form;

        String event = addRegForm.getEvent();
        if (event == null) {
            event = "default";
        }

        switch(event) {
            case "add" :
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
            case "edit" :
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
            case "saveLocationValues" :
                Enumeration<String> params = request.getParameterNames();
                while (params.hasMoreElements()) {
                    String paramName = params.nextElement();

                    String paramValueString = request.getParameter(paramName);


                    if (paramName.startsWith("indicator_")) {
                        String indId = paramName.substring(10);
                        Collection <AmpLocationIndicatorValue> values = addRegForm.getLocationIndicatorValues ();
                        for (AmpLocationIndicatorValue value : values) {
                            if (value.getIndicator().getId() == Long.parseLong(indId) &&
                                    value.getLocation().getId().equals(addRegForm.getEditedId())) {
                                // empty values should also be accepted
                                if (paramValueString == null || "".equals(paramValueString)) {
                                    value.setValue(null);
                                } else {

                                    // Ugly solution, but the requirement is to accept both values: 12.56 and 12,56
                                    paramValueString = paramValueString.replace(',', '.');

                                    /*
                                    This does not work as needed
                                    Number n = null;
                                    NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
                                    try {
                                        n = nf.parse(paramValueString);
                                    } catch (RuntimeException e) {// French accepts "," instead of "."
                                        nf = NumberFormat.getInstance(Locale.FRANCE);
                                        n = nf.parse(paramValueString);
                                    }
                                    double paramValue = n.doubleValue();
                                    */

                                    value.setValue(Double.parseDouble(paramValueString));
                                }
                                DbUtil.saveOrUpdateObject(value);
                                break;
                            }
                        }
                    }
                }

                addRegForm.setEvent(null);
                return mapping.findForward("addEdit");
            default :
                location = null;
                if (addRegForm.getEditedId() == null || addRegForm.getEditedId() == 0) {
                    location = new AmpCategoryValueLocations();
                    AmpCategoryValueLocations parentLoc=null;
                    if (addRegForm.getParentLocationId() != -1)
                        parentLoc = DynLocationManagerUtil.getLocation(addRegForm.getParentLocationId(), true);
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
                location.setDeleted(false);

                try {

                    if (addRegForm.getEditedId() != null && addRegForm.getEditedId() != 0) {
                        LocationUtil.saveLocation(location, true);
                    } else {
                        LocationUtil.saveLocation(location, false);
                    }

                    if (CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0.equalsCategoryValue(
                            location.getParentCategoryValue())) {
                        DynLocationManagerUtil.synchronizeCountries();
                    }
                }
                catch (DuplicateLocationCodeException e) {
                    errors.add("title" ,
                            new ActionMessage("error.aim.addLocation.duplicateCode", TranslatorWorker.translateText("Duplicate")
                                    + " " +  TranslatorWorker.translateText(e.getCodeType()))
                    );
                    this.saveErrors(request, errors);
                    return mapping.findForward("addEdit");
                }

                return mapping.findForward("added");
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
