package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherDefinition;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherPossibleValuesSupplier;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;

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

    public abstract boolean match(AmpActivityVersion a);
    
    protected abstract boolean validate();
    
    public List<String> getPossibleValue(PerformanceRuleAttributeType type) {
        return PerformanceRuleMatcherPossibleValuesSupplier.getDefaultPerformanceRuleAttributePossibleValues(type);
    }
    
    public Date getFundingDate(AmpFunding f, String selectedFundingDate) {
        switch (selectedFundingDate) {
            case PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE:
                return f.getFundingClassificationDate();
            default:
                throw new IllegalArgumentException();
        }
    }
    
    public Date getActivityDate(AmpActivityVersion a, String selectedActivityDate) {
        switch (selectedActivityDate) {
            case PerformanceRuleConstants.ACTIVITY_CLOSING_DATE:
                return a.getActualCompletionDate();
            default:
                throw new IllegalArgumentException();
        }
    }

    public Date getDeadline(Date selectedDate, int timeUnit, int timeAmount) {
        Calendar c = Calendar.getInstance();
        c.setTime(selectedDate);
        c.add(timeUnit, timeAmount);

        return c.getTime();
    }

}
