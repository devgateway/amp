package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleAttributeOption;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherPossibleValuesSupplier;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;
import org.digijava.module.aim.helper.Constants;

public class PerformanceRuleMatcherPossibleValuesSupplierTest {

    public static List<PerformanceRuleAttributeOption> getDefaultPerformanceRuleAttributePossibleValues(
            PerformanceRuleAttributeType type) {
        switch (type) {
            case TIME_UNIT:
                return PerformanceRuleMatcherPossibleValuesSupplier.getPeriodPossibleValues();
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

    public static List<PerformanceRuleAttributeOption> getActivityStatusPossibleValues() {
        return Stream.of(
                new PerformanceRuleAttributeOption(Constants.ACTIVITY_STATUS_ONGOING), 
                new PerformanceRuleAttributeOption(Constants.ACTIVITY_STATUS_COMPLETED),
                new PerformanceRuleAttributeOption(Constants.ACTIVITY_STATUS_PLANNED), 
                new PerformanceRuleAttributeOption(Constants.ACTIVITY_STATUS_CANCELLED))
                .collect(Collectors.toList());
    }
    
    public static List<PerformanceRuleAttributeOption> getActivityDatePossibleValue() {
        return Arrays.asList(
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.ACTIVITY_COMPLETION_DATE),
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.ACTIVITY_ACTUAL_APPROVAL_DATE),
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.ACTIVITY_PROPOSED_START_DATE),
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.ACTIVITY_ORIGINAL_COMPLETING_DATE),
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.ACTIVITY_ACTUAL_START_DATE),
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.ACTIVITY_CONTRACTING_DATE),
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.ACTIVITY_DISBURSEMENTS_DATE));
    }

    public static List<PerformanceRuleAttributeOption> getFundingDatePossibleValue() {
        return Arrays.asList(
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE),
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.FUNDING_EFFECTIVE_DATE),
                new PerformanceRuleAttributeOption(PerformanceRuleConstants.FUNDING_CLOSING_DATE));
    }

}
