/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.indicator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.exception.AmpWebApplicationException;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * Handles designation of some indicator layers as population layers
 * 
 * @author Nadejda Mandrescu
 */
public class PopulationLayerDesignator {
    protected static final Logger LOGGER = Logger.getLogger(PopulationLayerDesignator.class);
    
    private ApiEMGroup errors = new ApiEMGroup();
    
    public PopulationLayerDesignator() {
    }
    
    /**
     * @return list of indicator ids that are of type 'Count' and are not at 'Country' implementation location 
     */
    public List<Long> getAllowedPopulationLayersOptions() {
        AmpCategoryValue typeAcv = CategoryConstants.INDICATOR_LAYER_TYPE_COUNT.getAmpCategoryValueFromDB();
        AmpCategoryValue admAcv = CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getAmpCategoryValueFromDB();
        Long admId = admAcv == null ? null : admAcv.getId(); // exclude none if something wrong  
        if (typeAcv != null && typeAcv.isVisible()) {
            return DynLocationManagerUtil.getIndicatorLayersIdsByTypeExcludeAdm(typeAcv.getId(), admId);
        }
        return Collections.emptyList();
    }
    
    /**
     * Designate indicator layers as population layers for their corresponding admLevel. 
     * @param populationLayerRequest containing indicator layers ids list
     * @return no data on success or errors
     */
    public void designateAsPopulationLayers(PopulationLayerRequest populationLayerRequest) {
        List<Long> ids = populationLayerRequest.getLayersIds();
        LOGGER.info("Designating new population layers: " + Util.toCSString(ids));
        
        List<AmpIndicatorLayer> newPopulationLayers = new ArrayList<>(ids.size());
        Map<Long, Set<Long>> admLevelIndicatorMap = new HashMap<>();
        Map<Long, AmpIndicatorLayer> idToAil = new HashMap<>();
        for (Long id : ids) {
            AmpIndicatorLayer ail = id == null ? null : DynLocationManagerUtil.getIndicatorLayerById(id);
            Long admId = (ail == null || ail.getAdmLevel() == null) ? null : ail.getAdmLevel().getId();
            Set<Long> indicators = admLevelIndicatorMap.putIfAbsent(admId, new HashSet<>());
            if (indicators == null) { 
                indicators = admLevelIndicatorMap.get(admId);
            }
            if (indicators.add(id)) {
                newPopulationLayers.add(ail);
            }
            idToAil.put(id, ail);
        }
        validate(admLevelIndicatorMap, idToAil);
        
        if (errors.isEmpty()) {
            DynLocationManagerUtil.setIndicatorLayersPopulation(false, null);
            if (!ids.isEmpty()) {
                DynLocationManagerUtil.setIndicatorLayersPopulation(true, ids);
            }
            
            return;
        }
    
        throw new AmpWebApplicationException(Response.Status.BAD_REQUEST, ApiError.toError(errors));
    }
    
    private void validate(Map<Long, Set<Long>> admLevelIndicatorMap, Map<Long, AmpIndicatorLayer> idToAil) {
        // invalid ids
        Set<Long> invalidIds = new HashSet<Long>();
        for (Entry<Long, AmpIndicatorLayer> entry : idToAil.entrySet()) {
            if (entry.getValue() == null) {
                invalidIds.add(entry.getKey());
            }
        }
        if (!invalidIds.isEmpty()) {
            errors.addApiErrorMessage(IndicatorErrors.INVALID_POPULATION_LAYERS,
                    "No indicator layer found with ids: " + Util.toCSString(invalidIds));
        }
        // no adm level found
        if (admLevelIndicatorMap.containsKey(null)) {
            Set<Long> indicatorsWithNoAdms = admLevelIndicatorMap.get(null);
            indicatorsWithNoAdms.removeAll(invalidIds);
            if (!indicatorsWithNoAdms.isEmpty()) {
                String err = getLayerInfo(indicatorsWithNoAdms, idToAil);
                errors.addApiErrorMessage(IndicatorErrors.INVALID_POPULATION_LAYERS, "indicators with no admLevel: " + err);
            }
        }
        // duplicate indicators
        for (Entry<Long, Set<Long>> entry : admLevelIndicatorMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                String err = String.format("duplicate indicators with the same adm layer (%d): %s ", entry.getKey(),                        
                        getLayerInfo(entry.getValue(), idToAil));
                errors.addApiErrorMessage(IndicatorErrors.INVALID_POPULATION_LAYERS, err);
            }
        }
        // invalid indicator type or adm level
        String invalidTypes = "";
        String invalidAdmLevel = "";
        String allowedType = CategoryConstants.INDICATOR_LAYER_TYPE_COUNT.getValueKey();
        String notAllowedAdm = CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getValueKey();
        for (AmpIndicatorLayer newPopLayer : idToAil.values()) {
            if (newPopLayer == null) continue;
            String indType = newPopLayer.getIndicatorType() == null ? null : newPopLayer.getIndicatorType().getValue();
            String admLevel = newPopLayer.getAdmLevel() == null ? null : newPopLayer.getAdmLevel().getValue();
            if (!allowedType.equals(indType)) {
                invalidTypes += String.format(", (indicatorId = %d, type = %s)", newPopLayer.getId(), indType);
            }
            if (admLevel != null && notAllowedAdm.equals(admLevel)) { // not reporting twice if no admLevel
                invalidAdmLevel += ", " + newPopLayer.getId();
            }
        }
        if (invalidTypes.length() > 0) {
            errors.addApiErrorMessage(IndicatorErrors.INVALID_POPULATION_LAYERS, String.format(
                    "Only '%s' type layers can be designated as population layers, wrong entries: %s",
                    allowedType, invalidTypes.substring(2)));
        }
        if (invalidAdmLevel.length() > 0) {
            errors.addApiErrorMessage(IndicatorErrors.INVALID_POPULATION_LAYERS, String.format(
                    "'%s' adm level not allowed as population layer, used by indicatorsL", 
                    notAllowedAdm, invalidAdmLevel.substring(2)));
        }
    }
    
    private String getLayerInfo(Collection<Long> layerIds, Map<Long, AmpIndicatorLayer> idToAil) {
        String invalidLayers = "";
        for (Long id : layerIds) {
            AmpIndicatorLayer ail = idToAil.get(id);
            invalidLayers += ", " + (ail == null ? "id = " + id : "name = " + ail.getName());
        }
        return layerIds.size() > 0 ? invalidLayers.substring(2) : invalidLayers;
    }

}
