package org.digijava.kernel.ampapi.endpoints.performance.matchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class DisbursementsAfterClosingDateMatcher extends PerformanceRuleMatcher {
    
    public DisbursementsAfterClosingDateMatcher() {
        super("disbursementsAfterClosingDate", "Disbursements after closing Date");

        this.attributes = new ArrayList<>();
    }

    @Override
    public AmpCategoryValue match(AmpPerformanceRule rule, AmpActivityVersion a) {
        List<AmpFundingDetail> activityDisbursements = ActivityUtil.getTransactionsByType(a, Constants.DISBURSEMENT);
        
        if (a.getActualCompletionDate() != null) {
            Date completionDate = a.getActualCompletionDate();
            
            List<AmpFundingDetail> disbursementsAfterCompletionDate = activityDisbursements.stream()
                    .filter(disb -> disb.getTransactionDate().after(completionDate))
                    .collect(Collectors.toList());
            
            if (!disbursementsAfterCompletionDate.isEmpty()) {
                return rule.getLevel();
            }
        }

        return null;
    }

}
