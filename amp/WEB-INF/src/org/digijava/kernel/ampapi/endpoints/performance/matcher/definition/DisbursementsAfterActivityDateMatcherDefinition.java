package org.digijava.kernel.ampapi.endpoints.performance.matcher.definition;

import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.DisbursementsAfterActivityDateMatcher;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.PerformanceRuleMatcher;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;

import java.util.List;
import java.util.function.Function;

/**
 * matcher definition - if there are disbursements happened after a selected activity date 
 * 
 * @author Viorel Chihai
 *
 */
public class DisbursementsAfterActivityDateMatcherDefinition extends PerformanceRuleMatcherDefinition {
    
    public DisbursementsAfterActivityDateMatcherDefinition() {
        super(PerformanceRuleConstants.MATCHER_DISB_ACTIVITY_DATE, 
                PerformanceRuleConstants.MATCHER_DESCR_DISB_ACTIVITY_DATE, 
                String.format("A disbursement happened after the '{%s}'", 
                        PerformanceRuleConstants.ATTRIBUTE_ACTIVITY_DATE));
        
        Function<PerformanceRuleAttributeType, List<PerformanceRuleAttributeOption>> possibleValuesSupplier = 
                PerformanceRuleMatcherPossibleValuesSupplier.getInstance().getSupplier();

        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_ACTIVITY_DATE, 
                "Activity Date", PerformanceRuleAttributeType.ACTIVITY_DATE, possibleValuesSupplier));
    }

    @Override
    public PerformanceRuleMatcher createMatcher(AmpPerformanceRule rule) {
        return new DisbursementsAfterActivityDateMatcher(this, rule);
    }
    
}
