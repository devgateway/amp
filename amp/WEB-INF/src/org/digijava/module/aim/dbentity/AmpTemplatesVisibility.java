/*
* AMP FEATURE TEMPLATES
*/
/**
 * @author dan
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.*;

import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "AMP_TEMPLATES_VISIBILITY")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpTemplatesVisibility extends AmpObjectVisibility implements Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_TEMPLATES_VISIBILITY_SEQ")
    @SequenceGenerator(name = "AMP_TEMPLATES_VISIBILITY_SEQ", sequenceName = "amp_templates_visibility_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "visible")
    private String visible;

    @ManyToMany(mappedBy = "templates")
    private Set<AmpModulesVisibility> items;

    @ManyToMany
    @JoinTable(
            name = "amp_features_templates",
            joinColumns = @JoinColumn(name = "template"),
            inverseJoinColumns = @JoinColumn(name = "feature")
    )
    private Set<AmpFeaturesVisibility> features;

    @ManyToMany(mappedBy = "templates")
    private Set<AmpFieldsVisibility> fields;
    private static final long serialVersionUID = -4765301740400470276L;


    @Transient
    //Non persistent
    private List<String> usedByTeamsNames;

    public Set<AmpFeaturesVisibility> getFeatures() {
        return features;
    }

    public void setFeatures(Set<AmpFeaturesVisibility> features) {
        this.features = features;
    }

    public Set<AmpFieldsVisibility> getFields() {
        return fields;
    }

    public void setFields(Set<AmpFieldsVisibility> fields)
    {
        namesCache = null;
        this.fields = fields;
    }

    public AmpObjectVisibility getParent() {
        // TODO Auto-generated method stub
        //if(getVisible()) return this;
        return this;
    }

    public String getVisible() {
            return visible;
    }

    public AmpTemplatesVisibility getTemplate() {
        return this;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }
    
    public boolean isDefault() {
        return FeaturesUtil.getDefaultAmpTemplateVisibility().getId().equals(this.getId());
    }

    @Override
    public String[] getImplementedActions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Class getPermissibleCategory() {
        return AmpTemplatesVisibility.class;

    }
    
    /**
     * fields indexed by name, for easy lookup: we don't want to iterate through 450 items for each and every of the 220 columns
     */
    private transient Map<String, AmpFieldsVisibility> namesCache = null;
    private synchronized void buildNamesCache() {
        if (fields == null) {
            return;
        }
        Map<String, AmpFieldsVisibility> tempNamesCache = new HashMap<String, AmpFieldsVisibility>();
        for (AmpFieldsVisibility vis : getFields()) {
            tempNamesCache.put(vis.getName(), vis);
        }
        namesCache = tempNamesCache;
    }
    
    /**
     * returns true iff a field with a given name exists
     * @param name
     * @return
     */
    public boolean fieldExists(String name) {
        if (fields == null) {
            return false;
        }
        
        if (namesCache == null) {
            buildNamesCache();
        }
        return namesCache.containsKey(name);
    }
    
    /**
     * removed a field from the internal fields list and invalidates cache
     * @param field
     */
    public void removeField(AmpFieldsVisibility field) {
        if (fields != null) {
            fields.remove(field);
        }
        invalidateCache();
    }
    
    public void clearFields() {
        if (fields != null) {
            fields.clear();
        }
        invalidateCache();
    }
    
    /**
     * SLOW, but seldomly called
     * @param field
     */
    public void addField(AmpFieldsVisibility field) {
        fields.add(field);
        invalidateCache(); // don't change cache here, as this is not thread safe
    }
    
    public void invalidateCache() {
        namesCache = null;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        AmpTemplatesVisibility n = (AmpTemplatesVisibility) super.clone();

        //create new sets
        SortedSet tmp = new TreeSet();
        for (Object o : n.getFeatures()) {
            tmp.add(o);
        }
        n.setFeatures(tmp);

        tmp = new TreeSet();
        for (Object o : n.getFields()) {
            tmp.add(o);
        }
        n.setFields(tmp);

        tmp = new TreeSet();
        for (Object o : n.getItems()) {
            tmp.add(o);
        }
        n.setItems(tmp);

        return n;
    }

    public List<String> getUsedByTeamsNames() {
        return usedByTeamsNames;
    }

    public void setUsedByTeamsNames(List<String> usedByTeamsNames) {
        this.usedByTeamsNames = usedByTeamsNames;
    }

    @Override
    public boolean isVisibleTemplateObj(AmpTemplatesVisibility template) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
