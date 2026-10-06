package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.form.DynLocationManagerForm;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

public class DynamicLocationManager extends MultiAction {
    private static Logger logger    = Logger.getLogger(DynamicLocationManager.class);
    
    @Override
    public ActionForward modePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ActionMessages errors = new ActionMessages();
        request.setAttribute("myErrors", errors);
        DynLocationManagerForm myForm = (DynLocationManagerForm) form;
        
        String hideEmptyCountriesStr = request.getParameter("hideEmptyCountriesAction");
        if ("false".equals(hideEmptyCountriesStr) ) {
            myForm.setHideEmptyCountries(false);
        }
        
        Collection<AmpCategoryValue> ampCategoryValueCollectionByKeyExcludeDeleted = CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(CategoryConstants.IMPLEMENTATION_LOCATION_KEY );
        List<AmpCategoryValue> locationLevels = new ArrayList<AmpCategoryValue>();
        locationLevels.addAll(ampCategoryValueCollectionByKeyExcludeDeleted);
        
        myForm.setLocationLevels(locationLevels);
        
        return modeSelect(mapping, form, request, response);
    }

    @Override
    public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        
        DynLocationManagerForm myForm = (DynLocationManagerForm) form;
        if (myForm.getTreeStructure() != null || myForm.getDeleteLocationId() != null) {
            modeProcess(mapping, myForm, request, response);
        }
        
        return modeShow(mapping, myForm, request, response);
    }

    public ActionForward modeShow(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        
        ActionMessages errors = (ActionMessages) request.getAttribute("myErrors");
        DynLocationManagerForm myForm = (DynLocationManagerForm) form;
        
        String errorString = CategoryManagerUtil.checkImplementationLocationCategory();
        if (errorString != null) {
            ActionMessage error = (ActionMessage) new ActionMessage("error.aim.categoryManager.implLocProblem", errorString);
            errors.add("title", error);
            myForm.setImportantErrorAppeared(true);
        } else {
            Collection<AmpCategoryValue> implLocations =
                 CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.IMPLEMENTATION_LOCATION_KEY);
            
            List<AmpCategoryValue> locationLevels = new ArrayList<>(implLocations);
            
            Collection<AmpCategoryValueLocations> rootLocations = null;
            if (locationLevels.size() == 0) {
                errors.add("title", new ActionMessage("error.aim.dynRegionManager.implLocationCategMissing" ) );
            }
            
            rootLocations = DynLocationManagerUtil.getHighestLayerLocations(myForm, errors);
            myForm.setFirstLevelLocations(rootLocations);
            myForm.setFirstLevelLocations(correctStructure(rootLocations, locationLevels, 0, myForm.getHideEmptyCountries()));
            myForm.setNumOfLayers(locationLevels.size());
            myForm.setFirstLayerId(locationLevels.get(0).getId() );
        }
        
        saveErrors(request, errors);
        
        return mapping.findForward("forward");
    }
    
    private void insertParentForSpecifiedLocs( Collection<AmpCategoryValueLocations> children, AmpCategoryValueLocations newParent ) {
        AmpCategoryValueLocations oldParent         = ((AmpCategoryValueLocations)children.toArray()[0]).getParentLocation();
        
        /* The new parents point of view */
        newParent.setParentLocation( oldParent );
        newParent.getChildLocations().addAll(children);
        
        /* The old parents point of view */
        if (oldParent != null) {
            oldParent.getChildLocations().removeAll(children);
            oldParent.getChildLocations().add(newParent);
        }
        
        /* The children point of view */
        for(AmpCategoryValueLocations location : children) {
            location.setParentLocation(newParent);
        }
    }
    
    private Collection<AmpCategoryValueLocations> correctStructure(Collection<AmpCategoryValueLocations> sameParentLocations, 
                            List<AmpCategoryValue> implLocValues, int layer, boolean hideEmptyCountries) {
        
        if (sameParentLocations == null || sameParentLocations.size() == 0) {
            return null;
        }
        
        int largestLayerIndex = 0;
        int countryLayerIndex = 0;
        
        try {
            AmpCategoryValue countryLayer = implLocValues.get(layer);
            countryLayerIndex = countryLayer.getIndex();
            largestLayerIndex = countryLayerIndex;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        HashMap<Integer, Set<AmpCategoryValueLocations>> layerToLocMap  = new HashMap<Integer, Set<AmpCategoryValueLocations>>();
        
        for(AmpCategoryValueLocations loc : sameParentLocations) {
            int currentLayer = loc.getParentCategoryValue().getIndex();
            largestLayerIndex = (currentLayer > largestLayerIndex) ? currentLayer : largestLayerIndex;
            
            Set<AmpCategoryValueLocations> layerSet = layerToLocMap.get(currentLayer);
            if (layerSet == null) {
                layerSet = new TreeSet<AmpCategoryValueLocations>(DynLocationManagerUtil.alphabeticalLocComp);
                layerToLocMap.put(currentLayer, layerSet);
            }
            
            if (hideEmptyCountries && currentLayer == countryLayerIndex && loc.getChildLocations().size() == 0 )
                continue;
            
            layerSet.add(loc);
        }
        
        for (int i = largestLayerIndex; i > implLocValues.get(layer).getIndex(); i--){
            Set<AmpCategoryValueLocations> layerSet = layerToLocMap.get(i);
            if (layerSet != null) {
                AmpCategoryValueLocations newLoc = new AmpCategoryValueLocations();
                newLoc.setId(-1L);
                newLoc.setName("Unspecified");
                newLoc.setParentCategoryValue(implLocValues.get(i-1));
                newLoc.setChildLocations( new TreeSet<AmpCategoryValueLocations>(DynLocationManagerUtil.alphabeticalLocComp) );
                Set<AmpCategoryValueLocations> nextLayerSet = layerToLocMap.get(i-1);
                if (nextLayerSet == null) {
                    nextLayerSet = new TreeSet<AmpCategoryValueLocations>(DynLocationManagerUtil.alphabeticalLocComp);
                    layerToLocMap.put(i-1, nextLayerSet);
                }
                
                nextLayerSet.add(newLoc);
                insertParentForSpecifiedLocs(layerSet, newLoc);
            }
        }
        
        for(AmpCategoryValueLocations loc : sameParentLocations) {
            if (loc.getChildLocations() != null && loc.getChildLocations().size() > 0 ) {
                correctStructure(loc.getChildLocations().stream()
                                .filter(l -> !l.getDeleted()).collect(Collectors.toList()),
                        implLocValues, layer + 1, hideEmptyCountries);
            }
        }
        
        return layerToLocMap.get(countryLayerIndex);
    }

    public ActionForward modeProcess(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        DynLocationManagerForm myForm   = (DynLocationManagerForm) form;
        ActionMessages errors = (ActionMessages) request.getAttribute("myErrors");
        
        
        if (errors == null) {
            errors = new ActionMessages();
        }
        
        DynLocationManagerUtil.clearRegionsOfDefaultCountryCache();
        
        if (myForm.getTreeStructure() != null && myForm.getTreeStructure().length() > 0)
            DynLocationManagerUtil.saveStructure(myForm.getTreeStructure(), myForm.getUnorgLocations(), myForm.getLocationLevels(), errors);
        else if (myForm.getDeleteLocationId() != null && myForm.getDeleteLocationId() > 0)
            DynLocationManagerUtil.deleteLocation(myForm.getDeleteLocationId(), errors);
        
        saveErrors(request, errors);
        
        return null;
    }
}
