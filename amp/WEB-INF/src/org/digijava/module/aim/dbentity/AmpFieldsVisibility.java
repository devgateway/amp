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
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.module.gateperm.core.GatePermConst;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
@Entity
@Table(name = "AMP_FIELDS_VISIBILITY")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "AMP_FIELDS_VISIBILITY_SEQ", sequenceName = "amp_fields_visibility_seq", allocationSize = 1)
public class AmpFieldsVisibility extends AmpObjectVisibility implements Serializable{
    
        
    private static final long serialVersionUID = 1255296454545642749L;

//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_FIELDS_VISIBILITY_SEQ")
//    @SequenceGenerator(name = "AMP_FIELDS_VISIBILITY_SEQ", sequenceName = "amp_fields_visibility_seq", allocationSize = 1)
//    @Column(name = "id")
//    private Long id;
//
//    @Column(name = "name")
//    private String name;

//    @Column(name = "description")
//    private String description;
//
//    @Column(name = "hasLevel")
//    private Boolean hasLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private AmpFeaturesVisibility parent;

    @ManyToMany
    @JoinTable(
            name = "amp_fields_templates",
            joinColumns = @JoinColumn(name = "field"),
            inverseJoinColumns = @JoinColumn(name = "template")
    )
    private Set<AmpTemplatesVisibility> templates;
    
    public String getVisible() {
        return templates.contains(super.parent.getParent().getParent())?"true":"false";
    }

    public AmpTemplatesVisibility getTemplate() {
        return super.parent.getTemplate();
    } 
    
    public boolean isVisibleTemplateObj(AmpTemplatesVisibility aObjVis){
        for (AmpFieldsVisibility x : aObjVis.getFields()) {
            if (x.getId().compareTo(id) == 0) return true;

        }
        return false;
    }
    
    public boolean isFieldActive(AmpTreeVisibility atv)
    {
        AmpTemplatesVisibility currentTemplate = (AmpTemplatesVisibility) atv.getRoot();
        
        return currentTemplate.fieldExists(this.getName());
    }

    
    @Override
    public Class getPermissibleCategory() {
        return AmpFieldsVisibility.class;
    }
    
    public String getClusterIdentifier() {
        return name;
    }

    public String toString()
    {
        return String.format("%s: %s", this.getName(), this.getVisible());
    }

//    @Override
//    public void setTemplates(Set<AmpTemplatesVisibility> templates) {
//        this.templates = templates;
//    }

//    public void setParent(AmpFeaturesVisibility parent) {
//        this.parent = parent;
//    }
}
