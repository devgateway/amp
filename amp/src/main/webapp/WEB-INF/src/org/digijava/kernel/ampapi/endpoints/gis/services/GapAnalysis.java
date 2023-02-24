/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorUtils;
import org.digijava.kernel.ampapi.endpoints.gis.PerformanceFilterParameters;
import org.digijava.kernel.ampapi.endpoints.util.GisConstants;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.dbentity.AmpLocationIndicatorValue;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;

/**
 * GIS Gap Analysis service
 * 
 * @author Nadejda Mandrescu
 */
public class GapAnalysis {
    protected static Logger LOGGER = Logger.getLogger(GapAnalysis.class);
    
    private Map<String, AmpIndicatorLayer> implLocPopulationLayer = new HashMap<>();
    
    private AmpIndicatorLayer indicator;
    private PerformanceFilterParameters input;
    private boolean canDoGapAnalysis = false;
    
    private Map<String, BigDecimal> admFundings;
    private Map<String, BigDecimal> populationCount;
    private boolean isPopulation;
    private String currencyCode;
    
    public GapAnalysis() {
    }
    
    public GapAnalysis(AmpIndicatorLayer indicator, PerformanceFilterParameters input) {
        this.indicator = indicator;
        this.input = input;
        initForGapAnalysis();
    }
    
    private void initForGapAnalysis() {
        this.canDoGapAnalysis = canDoGapAnalysis(indicator);
        
        if (canDoGapAnalysis) {
            prepareData();
        }
    }
    
    private void prepareData() {
        String implementationLocation = indicator.getAdmLevel() == null ? null : indicator.getAdmLevel().getValue();
        String admLevel = GisConstants.IMPL_CATEGORY_VALUE_TO_ADM.get(implementationLocation);
        if (admLevel != null) {
            // prepare population data if needed
            if (CategoryConstants.INDICATOR_LAYER_TYPE_POPULATION_RATIO.getValueKey().equals(
                    indicator.getIndicatorType().getValue())) {
                AmpIndicatorLayer populationLayer = getPopulationLayer(indicator.getAdmLevel());
                if (populationLayer == null) {
                    canDoGapAnalysis = false;
                    LOGGER.error("No population layer found for '" + implementationLocation + "'");
                } else {
                    buildPopulationData(populationLayer); 
                }
            }
            // prepare funding data
            if (canDoGapAnalysis) {
                LocationService locationService = new LocationService();
                rememberUsedCurrency(locationService.getLastReportSpec());
                admFundings = convertToMap(locationService.getTotals(admLevel, input, null));
            }
        } else {
            canDoGapAnalysis = false;
            LOGGER.error("Could not identify admLevel for implLevel = " + implementationLocation);
        }
    }
    
    private void rememberUsedCurrency(ReportSpecification spec) {
        if ( spec != null && spec.getSettings() != null && spec.getSettings().getCurrencyCode() != null) {
            currencyCode = spec.getSettings().getCurrencyCode(); 
        } else {
            currencyCode = EndpointUtils.getDefaultCurrencyCode(); 
        } 
    }

    
    private void buildPopulationData(AmpIndicatorLayer populationLayer) {
        isPopulation = true;
        populationCount = new HashMap<>();
        for (AmpLocationIndicatorValue locValue : populationLayer.getIndicatorValues()) {
            populationCount.put(locValue.getLocation().getGeoCode(), new BigDecimal(locValue.getValue()));
        }
    }
    
    public BigDecimal getGapAnalysisAmount(BigDecimal amount, String geoCode) {
        // if funding not generated, means is 0 funding for that geoCode
        BigDecimal fundingAmount = admFundings.getOrDefault(geoCode, BigDecimal.ZERO);
        BigDecimal factor = isPopulation ? populationCount.get(geoCode) : BigDecimal.ONE;
        // if no factor found for population, then we report 'No Data'
        if (amount == null || factor == null || BigDecimal.ZERO.compareTo(factor.multiply(amount)) == 0) {
            amount = null;
            // for testing only, until GIS is updated to handle "null"s
            // amount = BigDecimal.ZERO;
        } else {
            amount =  fundingAmount.divide(factor.multiply(amount), 6, RoundingMode.HALF_EVEN);
        }
        return amount;
    }
    
    public boolean isReadyForGapAnalysis() {
        return canDoGapAnalysis;
    }
    
    /**
     * Converts total fundings to internal map for flexibility
     * @param data
     * @return
     */
    private Map<String, BigDecimal> convertToMap(AdmLevelTotals data) {
        List<AdmLevelTotal> values = data.getValues();
        Map<String, BigDecimal> result = new HashMap<>();
        for (AdmLevelTotal admAmount : values) {
            String admId = admAmount.getAdmId();
            BigDecimal amount = admAmount.getAmount();
            if (admId != null) {
                result.put(admId, amount == null ? BigDecimal.ZERO : amount);
            }
        }
        return result;
    }
    
    /**
     * Get unique Population Layer designated for 'admLevel' and remember for reuse
     * @param admLevel the generic ADM level ("adm0", ...)
     * @return the population layer or null if no unique layer found
     */
    public AmpIndicatorLayer getPopulationLayer(String admLevel) {
        HardCodedCategoryValue hardcodedCatValue = GisConstants.ADM_TO_IMPL_CATEGORY_VALUE.get(admLevel);
        return getPopulationLayer(hardcodedCatValue == null ? null : hardcodedCatValue.getAmpCategoryValueFromDB());
    }
    
    /**
     * Get unique Population Layer designated for the given implementation location and remember for reuse
     * @param implLoc the implementation location (Region, etc)
     * @return  the population layer or null if no unique layer found
     */
    public AmpIndicatorLayer getPopulationLayer(AmpCategoryValue implLoc) {
        String implLocation = implLoc == null ? null : implLoc.getValue();
        AmpIndicatorLayer ail = implLocPopulationLayer.putIfAbsent(implLocation, IndicatorUtils.getPopulationLayer(implLoc));
        if (ail == null) {
            ail = implLocPopulationLayer.get(implLocation);
        }
        return ail;
    }
    
    /**
     * Checks if Gap Analysis can be done over the specified indicator layer
     * @param ail the indicator layer to test
     * @return true if gap analysis can be done over this indicator layer
     */
    public boolean canDoGapAnalysis(AmpIndicatorLayer ail) {
        return ail == null ? false : canDoGapAnalysis(ail.getIndicatorType(), ail.getAdmLevel());
    }
    
    /**
     * Checks if Gap Analysis can be done based on indicator type and ADM level 
     * @param indicatorType the indicator type
     * @param admLevel the admLevel
     * @return true if Gap Analysis can be done
     */
    public boolean canDoGapAnalysis(AmpCategoryValue indicatorType, AmpCategoryValue admLevel) {
        if (indicatorType != null) {
            String acvValue = indicatorType.getValue();
            if (acvValue.equals(CategoryConstants.INDICATOR_LAYER_TYPE_PER_CAPITA.getValueKey())
                    || acvValue.equals(CategoryConstants.INDICATOR_LAYER_TYPE_COUNT.getValueKey())
                    || (acvValue.equals(CategoryConstants.INDICATOR_LAYER_TYPE_POPULATION_RATIO.getValueKey())
                            && getPopulationLayer(admLevel) != null)) {
                return true;
            }
        }
        return false;
    }
    
    public String getCurrencyCode() {
        return currencyCode;
    }
    
}
