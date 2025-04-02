package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.EditActivityForm.Location;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class SelectLocation extends Action {
    
    private static Logger logger = Logger.getLogger(SelectLocation.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.Exception {
    
        EditActivityForm eaForm = (EditActivityForm) form;
        eaForm.getLocation().setNoMoreRecords(false);
        Location location = eaForm.getLocation();
    
        ActionForward retAF = null;
        if (request.getParameter("forwardToPopin") != null) {
            retAF = mapping.findForward("forwardToPopin");
        } else {
            retAF = mapping.findForward("forwardToPopup");
        }
        
        
        String resetSelLocs = request.getParameter("resetSelLocs"); //Org.Manager Side -quick fix
        if (resetSelLocs != null && resetSelLocs.equalsIgnoreCase("reset")) {
            location.setSelectedLocs(null);
        }
        
        if (location.isLocationReset()) {
            eaForm.getLocation().setLocationReset(true);
            eaForm.reset(mapping, request);
        }
        
        Long impLocLevel;
        if (request.getParameter("implemLocationLevel") != null) {
            impLocLevel = new Long(request.getParameter("implemLocationLevel"));
            eaForm.getLocation().setImplemLocationLevel(impLocLevel);
        }
        
        AmpCategoryValue implLocValue = CategoryManagerUtil.getAmpCategoryValueFromDb(
                eaForm.getLocation().getImplemLocationLevel());
        
        if (implLocValue == null) {
            implLocValue = CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0.getAmpCategoryValueFromDB();
        }
    
        Integer impLevelValue = implLocValue != null ? new Integer(implLocValue.getIndex() + 1) : new Integer(1);
        eaForm.getLocation().setImpLevelValue(impLevelValue);
        
        /* New Region Manager changes */
        if (!"true".equals(request.getParameter("edit"))) {
            eaForm.getLocation().setParentLocId(null);
            eaForm.getLocation().getLocationByLayers().clear();
            eaForm.getLocation().getSelectedLayers().clear();
        }
        String cIso = FeaturesUtil.getDefaultCountryIso();
        if (request.getParameter("parentLocId") != null) {
            eaForm.getLocation().setParentLocId(new Long(request.getParameter("parentLocId")));
        }
        Long parentLocId = eaForm.getLocation().getParentLocId();
        Map<Integer, Collection<KeyValue>> locationByLayers = eaForm.getLocation().getLocationByLayers();
        AmpCategoryValue implLevel = CategoryManagerUtil.getAmpCategoryValueFromDb(eaForm.getLocation().getLevelId());
        
        if (implLevel != null && CategoryConstants.IMPLEMENTATION_LEVEL_INTERNATIONAL.equalsCategoryValue(implLevel)
                && CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0.equalsCategoryValue(implLocValue)) {
            Collection<AmpCategoryValueLocations> countries =
                    DynLocationManagerUtil.getLocationsByLayer(CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0);
            
            if (countries != null) {
                Integer countryIndex = null;
                ArrayList<KeyValue> countriesKV = new ArrayList<KeyValue>();
                for (AmpCategoryValueLocations country : countries) {
                    countryIndex = (countryIndex == null) ? country.getParentCategoryValue().getIndex() : countryIndex;
                    KeyValue countryKV = new KeyValue(country.getId().toString(), country.getName());
                    countriesKV.add(countryKV);
                }
                locationByLayers.put(countryIndex, countriesKV);
            }
            return retAF;
        }
        
        
        if (cIso != null && parentLocId == null) { // Setting up the country
            eaForm.getLocation().setDefaultCountryIsSet(true);
            AmpCategoryValueLocations defCountry = DynLocationManagerUtil
                    .getLocationByIso(cIso, CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0);
            Integer countryLayerIndex = defCountry.getParentCategoryValue().getIndex();
            KeyValue countryKV = new KeyValue(defCountry.getId().toString(), defCountry.getName());
            ArrayList<KeyValue> countries = new ArrayList<KeyValue>();
            countries.add(countryKV);
            locationByLayers.put(countryLayerIndex, countries);
            if (countryLayerIndex < implLocValue.getIndex()) {
                parentLocId = defCountry.getId();
            }
        }
        
        int lastLayer = 0;
        while (parentLocId != null) {
            AmpCategoryValueLocations parentLoc = DynLocationManagerUtil.getLocation(parentLocId, true);
            eaForm.getLocation().getSelectedLayers().put(parentLoc.getParentCategoryValue().getIndex(),
                    parentLoc.getId());
            
            Iterator<Entry<Integer, Collection<KeyValue>>> entryIter = locationByLayers.entrySet().iterator();
            while (entryIter.hasNext()) {
                /**
                 * In case the user changes a higher layer location all layers below need to be cleared.
                 */
                if (entryIter.next().getKey() > parentLoc.getParentCategoryValue().getIndex()) {
                    entryIter.remove();
                }
            }
            
            parentLocId = null;
            Collection<AmpCategoryValueLocations> childrenLocs = parentLoc.getChildLocations();
            if (childrenLocs != null && childrenLocs.size() > 0) {
                Integer currentLayer = (parentLoc.getParentCategoryValue().getIndex()) + 1;
                //((AmpCategoryValueLocations)childrenLocs.toArray()[0]).getParentCategoryValue().getIndex();
                
                if (currentLayer <= implLocValue.getIndex()) {
                    ArrayList<KeyValue> childrenKV = new ArrayList<KeyValue>(childrenLocs.size());
                    for (AmpCategoryValueLocations child : childrenLocs) {
                        boolean addLocationAllowed = true;
                        /**
                         * If we are on the last layer that needs to be shown, we have to
                         * hide locations that are already selected
                         */
                        if (currentLayer == implLocValue.getIndex() && eaForm.getLocation().getSelectedLocs() != null) {
                            for (org.digijava.module.aim.helper.Location l : eaForm.getLocation().getSelectedLocs()) {
                                if (child.equals(l.getAmpCVLocation())) {
                                    addLocationAllowed = false;
                                    break;
                                }
                            }
                        }
                        if (addLocationAllowed) {
                            childrenKV.add(new KeyValue(child.getId().toString(), child.getName()));
                        }
                    }
                    lastLayer = currentLayer;
                    locationByLayers.put(currentLayer, childrenKV);
                    
                    if (childrenLocs.size() == 1) {
                        AmpCategoryValueLocations newParent = (AmpCategoryValueLocations) childrenLocs.toArray()[0];
                        parentLocId = newParent.getId();
                    }
                }
                
            } else {
                if (lastLayer < implLocValue.getIndex()) {
                    eaForm.getLocation().setNoMoreRecords(true);
                }
            }
            
        }
        
        return retAF;
    }
}
