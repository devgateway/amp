/**
 * 
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

import org.dgfoundation.amp.visibility.AmpVisibilityRuleType;

/**
 * Defines visibility rule of a given type (ANY/ALL) that can consist of other rules or fields, features, modules, etc  
 * @author Nadejda Mandrescu
 */
public class AmpVisibilityRule implements Serializable {
    private Long id;
    private AmpVisibilityRule parent;
    /** A set of children rules*/
    private Set<AmpVisibilityRule> children;
    
    private AmpVisibilityRuleType type;
    
    /**
     * Only leaf child rule can have actual visibility rules. 
     * It is not allowed to mix children rules with actual visibility rules. 
     */
    /** */
    private Set<AmpFieldsVisibility> fields;
    private Set<AmpFeaturesVisibility> features;
    private Set<AmpModulesVisibility> modules;
    
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
