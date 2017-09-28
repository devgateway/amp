package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import java.util.Date;

import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleManager;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherDefinition;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ActivityUtil;

/**
 * The matcher checks if there are disbursements happened after a selected activity date 
 * 
 * @author Viorel Chihai
 *
 */
public class DisbursementsAfterActivityDateMatcher extends PerformanceRuleMatcher {
    
    private String selectedActivityDate;
    
    public DisbursementsAfterActivityDateMatcher(PerformanceRuleMatcherDefinition definition, AmpPerformanceRule rule) {
        super(definition, rule);
        
        PerformanceRuleManager performanceRuleManager = PerformanceRuleManager.getInstance();
        
        selectedActivityDate = performanceRuleManager.getAttributeValue(rule, 
                PerformanceRuleConstants.ATTRIBUTE_ACTIVITY_DATE);
    }

    @Override
    public boolean match(AmpActivityVersion a) {
        Date deadline = getActivityDate(a, selectedActivityDate);
        Date currentDate = new Date();
        
        if (deadline != null && deadline.before(currentDate)) {
            boolean hasDisbursmentsAfterDeadline = ActivityUtil.getTransactionsWithType(a, Constants.DISBURSEMENT)
                    .stream()
                    .filter(t -> t.getTransactionDate().before(currentDate))
                    .anyMatch(t -> t.getTransactionDate().after(deadline));
                
            if (hasDisbursmentsAfterDeadline) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected boolean validate() {
        PerformanceRuleManager performanceRuleManager = PerformanceRuleManager.getInstance();
        AmpPerformanceRuleAttribute attribute = performanceRuleManager
                .getAttributeFromRule(rule, PerformanceRuleConstants.ATTRIBUTE_ACTIVITY_DATE);
        
        if (attribute == null) {
            throw new IllegalArgumentException();
        }
        
        return true;
    }
    
}
