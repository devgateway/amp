package org.digijava.kernel.ampapi.endpoints.gis.services;

import javax.ws.rs.core.Response;

import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.exception.AmpWebApplicationException;
import org.digijava.kernel.ampapi.endpoints.gis.RuntimeIndicatorGapAnalysisParameters;
import org.digijava.kernel.ampapi.endpoints.indicator.Indicator;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorService;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorUtils;
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
    public boolean canDoGapAnalysis(Long indicatorTypeId, Long admLevelId) {
        AmpCategoryValue indicatorType = CategoryManagerUtil.getAmpCategoryValueFromDb(indicatorTypeId);
        AmpCategoryValue admLevel = CategoryManagerUtil.getAmpCategoryValueFromDb(admLevelId);
        if (indicatorType == null || admLevel == null) {
            return false;
        }
        return new GapAnalysis().canDoGapAnalysis(indicatorType, admLevel);
    }
    

    /**
     * Runs a Gap Analysis over external indicator data, that is not stored in our DB
     * @param input full indicator data, filters and settings
     * @return indicator values or error
     */
    public Indicator doPublicGapAnalysis(RuntimeIndicatorGapAnalysisParameters input) {
        Indicator indicator = input.getIndicator();
        if (errors.isEmpty()) {
            Long id = indicator.getId();
            indicator.setId(null);
            AmpIndicatorLayer ampIndicator = IndicatorService.getIndicatorLayer(indicator, errors, null, false);
            if (errors.isEmpty()) {
                Indicator processedIndicator = IndicatorUtils.doGapAnalysis(ampIndicator, input);
                processedIndicator.setId(id);
                return processedIndicator;
            }
        }
        
        throw new AmpWebApplicationException(Response.Status.BAD_REQUEST, ApiError.toError(errors));
    }
}
