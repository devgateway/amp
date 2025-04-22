package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceIssue;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleManager;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherDefinition;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
    public PerformanceIssue findPerformanceIssue(AmpActivityVersion a) {
        AmpCategoryValue activityStatus = CategoryManagerUtil
                .getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY, a.getCategories());
        
        Set<AmpOrganisation> donorsWithIssues = new HashSet<>();
        
        Date currentDate = new Date();
        
        for (AmpFunding f : a.getFunding()) {
            Date fundingSelectedDate = getFundingDate(f, selectedDate);
            if (fundingSelectedDate != null && StringUtils.isNotBlank(selectedStatus)) {
                Date fundingDeadline = getDeadline(fundingSelectedDate, timeUnit, timeAmount);
                
                if (fundingDeadline.before(currentDate)) {
                    donorsWithIssues.add(f.getAmpDonorOrgId());
                }
            }
        }
        
        if (!donorsWithIssues.isEmpty()) {
            if (activityStatus == null || !selectedStatus.equals(activityStatus.getLabel())) {
                return new PerformanceIssue(this, new ArrayList<>(donorsWithIssues));
            }
        }
        
        return null; 
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
