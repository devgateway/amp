package org.digijava.module.aim.dbentity;

import java.util.Set;

/**
 * 
 * modified by dan
 *
 */
public class AmpFeature {
    
    private Integer ampFeatureId;
    private String name;
    private String description;
    private String code;
    private boolean active;
    private Set templates;
    private String nameTrimmed;
    
    /**
     * @return Returns the ampFeatureId.
     */
    public Integer getAmpFeatureId() {
        return ampFeatureId;
    }
    /**
     * @param ampFeatureId The ampFeatureId to set.
     */
    public void setAmpFeatureId(Integer ampFeatureId) {
        this.ampFeatureId = ampFeatureId;
    }
    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code The code to set.
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public Set getTemplates() {
        return templates;
    }
    public void setTemplates(Set templates) {
        this.templates = templates;
    }
    public String getNameTrimmed() {
        return this.name.replaceAll(" ","");
    }
    public void setNameTrimmed(String nameTrimmed) {
        this.nameTrimmed = nameTrimmed;
    }
    
    
}
