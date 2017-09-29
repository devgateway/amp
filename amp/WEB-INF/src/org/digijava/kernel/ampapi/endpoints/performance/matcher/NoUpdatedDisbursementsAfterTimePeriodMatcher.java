package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import java.util.Calendar;
import java.util.Date;

import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleManager;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherDefinition;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute;
import org.digijava.module.aim.helper.Constants;

/**
 *  The matcher checks if no disbursements were updated during a certain period
 * 
 * @author Viorel Chihai
 *
 */
public class NoUpdatedDisbursementsAfterTimePeriodMatcher extends PerformanceRuleMatcher {
    
    private int timeUnit;
    private int timeAmount;
    
    public NoUpdatedDisbursementsAfterTimePeriodMatcher(PerformanceRuleMatcherDefinition definition, 
            AmpPerformanceRule rule) {
        super(definition, rule);
        
        PerformanceRuleManager performanceRuleManager = PerformanceRuleManager.getInstance();
        
        timeUnit = performanceRuleManager.getCalendarTimeUnit(
                performanceRuleManager.getAttributeValue(rule, PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT));
        timeAmount = Integer.parseInt(performanceRuleManager.getAttributeValue(rule, 
                PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT));
    }

    @Override
    public boolean match(AmpActivityVersion a) {
        Date currentDate = new Date();
        Date deadline = getDeadline(currentDate, timeUnit, timeAmount);
        
        for (AmpFunding f : a.getFunding()) {
            if (f.getFundingDetails() == null || f.getFundingDetails().isEmpty()) {
                return true;
            }
            
            boolean hasDisbursmentsAfterDeadline = f.getFundingDetails().stream()
                    .filter(t -> t.getTransactionType() == Constants.DISBURSEMENT)
                    .filter(t -> t.getTransactionDate().before(currentDate))
                    .anyMatch(t -> t.getTransactionDate().after(deadline));
            
            if (!hasDisbursmentsAfterDeadline) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    protected boolean validate() {
        PerformanceRuleManager performanceRuleManager = PerformanceRuleManager.getInstance();
        AmpPerformanceRuleAttribute attr1 = performanceRuleManager
                .getAttributeFromRule(rule, PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT);
        AmpPerformanceRuleAttribute attr2 = performanceRuleManager
                .getAttributeFromRule(rule, PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT);

        if (attr1 == null || attr2 == null) {
            throw new IllegalArgumentException();
        }
        
        return true;
    }
    
    public Date getDeadline(Date selectedDate, int timeUnit, int timeAmount) {
        Calendar c = Calendar.getInstance();
        c.setTime(selectedDate);
        c.add(timeUnit, -timeAmount);

        return c.getTime();
    }
    
}
