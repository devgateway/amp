package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherPossibleValuesSupplier;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;
import org.digijava.module.aim.helper.Constants;

public class PerformanceRuleMatcherPossibleValuesSupplierTest {

    public static List<String> getDefaultPerformanceRuleAttributePossibleValues(PerformanceRuleAttributeType type) {
        switch (type) {
            case TIME_UNIT:
                return PerformanceRuleMatcherPossibleValuesSupplier.getPeriodPossibleValues();
            case ACTIVITY_STATUS:
                return getActivityStatusPossibleValues();
            case ACTIVITY_DATE:
                return PerformanceRuleMatcherPossibleValuesSupplier.getActivityDatePossibleValue();
            case FUNDING_DATE:
                return PerformanceRuleMatcherPossibleValuesSupplier.getFundingDatePossibleValue();
            default:
                return new ArrayList<String>();
        }
    }

    public static List<String> getActivityStatusPossibleValues() {
        return Stream.of(Constants.ACTIVITY_STATUS_ONGOING, Constants.ACTIVITY_STATUS_COMPLETED,
                        Constants.ACTIVITY_STATUS_PLANNED, Constants.ACTIVITY_STATUS_CANCELLED)
                .collect(Collectors.toList());
    }

}
