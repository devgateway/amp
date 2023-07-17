package org.digijava.module.aim.dbentity;

import java.util.Set;

/**
 * 
 * modified by dan
 *
 */
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AMP_FEATURE")
public class AmpFeature {
    @Id
    @GeneratedValue(generator = "ampFeatureSeq")
    @SequenceGenerator(name = "ampFeatureSeq", sequenceName = "AMP_FEATURE_seq", allocationSize = 1)
    @Column(name = "amp_feature_id")
    private Integer ampFeatureId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "code")
    private String code;

    @Column(name = "active")
    private Boolean active;

    @ManyToMany(mappedBy = "features")
    private Set<FeatureTemplates> templates = new HashSet<>();
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
