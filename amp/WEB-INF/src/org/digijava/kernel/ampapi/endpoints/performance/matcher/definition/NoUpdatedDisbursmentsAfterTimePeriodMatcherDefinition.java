package org.digijava.kernel.ampapi.endpoints.performance.matcher.definition;

import java.util.List;
import java.util.function.Function;

import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.NoUpdatedDisbursmentsAfterTimePeriodMatcher;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.PerformanceRuleMatcher;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;

/**
 * matcher definition - if no disbursements were updated during a certain period from the selected funding date
 * 
 * @author Viorel Chihai
 *
 */
public class NoUpdatedDisbursmentsAfterTimePeriodMatcherDefinition extends PerformanceRuleMatcherDefinition {
    
    public NoUpdatedDisbursmentsAfterTimePeriodMatcherDefinition() {
        
        super("noUpdatedDisbursments", "No updated disbursments");
        
        Function<PerformanceRuleAttributeType, List<String>> possibleValuesSupplier = 
                PerformanceRuleMatcherPossibleValuesSupplier.getInstance().getSupplier();

        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT, 
                "Time Unit", PerformanceRuleAttributeType.TIME_UNIT, possibleValuesSupplier));
        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT, 
                "Time Amount", PerformanceRuleAttributeType.AMOUNT, possibleValuesSupplier));
    }

    @Override
    public PerformanceRuleMatcher createMatcher(AmpPerformanceRule rule) {
        return new NoUpdatedDisbursmentsAfterTimePeriodMatcher(this, rule);
    }
    
}
