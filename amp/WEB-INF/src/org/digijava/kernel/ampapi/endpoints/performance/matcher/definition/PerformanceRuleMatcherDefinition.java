package org.digijava.kernel.ampapi.endpoints.performance.matcher.definition;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.PerformanceRuleMatcher;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;

/**
 * 
 * @author Viorel Chihai
 *
 */
public abstract class PerformanceRuleMatcherDefinition {

    protected String name;

    protected String description;
    
    @JsonIgnore 
    protected List<PerformanceRuleMatcherAttribute> attributes = new ArrayList<>();
    
    public PerformanceRuleMatcherDefinition(String name, String description) {
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
    
    public abstract PerformanceRuleMatcher createMatcher(AmpPerformanceRule rule);

}
