package org.digijava.kernel.ampapi.endpoints.performance.matcher.definition;

import java.util.List;
import java.util.function.Function;

import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.NoUpdatedDisbursementsAfterTimePeriodMatcher;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.PerformanceRuleMatcher;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;

/**
 * matcher definition - if no disbursements were updated during a certain period from the selected funding date
 * 
 * @author Viorel Chihai
 *
 */
public class NoUpdatedDisbursementsAfterTimePeriodMatcherDefinition extends PerformanceRuleMatcherDefinition {
    
    public NoUpdatedDisbursementsAfterTimePeriodMatcherDefinition() {
        
        super(PerformanceRuleConstants.MATCHER_NO_UPD_DISB_TIME_PERIOD, 
                PerformanceRuleConstants.MATCHER_DESCR_NO_UPD_DISB_TIME_PERIOD, 
                String.format("Disbursements (actual or planned) from one or more donor "
                        + "have not been added in the last {%s} {%s}", 
                        PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT, 
                        PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT));
        
        Function<PerformanceRuleAttributeType, List<PerformanceRuleAttributeOption>> possibleValuesSupplier = 
                PerformanceRuleMatcherPossibleValuesSupplier.getInstance().getSupplier();

        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT, 
                "Time Unit", PerformanceRuleAttributeType.TIME_UNIT, possibleValuesSupplier));
        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT, 
                "Time Amount", PerformanceRuleAttributeType.AMOUNT, possibleValuesSupplier));
    }

    @Override
    public PerformanceRuleMatcher createMatcher(AmpPerformanceRule rule) {
        return new NoUpdatedDisbursementsAfterTimePeriodMatcher(this, rule);
    }
    
}
