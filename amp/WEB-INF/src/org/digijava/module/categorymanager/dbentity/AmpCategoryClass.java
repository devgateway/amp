package org.digijava.module.categorymanager.dbentity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Alex Gartner
 *
 */
@Entity
@Table(name = "AMP_CATEGORY_CLASS")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpCategoryClass implements Serializable, Comparable<AmpCategoryClass> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_CATEGORY_CLASS_SEQ")
    @SequenceGenerator(name = "AMP_CATEGORY_CLASS_SEQ", sequenceName = "AMP_CATEGORY_CLASS_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "category_name")
    private String name;

    @Column(name = "keyName", unique = true)
    private String keyName;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "is_multiselect")
    private boolean isMultiselect;

    @Column(name = "is_ordered")
    private boolean isOrdered;

    @OneToMany(mappedBy = "ampCategoryClass", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "index_column")
    private List<AmpCategoryValue> possibleValues;

    @ManyToMany(mappedBy = "usedCategories", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<AmpCategoryClass> usedByCategories;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AMP_CATEGORIES_USED", joinColumns = @JoinColumn(name = "used_category_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<AmpCategoryClass> usedCategories;


    private Boolean usedByCategorySingleSelect; //how this category class is linked with current usedByCategory
    
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public boolean getisMultiselect() {
        return isMultiselect;
    }
    public boolean isMultiselect() {
        return isMultiselect;
    }
    public void setIsMultiselect(boolean isMultiselect) {
        this.isMultiselect = isMultiselect;
    }
    public boolean getIsOrdered() {
        return isOrdered;
    }
    public boolean isOrdered() {
        return isOrdered;
    }
    public void setIsOrdered(boolean isOrdered) {
        this.isOrdered = isOrdered;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public List<AmpCategoryValue> getPossibleValues() {
        return possibleValues;
    }
    public void setPossibleValues(List<AmpCategoryValue> possibleValues) {
        if (this.possibleValues == null)
            this.possibleValues = possibleValues;
        else {
            this.possibleValues.clear();
            this.possibleValues.addAll(possibleValues);
        }
    }

    public String getKeyName() {
        return keyName;
    }
    public void setKeyName(String key) {
        this.keyName = key;
    }
    public List<AmpCategoryClass> getUsedCategories() {
        return usedCategories;
    }
    public void setUsedCategories(List<AmpCategoryClass> usedCategories) {
        this.usedCategories = usedCategories;
    }
    
    public Set<AmpCategoryClass> getUsedByCategories() {
        return usedByCategories;
    }
    public void setUsedByCategories(Set<AmpCategoryClass> usedByCategories) {
        this.usedByCategories = usedByCategories;
    }
    public int compareTo(AmpCategoryClass o) {
        return keyName.compareTo( o.getKeyName() );
    }
    
    @Override 
    public boolean equals(Object o) {
        if(o instanceof AmpCategoryClass)
        return compareTo( (AmpCategoryClass)o ) == 0;
        else return false;
    }
    
    public Boolean getUsedByCategorySingleSelect() {
        return usedByCategorySingleSelect;
    }
    public void setUsedByCategorySingleSelect(Boolean usedByCategorySingleSelect) {
        this.usedByCategorySingleSelect = usedByCategorySingleSelect;
    } 
        
}
