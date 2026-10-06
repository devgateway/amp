package org.digijava.module.budgetexport.adapter;

import org.digijava.module.aim.util.HierarchyListable;

import java.util.Collection;

/**
 * User: flyer
 * Date: 2/13/12
 * Time: 6:48 PM
 */
public class DummyAmpEntity implements HierarchyListable {
    private String label;
    private Long id;

    public DummyAmpEntity() {
    }

    public DummyAmpEntity(Long id, String label) {
        this.id = id;
        this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }

    public String getUniqueId() {
        return String.valueOf(this.id);
    }

    public String getAdditionalSearchString() {
        return this.label;
    }

    public Collection<? extends HierarchyListable> getChildren() {
        return null;
    }

    public int getCountDescendants() {
        return 0;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean getTranslateable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setTranslateable(boolean translateable) {
        // TODO Auto-generated method stub
        
    }
}
