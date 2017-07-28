package org.digijava.kernel.ampapi.endpoints.performance.matchers;

import java.util.Date;

import org.digijava.kernel.ampapi.endpoints.performance.PerfomanceRuleManager;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;
import org.digijava.module.aim.helper.Constants;

/**
 *  The matcher checks if no disbursements were updated during a certain period from the selected funding date
 * 
 * @author Viorel Chihai
 *
 */
public class NoUpdatedDisbursmentsAfterTimePeriodMatcher extends PerformanceRuleMatcher {
    
    public NoUpdatedDisbursmentsAfterTimePeriodMatcher() {
        super("NoUpdatedDisbursments", "No updated disbursments");

        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT, 
                "Time Unit", PerformanceRuleAttributeType.TIME_UNIT));
        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT, 
                "Time Amount", PerformanceRuleAttributeType.AMOUNT));
    }

    @Override
    public boolean match(AmpPerformanceRule rule, AmpActivityVersion a) {
        PerfomanceRuleManager performanceRuleManager = PerfomanceRuleManager.getInstance();
        
        int timeUnit = performanceRuleManager.getCalendarTimeUnit(
                performanceRuleManager.getAttributeValue(rule, PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT));
        int timeAmount = Integer.parseInt(performanceRuleManager.getAttributeValue(rule, 
                PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT));
        
        Date deadline = getDeadline(new Date(), timeUnit, timeAmount);
        for(AmpFunding f : a.getFunding()) {
            boolean hasDisbursmentsAfterDeadline = f.getFundingDetails().stream()
                    .filter(t -> t.getTransactionType() == Constants.DISBURSEMENT)
                    .anyMatch(t -> t.getTransactionDate().after(deadline));
            
            if (hasDisbursmentsAfterDeadline) {
                return false;
            }
        }
        
        return true;
    }
    
}
