package org.digijava.kernel.ampapi.endpoints.performance.matchers;

import java.util.ArrayList;
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

        this.attributes = new ArrayList<>();
        this.attributes.add(new PerformanceRuleMatcherAttribute(ATTRIBUTE_MONTH, "No updated status after x months",
                AmpPerformanceRuleAttribute.PerformanceRuleAttributeType.INTEGER));
    }

    @Override
    public boolean match(AmpPerformanceRule rule, AmpActivityVersion a) {
        
        PerfomanceRuleManager performanceRuleManager = PerfomanceRuleManager.getInstance();
        AmpPerformanceRuleAttribute monthAttribute = performanceRuleManager.getAttributeFromRule(rule, ATTRIBUTE_MONTH);
        
        if (monthAttribute != null && a.getActualApprovalDate() != null) {
            Date signatureDate = a.getActualApprovalDate();
            Date currentDate = new Date();
            
            Calendar c = Calendar.getInstance();
            c.setTime(signatureDate);
           
            int month = Integer.parseInt(monthAttribute.getValue());
            c.add(Calendar.MONTH, month);
            
            AmpCategoryValue activityStatus = CategoryManagerUtil
                    .getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY, a.getCategories());
            
            boolean statusIsNotUpdatedToOngoing = currentDate.after(c.getTime()) 
                    && activityStatus != null && !Constants.ACTIVITY_STATUS_ONGOING.equals(activityStatus.getLabel());
            
            return statusIsNotUpdatedToOngoing;
        }

        return false;
    }
}
