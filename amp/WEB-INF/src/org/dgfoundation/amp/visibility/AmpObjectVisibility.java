/**
 * @author dan
 *
 *
 */
package org.dgfoundation.amp.visibility;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.gateperm.core.ClusterIdentifiable;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.Permissible;

/**
 * @author dan
 *
 */
public abstract class AmpObjectVisibility  extends Permissible implements Serializable, Comparable, ClusterIdentifiable {
    private final static String [] IMPLEMENTED_ACTIONS=new String[] { GatePermConst.Actions.EDIT, GatePermConst.Actions.VIEW } ;

    @PermissibleProperty(type={Permissible.PermissibleProperty.PROPERTY_TYPE_ID})
    protected Long id;

    @PermissibleProperty(type={Permissible.PermissibleProperty.PROPERTY_TYPE_LABEL,Permissible.PermissibleProperty.PROPERTY_TYPE_CLUSTER_ID})
    protected String name;

    protected Set<AmpObjectVisibility> items;
    protected Set allItems;
    protected String nameTrimmed;
    protected String properName;
    protected Boolean hasLevel;
    protected String description;


    private TreeSet<AmpObjectVisibility> sortedItems    = null;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHasLevel() {
        return hasLevel;
    }

    public void setHasLevel(Boolean hasLevel) {
        this.hasLevel = hasLevel;
    }

    public void setNameTrimmed(String nameTrimmed) {
        this.nameTrimmed = nameTrimmed;
    }

    public abstract AmpTemplatesVisibility getTemplate();

    protected AmpObjectVisibility parent;
    protected Set<AmpTemplatesVisibility> templates;

    public abstract String getVisible();

    public Set<AmpTemplatesVisibility> getTemplates() {
        return templates;
    }
    public void setTemplates(Set<AmpTemplatesVisibility> templates) {
        this.templates = templates;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Set<AmpObjectVisibility> getItems() {
        return items;
    }

    public void setItems(Set<AmpObjectVisibility> items) {
        this.items = items;
    }

    public Set<AmpObjectVisibility> getOrCreateItems(){
        if (items == null)
            items = new TreeSet<>();
        return getItems();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public AmpObjectVisibility getParent() {
        return parent;
    }

    public void setParent(AmpObjectVisibility parent) {
        this.parent = parent;
    }

    public String getNameTrimmed()
    {
        return this.name.replaceAll(" ","");
    }

    public String getDescriptionTrimmed()
    {
        return this.description.replaceAll(" ","");
    }

    public Set getAllItems() {
        return allItems;
    }

    public TreeSet getSortedAlphaAllItems()
    {
        if(this.getAllItems()==null) return null;
        TreeSet mySet=new TreeSet(FeaturesUtil.ALPHA_ORDER);
        mySet.addAll(this.getAllItems());
        return mySet;
    }

    public void setAllItems(Set allItems) {
        this.allItems = allItems;
    }

    public String toString() {
        return String.format("%s %s (id: %d)", this.getClass().getSimpleName(), this.getName(), this.getId());
    }

    @Override
    public Object getIdentifier() {
        return id;
    }

    public TreeSet<AmpObjectVisibility> getSortedAlphaItems()
    {
        if(this.getItems()==null) return null;

        if (this.sortedItems == null || this.sortedItems.size() != this.items.size() ) {
            TreeSet<AmpObjectVisibility> mySet=new TreeSet<>(FeaturesUtil.ALPHA_ORDER);
            mySet.addAll(this.getItems());
            this.sortedItems   = mySet;
        }
        return this.sortedItems;
    }

    public String getProperName() throws IOException {
        //////System.out.println("-----------------------"+FeaturesUtil.makeProperString(this.getName()));
        return FeaturesUtil.makeProperString(this.getName());
    }

    public void setProperName(String properName) {
        this.properName = properName;
    }

    public String getClusterIdentifier() {
        return name;
    }
    public String[] getImplementedActions() {
        return  IMPLEMENTED_ACTIONS.clone();
    }

    @Override
    public int compareTo(Object other)
    {
        AmpObjectVisibility oth = (AmpObjectVisibility) other;
        int cmpClass = this.getClass().getName().compareTo(oth.getClass().getName());
        if (cmpClass != 0)
            return cmpClass; // normally we shouldn't be getting entries of different classes

        Long id1 = this.getId();
        Long id2 = oth.getId();

        if (id1 == null)
        {
            if (id2 == null) return 0;
            return 1; // nulls go to the end
        }
        if (id2 == null)
            return -1; //nulls go to the end

        return id1.compareTo(id2);
    }

    @Override
    public boolean equals(Object other)
    {
        return this.compareTo(other) == 0;
    }

    @Override
    public int hashCode()
    {
        if (this.getId() == null)
            return this.getClass().hashCode();
        return this.getId().hashCode();
    }

    abstract public boolean isVisibleTemplateObj(AmpTemplatesVisibility template);
}