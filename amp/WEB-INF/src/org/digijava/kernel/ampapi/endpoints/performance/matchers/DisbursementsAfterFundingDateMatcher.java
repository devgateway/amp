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
 * The matcher checks if there are disbursements happened after a selected funding date 
 * 
 * @author Viorel Chihai
 *
 */
public class DisbursementsAfterFundingDateMatcher extends PerformanceRuleMatcher {
    
    public DisbursementsAfterFundingDateMatcher() {
        super("disbursementsAfterFundingDate", "Disbursements after selected funding date");

        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_FUNDING_DATE, 
                "Funding Date", PerformanceRuleAttributeType.FUNDING_DATE));
    }

    @Override
    public boolean match(AmpPerformanceRule rule, AmpActivityVersion a) {
        PerfomanceRuleManager performanceRuleManager = PerfomanceRuleManager.getInstance();
        
        String selectedFundingDate = performanceRuleManager.getAttributeValue(rule,
                PerformanceRuleConstants.ATTRIBUTE_FUNDING_DATE);
        
        for(AmpFunding f : a.getFunding()) {
            Date deadline = getFundingDate(f, selectedFundingDate);
            
            if (deadline != null) {
                boolean hasDisbursmentsAfterDeadline = f.getFundingDetails().stream()
                        .filter(t -> t.getTransactionType() == Constants.DISBURSEMENT)
                        .anyMatch(t -> t.getTransactionDate().after(deadline));
                
                if (hasDisbursmentsAfterDeadline) {
                    return true;
                }
            }
        }

        return false;
    }
    
}
