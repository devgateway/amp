/**
 * 
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

import org.dgfoundation.amp.visibility.AmpVisibilityRuleType;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AMP_VISIBILITY_RULE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
/**
 * Defines visibility rule of a given type (ANY/ALL) that can consist of other rules or fields, features, modules, etc  
 * @author Nadejda Mandrescu
 */
public class AmpVisibilityRule implements Serializable {

     @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_visibility_rule_seq_gen")
    @SequenceGenerator(name = "amp_visibility_rule_seq_gen", sequenceName = "AMP_VISIBILITY_RULE_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_amp_visibility_rule_parent"))
    private AmpVisibilityRule parent;

    @Enumerated(EnumType.STRING)
    @Column(name = "RULE_TYPE")
    private AmpVisibilityRuleType type;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<AmpVisibilityRule> children = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "AMP_VISIBILITY_RULE_AMP_FIELDS_VISIBILITY",
            joinColumns = @JoinColumn(name = "RULE_ID"),
            inverseJoinColumns = @JoinColumn(name = "FIELD_ID"))
    private Set<AmpFieldsVisibility> fields = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "AMP_VISIBILITY_RULE_AMP_FEATURES_VISIBILITY",
            joinColumns = @JoinColumn(name = "RULE_ID"),
            inverseJoinColumns = @JoinColumn(name = "FEATURE_ID"))
    private Set<AmpFeaturesVisibility> features = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "AMP_VISIBILITY_RULE_AMP_MODULES_VISIBILITY",
            joinColumns = @JoinColumn(name = "RULE_ID"),
            inverseJoinColumns = @JoinColumn(name = "MODULE_ID"))
    private Set<AmpModulesVisibility> modules = new HashSet<>();

    
    /* any other dependency can be defined here, 
     * but appropriate logic should be added for it to process the overall rule status
     * */
    
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * @return the parent
     */
    public AmpVisibilityRule getParent() {
        return parent;
    }
    
    /**
     * @param parent the parent to set
     */
    public void setParent(AmpVisibilityRule parent) {
        this.parent = parent;
    }
    
    /**
     * @return the children
     */
    public Set<AmpVisibilityRule> getChildren() {
        return children;
    }
    
    /**
     * @param children the children to set
     */
    public void setChildren(Set<AmpVisibilityRule> children) {
        this.children = children;
    }
    
    /**
     * @return the type
     */
    public AmpVisibilityRuleType getType() {
        return type;
    }
    
    /**
     * @param type the type to set
     */
    public void setType(AmpVisibilityRuleType type) {
        this.type = type;
    }
    
    /**
     * @return the fields
     */
    public Set<AmpFieldsVisibility> getFields() {
        return fields;
    }
    
    /**
     * @param fields the fields to set
     */
    public void setFields(Set<AmpFieldsVisibility> fields) {
        this.fields = fields;
    }
    
    /**
     * @return the features
     */
    public Set<AmpFeaturesVisibility> getFeatures() {
        return features;
    }
    
    /**
     * @param features the features to set
     */
    public void setFeatures(Set<AmpFeaturesVisibility> features) {
        this.features = features;
    }
    
    /**
     * @return the modules
     */
    public Set<AmpModulesVisibility> getModules() {
        return modules;
    }
    
    /**
     * @param modules the modules to set
     */
    public void setModules(Set<AmpModulesVisibility> modules) {
        this.modules = modules;
    }
    
    @Override
    public String toString() {
        return String.format("[id: %d, type: %d, modules: %s, features: %s, fields: %s, children: %s]", this.id, this.type, this.modules, this.features, this.fields, this.children);
    }
}
