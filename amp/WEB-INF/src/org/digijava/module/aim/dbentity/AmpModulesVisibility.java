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
import java.util.TreeSet;

import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "AMP_MODULES_VISIBILITY")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "AMP_MODULES_VISIBILITY_SEQ", sequenceName = "amp_modules_visibility_seq", allocationSize = 1)
public class AmpModulesVisibility extends AmpObjectVisibility implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 292612393819900427L;

//    @Override
//    public Long getId() {
//        return super.id;
//    }
//
//    @Override
//    public void setId(Long id) {
//        this.id = id;
//    }

//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_MODULES_VISIBILITY_SEQ")
//    @SequenceGenerator(name = "AMP_MODULES_VISIBILITY_SEQ", sequenceName = "amp_modules_visibility_seq", allocationSize = 1)
//    @Column(name = "id")
//    private Long id;

//    @Column(name = "name", unique = true)
//    private String name;

//    @Column(name = "description")
//    private String description;

//    @Column(name = "hasLevel")
//    private Boolean hasLevel;

//    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<AmpFeaturesVisibility> items;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private AmpModulesVisibility parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<AmpModulesVisibility> submodules;

    @ManyToMany
    @JoinTable(
            name = "amp_modules_templates",
            joinColumns = @JoinColumn(name = "module"),
            inverseJoinColumns = @JoinColumn(name = "template")
    )
    private Set<AmpTemplatesVisibility> templates;


    /**
     * @see org.dgfoundation.amp.visibility.AmpObjectVisibility#getParent()
     */
    public AmpObjectVisibility getParent() {
        return super.parent;
    }

    /**
     * @see org.dgfoundation.amp.visibility.AmpObjectVisibility#getVisible()
     */
    public String getVisible() {
        return getTemplate().getItems().contains(this) ? "true" : "false";
    }

    /**
     * @param id
     * @return
     * @deprecated does not provide the latest state when outside Admin session, use FeaturesUtil
     */
    public boolean isVisibleId(Long id) {
        for (AmpTemplatesVisibility x : this.getTemplates()) {
            if (x.getId().compareTo(id) == 0)
                return true;

        }
        return false;
    }

    /**
     * @param aObjVis
     * @return
     * @deprecated does not provide the latest state when outside Admin session, use FeaturesUtil
     */
    public boolean isVisibleTemplateObj(AmpTemplatesVisibility aObjVis) {
        for (AmpObjectVisibility ampObjectVisibility : aObjVis.getItems()) {
            AmpModulesVisibility x = (AmpModulesVisibility) ampObjectVisibility;
            if (x.getId().compareTo(id) == 0)
                return true;

        }
        return false;
    }

    /**
     * @see org.dgfoundation.amp.visibility.AmpObjectVisibility#getTemplate()
     */
    public AmpTemplatesVisibility getTemplate() {
        return parent.getTemplate();
    }

    /**
     * @return
     */
    public TreeSet getSortedAlphaSubModules() {
        TreeSet mySet = new TreeSet(FeaturesUtil.ALPHA_ORDER);
        mySet.addAll(submodules);
        return mySet;
    }

    /**
     * @return
     */
    public Set<AmpModulesVisibility> getSubmodules() {
        return submodules;
    }

    /**
     * @param submodules
     */
    public void setSubmodules(Set<AmpModulesVisibility> submodules) {
        this.submodules = submodules;
    }


    /**
     * @see org.digijava.module.gateperm.core.Permissible#getPermissibleCategory()
     */
    @Override
    public Class getPermissibleCategory() {
        return AmpModulesVisibility.class;
    }

//    @Override
//    public String getName() {
//        return this.name;
//    }
//
//    @Override
//    public void setName(String name) {
//        this.name = name;
//    }

//    @Override
//    public String getDescription() {
//        return description;
//    }
//
//    @Override
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    @Override
//    public Boolean getHasLevel() {
//        return hasLevel;
//    }

//    @Override
//    public void setHasLevel(Boolean hasLevel) {
//        this.hasLevel = hasLevel;
//    }


}
