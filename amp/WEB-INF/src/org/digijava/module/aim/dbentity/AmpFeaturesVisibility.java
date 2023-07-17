/*
* AMP FEATURE TEMPLATES
*/
/**
 * @author dan
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "AMP_FEATURES_VISIBILITY")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpFeaturesVisibility extends AmpObjectVisibility implements Serializable {

    private static final long serialVersionUID = 7004856623866175824L;


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_FEATURES_VISIBILITY_SEQ")
    @SequenceGenerator(name = "AMP_FEATURES_VISIBILITY_SEQ", sequenceName = "amp_features_visibility_seq",allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "hasLevel")
    private Boolean hasLevel;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<AmpFieldsVisibility> items;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private AmpModulesVisibility parent;

    @ManyToMany(mappedBy = "features", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<AmpTemplatesVisibility> templates;


    public AmpObjectVisibility getParent() {
        return (AmpModulesVisibility)parent;
    }

    public String getVisible() {
        return templates.contains(parent.getParent())?"true":"false";
    }
    
    public AmpTemplatesVisibility getTemplate() {
        return parent.getTemplate();
    } 
    
    public boolean isVisibleTemplateObj(AmpTemplatesVisibility aObjVis){
        for(AmpFeaturesVisibility x:aObjVis.getFeatures())
        {
            if (x.getId().compareTo(id) == 0) 
                return true;            
        }
        return false;
    }
    @Override
    public Class getPermissibleCategory() {
        return AmpFeaturesVisibility.class;
        
    }
    
    
    
}
