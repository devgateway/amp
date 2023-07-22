package org.digijava.module.aim.dbentity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.performance.AmpCategoryValueSerializer;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * 
 * @author Viorel Chihai
 *
 */
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AMP_PERFORMANCE_RULE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AmpPerformanceRule {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_PERFORMANCE_RULE_seq")
    @SequenceGenerator(name = "AMP_PERFORMANCE_RULE_seq", sequenceName = "AMP_PERFORMANCE_RULE_seq", allocationSize = 1)
    @Column(name = "rule_id")
    private Long id;

    @Column(name = "rule_name", nullable = false)
    @ApiModelProperty(example = "No disbursements")

    private String name;

    @Column(name = "rule_description")
    private String description;

    @Column(name = "rule_type_class_name", nullable = false)
    @JsonProperty(PerformanceRuleConstants.JSON_ATTRIBUTE_TYPE_CLASS_NAME)

    private String typeClassName;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_category", referencedColumnName = "id", nullable = false)
    @JsonSerialize(using = AmpCategoryValueSerializer.class)

    private AmpCategoryValue level;

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<AmpPerformanceRuleAttribute> attributes = new HashSet<>();




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTypeClassName() {
        return typeClassName;
    }

    public void setTypeClassName(String typeClassName) {
        this.typeClassName = typeClassName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public AmpCategoryValue getLevel() {
        return level;
    }

    public void setLevel(AmpCategoryValue level) {
        this.level = level;
    }

    public Set<AmpPerformanceRuleAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<AmpPerformanceRuleAttribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "PerformanceRule [id=" + id + ", name=" + name + ", typeClassName=" + typeClassName + ", enabled="
                + enabled + ", level=" + level + "]";
    }
}
