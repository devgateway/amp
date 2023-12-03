package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import org.digijava.kernel.ampapi.endpoints.performance.PerformanceIssue;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleAttributeOption;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherDefinition;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherPossibleValuesSupplier;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author Viorel Chihai
 *
 */
public abstract class PerformanceRuleMatcher {
    
    protected PerformanceRuleMatcherDefinition definition;

    protected AmpPerformanceRule rule;
    
    public PerformanceRuleMatcher(PerformanceRuleMatcherDefinition definition, AmpPerformanceRule rule) {
        super();
        this.definition = definition;
        this.rule = rule;
        validate();
    }
    
    public AmpPerformanceRule getRule() {
        return rule;
    }

    public abstract PerformanceIssue findPerformanceIssue(AmpActivityVersion a);
    
    protected abstract boolean validate();
    
    public List<PerformanceRuleAttributeOption> getPossibleValue(PerformanceRuleAttributeType type) {
        return PerformanceRuleMatcherPossibleValuesSupplier.getDefaultPerformanceRuleAttributePossibleValues(type);
    }
    
    public Date getFundingDate(AmpFunding f, String selectedFundingDate) {
        switch (selectedFundingDate) {
            case PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE:
                return f.getFundingClassificationDate();
            case PerformanceRuleConstants.FUNDING_EFFECTIVE_DATE:
                return f.getEffectiveFundingDate();
            case PerformanceRuleConstants.FUNDING_CLOSING_DATE:
                return f.getFundingClosingDate();
            default:
                throw new IllegalArgumentException();
        }
    }
    
    public Date getActivityDate(AmpActivityVersion a, String selectedActivityDate) {
        switch (selectedActivityDate) {
            case PerformanceRuleConstants.ACTIVITY_COMPLETION_DATE:
                return a.getActualCompletionDate();
            case PerformanceRuleConstants.ACTIVITY_ACTUAL_APPROVAL_DATE:
                return a.getActualApprovalDate();
            case PerformanceRuleConstants.ACTIVITY_ACTUAL_START_DATE:
                return a.getActualStartDate();
            case PerformanceRuleConstants.ACTIVITY_CONTRACTING_DATE:
                return a.getContractingDate();
            case PerformanceRuleConstants.ACTIVITY_DISBURSEMENTS_DATE:
                return a.getDisbursmentsDate();
            case PerformanceRuleConstants.ACTIVITY_ORIGINAL_COMPLETING_DATE:
                return a.getOriginalCompDate();
            case PerformanceRuleConstants.ACTIVITY_PROPOSED_START_DATE:
                return a.getProposedStartDate();
            default:
                throw new IllegalArgumentException();
        }
    }
    
    public PerformanceRuleMatcherDefinition getDefinition() {
        return definition;
    };
    
    public Date getDeadline(Date selectedDate, int timeUnit, int timeAmount) {
        Calendar c = Calendar.getInstance();
        c.setTime(selectedDate);
        c.add(timeUnit, timeAmount);

        return c.getTime();
    }

}
