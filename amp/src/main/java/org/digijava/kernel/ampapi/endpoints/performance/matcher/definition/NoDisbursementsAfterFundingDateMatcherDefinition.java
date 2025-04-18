package org.digijava.kernel.ampapi.endpoints.performance.matcher.definition;

import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.NoDisbursementsAfterFundingDateMatcher;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.PerformanceRuleMatcher;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;

import java.util.List;
import java.util.function.Function;

/**
 * matcher definition - if no disbursments were entered after certain period has passed from the selected funding date
 * 
 * @author Viorel Chihai
 *
 */
public class NoDisbursementsAfterFundingDateMatcherDefinition extends PerformanceRuleMatcherDefinition {
    
    public NoDisbursementsAfterFundingDateMatcherDefinition() {
        
        super(PerformanceRuleConstants.MATCHER_NO_DISB_FUNDING_DATE, 
                PerformanceRuleConstants.MATCHER_DESCR_NO_DISB_FUNDING_DATE, 
                String.format("{%s} {%s} have passed since the '{%s}' and still no disbursement from donor", 
                        PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT, 
                        PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT, 
                        PerformanceRuleConstants.ATTRIBUTE_FUNDING_DATE));
        
        Function<PerformanceRuleAttributeType, List<PerformanceRuleAttributeOption>> possibleValuesSupplier = 
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
        return new NoDisbursementsAfterFundingDateMatcher(this, rule);
    }
    
}
