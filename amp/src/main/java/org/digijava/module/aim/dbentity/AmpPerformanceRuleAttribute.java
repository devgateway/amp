package org.digijava.module.aim.dbentity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

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

    @ApiModelProperty(example = "timeAmount")
    private String name;

    @ApiModelProperty(example = "20")
    private String value;

    @ApiModelProperty(example = "AMOUNT")
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
        AMOUNT,
        TIME_UNIT,
        ACTIVITY_STATUS,
        ACTIVITY_DATE,
        FUNDING_DATE
    }

}
