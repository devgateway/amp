/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorEPConstants;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorErrors;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorService;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorUtils;
import org.digijava.kernel.ampapi.endpoints.gis.GisFormParameters;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * Gap Analysis execution over client side indicator layers  
 * 
 * @author Nadejda Mandrescu
 */
public class PublicGapAnalysis {
    
    private ApiEMGroup errors  = new ApiEMGroup();
    
    public PublicGapAnalysis() {
    }
    
    /**
     * Checks if Gap Analysis can be done based on indicator type and ADM level
     * @param indicatorTypeId the indicator type Id
     * @param admLevelId the adm level Id
     * @return true if Gap Analysis can be done
     */
    public JsonBean canDoGapAnalysis(Long indicatorTypeId, Long admLevelId) {
        AmpCategoryValue indicatorType = CategoryManagerUtil.getAmpCategoryValueFromDb(indicatorTypeId);
        AmpCategoryValue admLevel = CategoryManagerUtil.getAmpCategoryValueFromDb(admLevelId);
        if (indicatorType == null) {
            errors.addApiErrorMessage(IndicatorErrors.FIELD_INVALID_VALUE, "indicatorTypeId = " + indicatorTypeId);
        }
        if (admLevel == null) {
            errors.addApiErrorMessage(IndicatorErrors.FIELD_INVALID_VALUE, "admLevelId = " + admLevelId);
        }
        
        boolean hasErrors = !errors.isEmpty();
        boolean canDoGA = !hasErrors && (new GapAnalysis().canDoGapAnalysis(indicatorType, admLevel)); 
        JsonBean result = hasErrors ? ApiError.toError(errors.getAllErrors()) : new JsonBean();
        result.set(IndicatorEPConstants.CAN_DO_GAP_ANALYSIS, canDoGA);
        
        return result;
    }
    

    /**
     * Runs a Gap Analysis over external indicator data, that is not stored in our DB
     * @param input full indicator data, filters and settings
     * @return indicator values or error
     */
    public JsonBean doPublicGapAnalysis(GisFormParameters input) {
        Map<String, Object> indicatorMap = getDataAsMap(input.getIndicator(), IndicatorEPConstants.INDICATOR);
        if (errors.isEmpty()) {
            // create a temporary indicator layer
            JsonBean indicatorJson = new JsonBean();
            indicatorJson.any().putAll(indicatorMap);
            // For processing a public layer we need to remove the ID and restore it later.
            Long id = Long.valueOf(indicatorJson.get(IndicatorEPConstants.ID).toString());
            indicatorJson.set(IndicatorEPConstants.ID, null); // Make sure the public layer doesnt have an id.
            AmpIndicatorLayer indicator = IndicatorService.getIndicatorLayer(indicatorJson, errors, null);
            if (errors.isEmpty()) {
                JsonBean processedIndicator = IndicatorUtils.getIndicatorsAndLocationValues(indicator, input, true);
                processedIndicator.set(IndicatorEPConstants.ID, id);
                return processedIndicator;
            }
        }
        return ApiError.toError(errors);
    }
    
    private Map<String, Object> getDataAsMap(Object data, String name) {
        Map<String, Object> result = null;
        if (data != null) {
            if (Map.class.isAssignableFrom(data.getClass())) {
                result = (Map<String, Object>) data;
            } else {
                errors.addApiErrorMessage(IndicatorErrors.FIELD_INVALID_TYPE, name);
            }
        } else {
            errors.addApiErrorMessage(IndicatorErrors.FIELD_INVALID_VALUE, name + " = " + data);
        }
        return result;
    }

}
