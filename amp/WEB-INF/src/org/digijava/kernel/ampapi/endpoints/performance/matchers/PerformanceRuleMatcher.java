package org.digijava.kernel.ampapi.endpoints.performance.matchers;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class PerformanceRuleMatcher {

    protected String name;

    protected String description;
    
    @JsonIgnore
    protected List<PerformanceRuleMatcherAttribute> attributes;
    
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
}
