/*
* AMP FEATURE TEMPLATES
*/
/**
 * @author dan
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

public class FeatureTemplates implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -1080500678950534107L;
    private Long templateId;
    private String featureTemplateName;
    private Set features;
    
    public Set getFeatures() {
        return features;
    }
    public void setFeatures(Set features) {
        this.features = features;
    }
    /**
     * @return Returns the globalSettingsName.
     */
    public Long getTemplateId() {
        return templateId;
    }
    public void setTemplateId(Long featureId) {
        this.templateId = featureId;
    }
    public String getFeatureTemplateName() {
        return featureTemplateName;
    }
    public void setFeatureTemplateName(String featureTemplateName) {
        this.featureTemplateName = featureTemplateName;
    }
    
}
