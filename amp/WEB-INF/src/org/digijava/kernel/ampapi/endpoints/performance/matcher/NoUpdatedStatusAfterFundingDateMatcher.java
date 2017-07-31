package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.endpoints.performance.PerfomanceRuleManager;
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
    
    public NoUpdatedStatusAfterFundingDateMatcher(PerformanceRuleMatcherDefinition definition,
            AmpPerformanceRule rule) {

        super(definition, rule);
    }

    @Override
    public boolean match(AmpActivityVersion a) {
        PerfomanceRuleManager performanceRuleManager = PerfomanceRuleManager.getInstance();
        
        int timeUnit = performanceRuleManager.getCalendarTimeUnit(
                performanceRuleManager.getAttributeValue(rule, PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT));
        int timeAmount = Integer.parseInt(performanceRuleManager.getAttributeValue(rule, 
                PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT));
        String selectedDate = performanceRuleManager.getAttributeValue(rule,
                PerformanceRuleConstants.ATTRIBUTE_FUNDING_DATE);
        String selectedStatus = performanceRuleManager.getAttributeValue(rule, 
                PerformanceRuleConstants.ATTRIBUTE_ACTIVITY_STATUS);
        
        AmpCategoryValue activityStatus = CategoryManagerUtil
                .getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY, a.getCategories());
        
        if (activityStatus == null) {
            return true;
        }
        
        Date tmpDeadlineDate = new Date(0);
        for (AmpFunding f : a.getFunding()) {
            Date fundingSelectedDate = getFundingDate(f, selectedDate);
            if (fundingSelectedDate != null && StringUtils.isNotBlank(selectedStatus)) {
                Date deadline = getDeadline(fundingSelectedDate, timeUnit, timeAmount);
                
                if (deadline.after(tmpDeadlineDate)) {
                    tmpDeadlineDate = deadline;
                }
            }
        }
        
        Date currentDate = new Date();
        
        if (currentDate.after(tmpDeadlineDate)) {
            return !selectedStatus.equals(activityStatus.getLabel());
        }

        return false;
    }
    
    @Override
    public boolean validate() {
        PerfomanceRuleManager performanceRuleManager = PerfomanceRuleManager.getInstance();
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
