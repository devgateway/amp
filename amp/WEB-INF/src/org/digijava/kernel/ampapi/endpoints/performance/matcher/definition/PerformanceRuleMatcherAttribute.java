package org.digijava.kernel.ampapi.endpoints.performance.matcher.definition;

import java.util.List;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
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

    @JsonProperty(PerformanceRuleConstants.JSON_ATTRIBUTE_POSSIBLE_VALUES)
    private List<PerformanceRuleAttributeOption> possibleValues;

    public PerformanceRuleMatcherAttribute(String name, String description, PerformanceRuleAttributeType type,
            Function<PerformanceRuleAttributeType, List<PerformanceRuleAttributeOption>> possibleValuesSupplier) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.possibleValues = possibleValuesSupplier.apply(type);
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

    public List<PerformanceRuleAttributeOption> getPossibleValues() {
        return possibleValues;
    }

    public void setPossibleValues(List<PerformanceRuleAttributeOption> possibleValues) {
        this.possibleValues = possibleValues;
    }

}
