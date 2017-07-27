package org.digijava.kernel.ampapi.endpoints.performance.matchers;

import java.util.Calendar;
import java.util.Date;

import org.digijava.kernel.ampapi.endpoints.performance.PerfomanceRuleManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class NoUpdatedStatusAfterSignatureDatesMatcher extends PerformanceRuleMatcher {
    
    public static final String ATTRIBUTE_MONTH = "month";

    public NoUpdatedStatusAfterSignatureDatesMatcher() {
        super("noUpdatedStatusAfterSignatureDate", "No updated status after signature date");

        this.attributes.add(new PerformanceRuleMatcherAttribute(ATTRIBUTE_MONTH, "No updated status after x months",
                AmpPerformanceRuleAttribute.PerformanceRuleAttributeType.INTEGER));
    }

    @Override
    public boolean match(AmpPerformanceRule rule, AmpActivityVersion a) {
        
        PerfomanceRuleManager performanceRuleManager = PerfomanceRuleManager.getInstance();
        AmpPerformanceRuleAttribute monthAttribute = performanceRuleManager.getAttributeFromRule(rule, ATTRIBUTE_MONTH);
        
        if (monthAttribute != null && a.getActualApprovalDate() != null) {
            Date currentDate = new Date();
            Date deadline = getDeadline(a, monthAttribute);
            
            AmpCategoryValue activityStatus = CategoryManagerUtil
                    .getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY, a.getCategories());
            
            boolean statusIsNotUpdatedToOngoing = currentDate.after(deadline) 
                    && activityStatus != null && !Constants.ACTIVITY_STATUS_ONGOING.equals(activityStatus.getLabel());
            
            return statusIsNotUpdatedToOngoing;
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
        c.add(Calendar.MONTH, month);

        return c.getTime();
    }
}
