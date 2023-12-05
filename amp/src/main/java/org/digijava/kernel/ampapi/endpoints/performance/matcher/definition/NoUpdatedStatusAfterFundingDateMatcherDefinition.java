package org.digijava.kernel.ampapi.endpoints.performance.matcher.definition;

import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.NoUpdatedStatusAfterFundingDateMatcher;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.PerformanceRuleMatcher;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;

import java.util.List;
import java.util.function.Function;

/**
 * matcher definition - if no disbursements were updated during a certain period
 * from the selected funding date
 * 
 * @author Viorel Chihai
 *
 */
public class NoUpdatedStatusAfterFundingDateMatcherDefinition extends PerformanceRuleMatcherDefinition {

    public NoUpdatedStatusAfterFundingDateMatcherDefinition() {

        super(PerformanceRuleConstants.MATCHER_NO_UPDATED_STATUS, 
                PerformanceRuleConstants.MATCHER_DESCR_NO_UPDATED_STATUS, 
                String.format("{%s} {%s} went by after the '{%s}' and the project status was not modified to '{%s}'", 
                        PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT, 
                        PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT, 
                        PerformanceRuleConstants.ATTRIBUTE_FUNDING_DATE,
                        PerformanceRuleConstants.ATTRIBUTE_ACTIVITY_STATUS));

        Function<PerformanceRuleAttributeType, List<PerformanceRuleAttributeOption>> possibleValuesSupplier = 
                PerformanceRuleMatcherPossibleValuesSupplier.getInstance().getSupplier();

        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT, "Time Unit",
                PerformanceRuleAttributeType.TIME_UNIT, possibleValuesSupplier));
        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT,
                "Time Amount", PerformanceRuleAttributeType.AMOUNT, possibleValuesSupplier));
        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_FUNDING_DATE,
                "Funding Date", PerformanceRuleAttributeType.FUNDING_DATE, possibleValuesSupplier));
        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_ACTIVITY_STATUS,
                "Activity Status", PerformanceRuleAttributeType.ACTIVITY_STATUS, possibleValuesSupplier));
    }

    @Override
    public PerformanceRuleMatcher createMatcher(AmpPerformanceRule rule) {
        return new NoUpdatedStatusAfterFundingDateMatcher(this, rule);
    }

}
