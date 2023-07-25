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

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "AMP_TEMPLATES_VISIBILITY")
public class AmpTemplatesVisibility extends AmpObjectVisibility implements Serializable, Cloneable {
    /**
     *
     */
    private static final long serialVersionUID = -4765301740400470276L;

    private Set<AmpFeaturesVisibility> features;
    private Set<AmpFieldsVisibility> fields;
    private String visible;

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
}