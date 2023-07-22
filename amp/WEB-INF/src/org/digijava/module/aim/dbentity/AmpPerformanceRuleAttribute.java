package org.digijava.module.aim.dbentity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @author Viorel Chihai
 *
 */
import javax.persistence.*;

import javax.persistence.*;

@Entity
@Table(name = "AMP_PERFORMANCE_RULE_ATTRIBUTE")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpPerformanceRuleAttribute {
    @Id
    @JsonIgnore

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_PERFORMANCE_RULE_ATTRIBUTE_seq")
    @SequenceGenerator(name = "AMP_PERFORMANCE_RULE_ATTRIBUTE_seq", sequenceName = "AMP_PERFORMANCE_RULE_ATTRIBUTE_seq", allocationSize = 1)
    @Column(name = "rule_attribute_id")
    private Long id;

    @Column(name = "attribute_name", nullable = false)
    @ApiModelProperty(example = "timeAmount")

    private String name;

    @Column(name = "attribute_value", nullable = false)
    private String value;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(example = "AMOUNT")

    @Column(name = "type")
    private PerformanceRuleAttributeType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference

    @JoinColumn(name = "rule_id", referencedColumnName = "rule_id", nullable = false)
    private AmpPerformanceRule rule;

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
