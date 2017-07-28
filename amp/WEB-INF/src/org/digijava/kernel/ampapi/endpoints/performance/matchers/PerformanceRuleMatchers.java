package org.digijava.kernel.ampapi.endpoints.performance.matchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
public final class PerformanceRuleMatchers {

    public static final List<PerformanceRuleMatcher> RULE_TYPES = new ArrayList<PerformanceRuleMatcher>() {
        {
            add(new NoUpdatedStatusAfterFundingDateMatcher());
            add(new NoDisbursmentsAfterFundingDateMatcher());
            add(new DisbursementsAfterActivityDateMatcher());
            add(new NoUpdatedDisbursmentsAfterTimePeriodMatcher());
        }
    };
    
    public static final Map<String, PerformanceRuleMatcher> RULE_TYPES_BY_NAME = 
            PerformanceRuleMatchers.RULE_TYPES.stream()
            .collect(Collectors.toMap(r -> r.getName(), Function.identity()));
    
    private PerformanceRuleMatchers() {

    }
    
    public static List<String> getPerformanceRuleAttributePossibleValues(PerformanceRuleAttributeType type) {
        switch (type) {
            case TIME_UNIT :
                return getPeriodPossibleValues();
            case ACTIVITY_STATUS :
                return getActivityStatusPossibleValues();
            case ACTIVITY_DATE :
                return getActivityDatePossibleValue();
            case FUNDING_DATE:
                return getFundingDatePossibleValue();
            default:
                return null;
        }
    }
    
    public static List<String> getActivityStatusPossibleValues() {
        return CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_STATUS_KEY)
                .stream().map(AmpCategoryValue::getLabel)
                .collect(Collectors.toList());
    }
    
    public static List<String> getPeriodPossibleValues() {
        return Arrays.asList(PerformanceRuleConstants.TIME_UNIT_DAY, 
                PerformanceRuleConstants.TIME_UNIT_MONTH, 
                PerformanceRuleConstants.TIME_UNIT_YEAR);
    }
    
    public static List<String> getActivityDatePossibleValue() {
        return Arrays.asList(PerformanceRuleConstants.ACTIVITY_CLOSING_DATE);
    }
    
    public static List<String> getFundingDatePossibleValue() {
        return Arrays.asList(PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE);
    }
    
}
