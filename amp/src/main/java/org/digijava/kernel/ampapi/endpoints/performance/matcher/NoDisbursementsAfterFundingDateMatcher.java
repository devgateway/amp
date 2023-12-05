package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import org.digijava.kernel.ampapi.endpoints.performance.PerformanceIssue;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleManager;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherDefinition;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * The matcher checks if no disbursments were entered after certain period has passed from the selected funding date
 * 
 * @author Viorel Chihai
 *
 */
public class NoDisbursementsAfterFundingDateMatcher extends PerformanceRuleMatcher {
    
    private int timeUnit;
    private int timeAmount;
    private String selectedFundingDate;
    
    public NoDisbursementsAfterFundingDateMatcher(PerformanceRuleMatcherDefinition definition, 
            AmpPerformanceRule rule) {
        super(definition, rule);
        
        PerformanceRuleManager performanceRuleManager = PerformanceRuleManager.getInstance();

        timeUnit = performanceRuleManager.getCalendarTimeUnit(performanceRuleManager.getAttributeValue(rule, 
                PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT));
        timeAmount = Integer.parseInt(performanceRuleManager.getAttributeValue(rule, 
                PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT));
        selectedFundingDate = performanceRuleManager.getAttributeValue(rule,
                PerformanceRuleConstants.ATTRIBUTE_FUNDING_DATE);
    }

    @Override
    public PerformanceIssue findPerformanceIssue(AmpActivityVersion a) {
        Date currentDate = new Date();
        Set<AmpOrganisation> donorsWithIssues = new HashSet<>();
        
        for (AmpFunding f : a.getFunding()) {
            Date fundingSelectedDate = getFundingDate(f, selectedFundingDate);
            if (fundingSelectedDate != null) {
                Date deadline = getDeadline(fundingSelectedDate, timeUnit, timeAmount);
                if (deadline.before(currentDate)) {
                    if (f.getFundingDetails() == null || f.getFundingDetails().isEmpty()) {
                        donorsWithIssues.add(f.getAmpDonorOrgId());
                    } else {
                        boolean hasDisbursmentsAfterDeadline = f.getFundingDetails().stream()
                                .filter(t -> t.getTransactionType() == Constants.DISBURSEMENT)
                                .filter(t ->  t.getTransactionDate().before(currentDate))
                                .anyMatch(t -> t.getTransactionDate().after(fundingSelectedDate));
                        
                        if (!hasDisbursmentsAfterDeadline) {
                            donorsWithIssues.add(f.getAmpDonorOrgId());
                        }
                    }
                }
            }
        }
        
        if (donorsWithIssues.isEmpty()) {
            return null;
        }
        
        return new PerformanceIssue(this, new ArrayList<>(donorsWithIssues));
    }

    @Override
    protected boolean validate() {
        PerformanceRuleManager performanceRuleManager = PerformanceRuleManager.getInstance();
        
        if (rule.getAttributes() == null) {
            return false;
        }
        
        AmpPerformanceRuleAttribute attr1 = performanceRuleManager
                .getAttributeFromRule(rule, PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT);
        AmpPerformanceRuleAttribute attr2 = performanceRuleManager
                .getAttributeFromRule(rule, PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT);
        AmpPerformanceRuleAttribute attr3 = performanceRuleManager
                .getAttributeFromRule(rule, PerformanceRuleConstants.ATTRIBUTE_FUNDING_DATE);

        if (attr1 == null || attr2 == null || attr3 == null) {
            throw new IllegalArgumentException();
        }
        
        return true;
    }

}
