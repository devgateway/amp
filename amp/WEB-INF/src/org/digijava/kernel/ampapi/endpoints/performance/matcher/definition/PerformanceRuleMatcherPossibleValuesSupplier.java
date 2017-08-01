package org.digijava.kernel.ampapi.endpoints.performance.matcher.definition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * 
 * @author Viorel Chihai
 *
 */
public final class PerformanceRuleMatcherPossibleValuesSupplier {
    
    private static PerformanceRuleMatcherPossibleValuesSupplier instance;
    
    private Function<PerformanceRuleAttributeType, List<String>> supplier;

    private PerformanceRuleMatcherPossibleValuesSupplier() {

    }

    public static List<String> getDefaultPerformanceRuleAttributePossibleValues(PerformanceRuleAttributeType type) {
        switch (type) {
            case TIME_UNIT:
                return getPeriodPossibleValues();
            case ACTIVITY_STATUS:
                return getActivityStatusPossibleValues();
            case ACTIVITY_DATE:
                return getActivityDatePossibleValue();
            case FUNDING_DATE:
                return getFundingDatePossibleValue();
            default:
                return Collections.emptyList();
        }
    }

    public static List<String> getActivityStatusPossibleValues() {
        return CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_STATUS_KEY).stream()
                .map(AmpCategoryValue::getLabel).collect(Collectors.toList());
    }

    public static List<String> getPeriodPossibleValues() {
        return Arrays.asList(PerformanceRuleConstants.TIME_UNIT_DAY, PerformanceRuleConstants.TIME_UNIT_MONTH,
                PerformanceRuleConstants.TIME_UNIT_YEAR);
    }

    public static List<String> getActivityDatePossibleValue() {
        return Arrays.asList(PerformanceRuleConstants.ACTIVITY_CLOSING_DATE);
    }

    public static List<String> getFundingDatePossibleValue() {
        return Arrays.asList(PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE);
    }

    public static PerformanceRuleMatcherPossibleValuesSupplier getInstance() {
        if (instance == null) {
            instance = new PerformanceRuleMatcherPossibleValuesSupplier();
        }
        
        return instance;
    }
    
    public void setSupplier(Function<PerformanceRuleAttributeType, List<String>> supplier) {
        this.supplier = supplier;
    }
    
    public Function<PerformanceRuleAttributeType, List<String>> getSupplier() {
        if (supplier == null) {
            supplier = PerformanceRuleMatcherPossibleValuesSupplier::getDefaultPerformanceRuleAttributePossibleValues;
        }
        
        return supplier;
    }
}
