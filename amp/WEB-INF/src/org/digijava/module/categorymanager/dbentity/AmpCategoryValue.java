package org.digijava.module.categorymanager.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.digijava.module.aim.annotations.interchange.PossibleValueId;
import org.digijava.module.aim.annotations.interchange.PossibleValueValue;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.Versionable;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.Output;

/**
 * Represents one of the possible values for a certain category
 * @author Alex Gartner
 *
 */
public class AmpCategoryValue implements Serializable, Identifiable, Comparable<AmpCategoryValue>, HierarchyListable, Versionable{
    @PossibleValueId
    private Long id;
    private AmpCategoryClass ampCategoryClass;
    @PossibleValueValue
    private String value;
    private Integer index;
    private Boolean deleted = false;
    
    private Set<AmpActivityVersion> activities;
    //private Long fieldType;
    
    private Set<AmpCategoryValue> usedValues = new HashSet<>();
    private Set<AmpCategoryValue> usedByValues = new HashSet<>();

    private AmpCategoryValue defaultUsedValue;

    private boolean translateable   = true;

    /**
     * LAME way of html-escaping
     * TODO: review, rewrite
     * @return
     */
    public String getEncodedValue(){
        String value = "";
        for(int i=0;i<this.value.length();i++) {
            if(this.value.charAt(i)>='A' && this.value.charAt(i) <= 'z'){
                value = value + this.value.charAt(i);
            }
        }
        //value = URLEncoder.encode(this.value,"");
        return value;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public AmpCategoryClass getAmpCategoryClass() {
        return ampCategoryClass;
    }
    public void setAmpCategoryClass(AmpCategoryClass ampCategoryClass) {
        this.ampCategoryClass = ampCategoryClass;
    }

    public Integer getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index  = index;
        // not used, calculated value, see getIndex() method
    }

//    public String toString() {
//        return value;
//    }
    public Object getIdentifier() {
        return this.getId();
    }
    public int compareTo(AmpCategoryValue a) {
        // TODO Auto-generated method stub
        return this.getId().compareTo(a.getId());
    }

    public Set<AmpCategoryValue> getUsedValues() {
        return usedValues;
    }

    public void setUsedValues(Set<AmpCategoryValue> usedValues) {
        this.usedValues = usedValues;
    }

    public Set<AmpCategoryValue> getUsedByValues() {
        return usedByValues;
    }

    public void setUsedByValues(Set<AmpCategoryValue> usedByValues) {
        this.usedByValues = usedByValues;
    }

    public AmpCategoryValue getDefaultUsedValue() {
        return defaultUsedValue;
    }

    public void setDefaultUsedValue(AmpCategoryValue defaultUsedValue) {
        this.defaultUsedValue = defaultUsedValue;
    }

    @Override
    public Collection<? extends HierarchyListable> getChildren() {
        return null;
    }
    
    @Override
    public int getCountDescendants() {
        return 1;
    }
    
    @Override
    public String getLabel() {
        return this.value;
    }
    
    @Override
    public String getUniqueId() {
        return String.valueOf(this.id.longValue());
    }
    
    @Override
    public boolean getTranslateable() {
        return translateable;
    }

    @Override
    public void setTranslateable(boolean translateable) {
        this.translateable = translateable;
    }

    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpCategoryValue aux = (AmpCategoryValue) obj;
        if (aux != null) {
            if (aux.getAmpCategoryClass().getName().equals(this.getAmpCategoryClass().getName())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());

        out.getOutputs().add(
                new Output(null, new String[] { "Class" }, new Object[] { this.ampCategoryClass.getName() }, true));
        out.getOutputs().add(new Output(null, new String[] { "Value" }, new Object[] { this.value }, true));
        return out;
    }
    
    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) {
        this.activities = new HashSet<AmpActivityVersion>();
        this.activities.add(newActivity);
        return this;
    }

    public String getAdditionalSearchString() {
        return this.value;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * whether this AmpCategoryValue has been marked as deleted
     * @return
     */
    public boolean isVisible() {
        return (deleted == null) || (!deleted);
    }
    
    public void setDeleted(Boolean deleted) {
        if (deleted == null)
            this.deleted = false;
        else
            this.deleted = deleted;
    }

    public boolean equals(Object o) {
        if (!(o instanceof AmpCategoryValue)) {
            return false;
        }
        AmpCategoryValue a = (AmpCategoryValue) o;
        return this.getId().equals(a.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
