package org.digijava.kernel.ampapi.endpoints.performance.matchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ActivityUtil;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class DisbursementsAfterClosingDateMatcher extends PerformanceRuleMatcher {
    
    public DisbursementsAfterClosingDateMatcher() {
        super("disbursementsAfterClosingDate", "Disbursements after closing date");

        this.attributes = new ArrayList<>();
    }

    @Override
    public boolean match(AmpPerformanceRule rule, AmpActivityVersion a) {
        List<AmpFundingDetail> activityDisbursements = ActivityUtil.getTransactionsWithType(a, Constants.DISBURSEMENT);
        
        if (a.getActualCompletionDate() != null) {
            Date completionDate = a.getActualCompletionDate();
            
            boolean hasActivityDisbursementsAfterCompletionDate = activityDisbursements.stream()
                    .anyMatch(disb -> disb.getTransactionDate().after(completionDate));
            
            return hasActivityDisbursementsAfterCompletionDate;
        }

        return false;
    }
    
}
