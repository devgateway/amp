package org.digijava.kernel.ampapi.endpoints.performance.matchers;

import java.util.List;

import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class PerformanceRuleMatcherAttribute {

    private String name;
    
    private String description;
    
    private AmpPerformanceRuleAttribute.PerformanceRuleAttributeType type;
    
    private List<String> possibleValues;

    public PerformanceRuleMatcherAttribute(String name, String description, PerformanceRuleAttributeType type) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.possibleValues = PerformanceRuleMatchers.getPerformanceRuleAttributePossibleValues(type);
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

    public AmpPerformanceRuleAttribute.PerformanceRuleAttributeType getType() {
        return type;
    }

    public void setType(AmpPerformanceRuleAttribute.PerformanceRuleAttributeType type) {
        this.type = type;
    }

    public List<String> getPossibleValues() {
        return possibleValues;
    }

    public void setPossibleValues(List<String> possibleValues) {
        this.possibleValues = possibleValues;
    }
    
}
