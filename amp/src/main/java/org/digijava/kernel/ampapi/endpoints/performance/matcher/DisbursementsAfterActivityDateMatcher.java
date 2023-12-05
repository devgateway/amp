package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import org.digijava.kernel.ampapi.endpoints.performance.PerformanceIssue;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleManager;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherDefinition;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ActivityUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public PerformanceIssue findPerformanceIssue(AmpActivityVersion a) {
        Date deadline = getActivityDate(a, selectedActivityDate);
        Date currentDate = new Date();
        
        if (deadline != null && deadline.before(currentDate)) {
            List<AmpFundingDetail> transactions = ActivityUtil.getTransactionsWithType(a, Constants.DISBURSEMENT)
                    .stream()
                    .filter(t -> t.getTransactionDate().before(currentDate))
                    .filter(t -> t.getTransactionDate().after(deadline))
                    .collect(Collectors.toList());
                
            if (!transactions.isEmpty()) {
                Set<AmpOrganisation> donorWithIssues = transactions.stream()
                        .map(fd -> fd.getAmpFundingId().getAmpDonorOrgId())
                        .collect(Collectors.toSet());
                
                return new PerformanceIssue(this, new ArrayList<>(donorWithIssues));
            }
        }

        return null;
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
