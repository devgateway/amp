/*
* AMP FEATURE TEMPLATES
*/
/**
 * @author dan
 */
package org.digijava.module.aim.dbentity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AMP_FEATURE_TEMPLATES")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FeatureTemplates implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -1080500678950534107L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_feature_templates_seq_gen")
    @SequenceGenerator(name = "amp_feature_templates_seq_gen", sequenceName = "amp_feature_templates_seq", allocationSize = 1)
    @Column(name = "id")
    private Long templateId;

    @Column(name = "featureTemplateName")
    private String featureTemplateName;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "amp_template_features",
            joinColumns = @JoinColumn(name = "templateId"),
            inverseJoinColumns = @JoinColumn(name = "featureId"))
    private Set<AmpFeature> features = new HashSet<>();

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
