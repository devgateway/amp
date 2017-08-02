package org.digijava.kernel.ampapi.endpoints.performance.matcher.definition;

import java.util.List;
import java.util.function.Function;

import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.NoDisbursmentsAfterFundingDateMatcher;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.PerformanceRuleMatcher;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;

/**
 * matcher definition - if no disbursments were entered after certain period has passed from the selected funding date
 * 
 * @author Viorel Chihai
 *
 */
public class NoDisbursmentsAfterFundingDateMatcherDefinition extends PerformanceRuleMatcherDefinition {
    
    public NoDisbursmentsAfterFundingDateMatcherDefinition() {
        
        super("noDisbursementsAfterFundingDate", "No disbursments after selected funding date");
        
        Function<PerformanceRuleAttributeType, List<String>> possibleValuesSupplier = 
                PerformanceRuleMatcherPossibleValuesSupplier.getInstance().getSupplier();

        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT, 
                "Time Unit", PerformanceRuleAttributeType.TIME_UNIT, possibleValuesSupplier));
        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT, 
                "Time Amount", PerformanceRuleAttributeType.AMOUNT, possibleValuesSupplier));
        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_FUNDING_DATE, 
                "Funding Date", PerformanceRuleAttributeType.FUNDING_DATE, possibleValuesSupplier));
    }

    @Override
    public PerformanceRuleMatcher createMatcher(AmpPerformanceRule rule) {
        return new NoDisbursmentsAfterFundingDateMatcher(this, rule);
    }
    
}
