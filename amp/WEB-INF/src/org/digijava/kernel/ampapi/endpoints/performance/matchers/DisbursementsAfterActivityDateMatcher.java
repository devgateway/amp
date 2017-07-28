package org.digijava.kernel.ampapi.endpoints.performance.matchers;

import java.util.Date;

import org.digijava.kernel.ampapi.endpoints.performance.PerfomanceRuleManager;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ActivityUtil;

/**
 * The matcher checks if there are disbursements happened after a selected activity date 
 * 
 * @author Viorel Chihai
 *
 */
public class DisbursementsAfterActivityDateMatcher extends PerformanceRuleMatcher {
    
    public DisbursementsAfterActivityDateMatcher() {
        super("disbursementsAfterActivityDate", "Disbursements after selected activity date");

        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_ACTIVITY_DATE, 
                "Funding Date", PerformanceRuleAttributeType.ACTIVITY_DATE));
    }

    @Override
    public boolean match(AmpPerformanceRule rule, AmpActivityVersion a) {
        PerfomanceRuleManager performanceRuleManager = PerfomanceRuleManager.getInstance();
        
        String selectedActivityDate = performanceRuleManager.getAttributeValue(rule,
                PerformanceRuleConstants.ATTRIBUTE_ACTIVITY_DATE);
        
        
        Date deadline = getActivityDate(a, selectedActivityDate);
        if (deadline != null) {
            boolean hasDisbursmentsAfterDeadline = ActivityUtil.getTransactionsWithType(a, Constants.DISBURSEMENT)
                    .stream()
                    .anyMatch(t -> t.getTransactionDate().after(deadline));
                
            if (hasDisbursmentsAfterDeadline) {
                return true;
            }
        }

        return false;
    }
    
}
