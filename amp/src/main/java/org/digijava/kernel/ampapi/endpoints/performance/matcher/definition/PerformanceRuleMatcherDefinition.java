package org.digijava.kernel.ampapi.endpoints.performance.matcher.definition;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.PerformanceRuleMatcher;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;

/**
 * 
 * @author Viorel Chihai
 *
 */
public abstract class PerformanceRuleMatcherDefinition {

    @ApiModelProperty(example = "noUpdatedStatusAfterFundingDate")
    protected String name;

    @ApiModelProperty(example = "No updated status after selected funding date")
    protected String description;

    @ApiModelProperty(example = "{timeAmount} {timeUnit} went by after the '{fundingDate}' and the project status was "
            + "not modified to '{activityStatus}'")
    protected String message;
    
    @JsonIgnore
    protected List<PerformanceRuleMatcherAttribute> attributes = new ArrayList<>();
    
    public PerformanceRuleMatcherDefinition(String name, String description, String message) {
        this.name = name;
        this.description = description;
        this.message = message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PerformanceRuleMatcherAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<PerformanceRuleMatcherAttribute> attributes) {
        this.attributes = attributes;
    }
    
    public abstract PerformanceRuleMatcher createMatcher(AmpPerformanceRule rule);

}
