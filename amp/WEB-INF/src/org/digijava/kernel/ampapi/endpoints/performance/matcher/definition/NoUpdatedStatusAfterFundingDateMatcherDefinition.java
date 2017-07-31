package org.digijava.kernel.ampapi.endpoints.performance.matcher.definition;

import java.util.List;
import java.util.function.Function;

import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.NoUpdatedStatusAfterFundingDateMatcher;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.PerformanceRuleMatcher;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;

/**
 * matcher definition - if no disbursements were updated during a certain period
 * from the selected funding date
 * 
 * @author Viorel Chihai
 *
 */
public class NoUpdatedStatusAfterFundingDateMatcherDefinition extends PerformanceRuleMatcherDefinition {

    public NoUpdatedStatusAfterFundingDateMatcherDefinition() {

        super("noUpdatedStatusAfterFundingDate", "No updated status after selected funding date");

        Function<PerformanceRuleAttributeType, List<String>> possibleValuesSupplier = 
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
