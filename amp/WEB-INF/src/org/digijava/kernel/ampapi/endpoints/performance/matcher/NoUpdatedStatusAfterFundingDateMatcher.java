package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleManager;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherDefinition;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 *  The matcher checks if the activity is in a certain status after certain period has passed 
 *  from the selected funding date
 * 
 * @author Viorel Chihai
 *
 */
public class NoUpdatedStatusAfterFundingDateMatcher extends PerformanceRuleMatcher {
    
    private int timeUnit;
    private int timeAmount;
    private String selectedDate;
    private String selectedStatus;
    
    public NoUpdatedStatusAfterFundingDateMatcher(PerformanceRuleMatcherDefinition definition,
            AmpPerformanceRule rule) {
        super(definition, rule);
        
        PerformanceRuleManager performanceRuleManager = PerformanceRuleManager.getInstance();
        
        timeUnit = performanceRuleManager.getCalendarTimeUnit(
                performanceRuleManager.getAttributeValue(rule, PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT));
        timeAmount = Integer.parseInt(performanceRuleManager.getAttributeValue(rule, 
                PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT));
        selectedDate = performanceRuleManager.getAttributeValue(rule,
                PerformanceRuleConstants.ATTRIBUTE_FUNDING_DATE);
        selectedStatus = performanceRuleManager.getAttributeValue(rule, 
                PerformanceRuleConstants.ATTRIBUTE_ACTIVITY_STATUS);
    }

    @Override
    public boolean match(AmpActivityVersion a) {
        AmpCategoryValue activityStatus = CategoryManagerUtil
                .getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY, a.getCategories());
        
        if (activityStatus == null) {
            return true;
        }
        Date currentDate = new Date();
        
        Date tmpDeadlineDate = new Date(0);
        boolean tmpDeadlineUpdated = false;
        for (AmpFunding f : a.getFunding()) {
            Date fundingSelectedDate = getFundingDate(f, selectedDate);
            if (fundingSelectedDate != null && StringUtils.isNotBlank(selectedStatus)) {
                Date deadline = getDeadline(fundingSelectedDate, timeUnit, timeAmount);
                
                if (deadline.after(tmpDeadlineDate) && deadline.before(currentDate)) {
                    tmpDeadlineDate = deadline;
                    tmpDeadlineUpdated = true;
                }
            }
        }
        
        return tmpDeadlineUpdated && !selectedStatus.equals(activityStatus.getLabel());
    }
    
    @Override
    protected boolean validate() {
        PerformanceRuleManager performanceRuleManager = PerformanceRuleManager.getInstance();
        AmpPerformanceRuleAttribute attr1 = performanceRuleManager
                .getAttributeFromRule(rule, PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT);
        AmpPerformanceRuleAttribute attr2 = performanceRuleManager
                .getAttributeFromRule(rule, PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT);
        AmpPerformanceRuleAttribute attr3 = performanceRuleManager
                .getAttributeFromRule(rule, PerformanceRuleConstants.ATTRIBUTE_FUNDING_DATE);
        AmpPerformanceRuleAttribute attr4 = performanceRuleManager
                .getAttributeFromRule(rule, PerformanceRuleConstants.ATTRIBUTE_ACTIVITY_STATUS);

        if (attr1 == null || attr2 == null || attr3 == null || attr4 == null) {
            throw new IllegalArgumentException();
        }
        
        return true;
    }
    
}
