package org.digijava.kernel.ampapi.endpoints.performance.matchers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.digijava.kernel.ampapi.endpoints.performance.PerfomanceRuleManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ActivityUtil;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class NoUpdatedDisbursmentsMatcher extends PerformanceRuleMatcher {
    
    public static final String ATTRIBUTE_MONTH = "month";

    public NoUpdatedDisbursmentsMatcher() {
        super("NoUpdatedDisbursments", "No updated disbursments in the last months");

        this.attributes.add(new PerformanceRuleMatcherAttribute(ATTRIBUTE_MONTH, 
                "No updated disbursements in the last selected months",
                AmpPerformanceRuleAttribute.PerformanceRuleAttributeType.INTEGER));
    }

    @Override
    public boolean match(AmpPerformanceRule rule, AmpActivityVersion a) {
        List<AmpFundingDetail> activityDisbursements = ActivityUtil.getTransactionsWithType(a, Constants.DISBURSEMENT);
        
        PerfomanceRuleManager performanceRuleManager = PerfomanceRuleManager.getInstance();
        AmpPerformanceRuleAttribute monthAttribute = performanceRuleManager.getAttributeFromRule(rule, ATTRIBUTE_MONTH);
        
        if (monthAttribute != null && a.getApprovalDate() != null) {
            Date deadline = getDeadline(a, monthAttribute);
            
            boolean hasActivityDisbursementsAfterSignatureDate = activityDisbursements.stream()
                    .anyMatch(disb -> disb.getTransactionDate().after(deadline));
            
            return hasActivityDisbursementsAfterSignatureDate;
        }

        return false;
    }
    
    /**
     * @param a
     * @param monthAttribute
     * @return deadline
     */
    public Date getDeadline(AmpActivityVersion a, AmpPerformanceRuleAttribute monthAttribute) {
        int month = Integer.parseInt(monthAttribute.getValue());

        Calendar c = Calendar.getInstance();
        c.setTime(a.getApprovalDate());
        c.add(Calendar.MONTH, -month);

        return c.getTime();
    }
}
