package org.digijava.module.aim.dbentity;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class AmpPerformanceRuleAttribute {

    @JsonIgnore
    private Long id;

    @JsonBackReference
    private AmpPerformanceRule rule;

    private String name;

    private String value;

    private PerformanceRuleAttributeType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpPerformanceRule getRule() {
        return rule;
    }

    public void setRule(AmpPerformanceRule rule) {
        this.rule = rule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PerformanceRuleAttributeType getType() {
        return type;
    }

    public void setType(PerformanceRuleAttributeType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public enum PerformanceRuleAttributeType {
        DECIMAL, INTEGER, STRING, DATE
    }

}
