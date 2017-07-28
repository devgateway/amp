package org.digijava.kernel.ampapi.endpoints.performance.matchers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;

/**
 * 
 * @author Viorel Chihai
 *
 */
public abstract class PerformanceRuleMatcher {

    protected String name;

    protected String description;
    
    @JsonIgnore
    protected List<PerformanceRuleMatcherAttribute> attributes = new ArrayList<>();
    
    public PerformanceRuleMatcher(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PerformanceRuleMatcherAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<PerformanceRuleMatcherAttribute> attributes) {
        this.attributes = attributes;
    }
    
    public abstract boolean match(AmpPerformanceRule rule, AmpActivityVersion a);
    
    public Date getFundingDate(AmpFunding f, String selectedFundingDate) {
        switch (selectedFundingDate) {
            case PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE :
                return f.getFundingClassificationDate();
            default :
                return null;
        }
    }
    
    public Date getActivityDate(AmpActivityVersion a, String selectedActivityDate) {
        switch (selectedActivityDate) {
            case PerformanceRuleConstants.ACTIVITY_CLOSING_DATE :
                return a.getActualCompletionDate();
            default :
                return null;
        }
    }

    public Date getDeadline(Date selectedDate, int timeUnit, int timeAmount) {
        Calendar c = Calendar.getInstance();
        c.setTime(selectedDate);
        c.add(timeUnit, timeAmount);

        return c.getTime();
    }

}
