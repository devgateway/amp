/**
 * 
 */
package org.digijava.module.aim.util.filters;

import org.digijava.module.aim.util.HierarchyListable;

import java.util.Collection;

/**
 * @author alex
 *
 */
public class DateListableImplementation extends HierarchyListableImplementation {

    private boolean translateable;
    private boolean selected;
    
    /**
     * As opposed to other HierarchyListable hierarchies of objects the DateListableImplementations 
     * don't map to an array in the activity form. That's why we need to know the property name for
     * each object. 
     */
    private String actionFormProperty;
    


    private Collection<? extends HierarchyListable> children;
    /**
     * 
     */
    public DateListableImplementation(String label, String uniqueId) {
        super(label, uniqueId);
    }

    
    public String getType () {
        return "datelist";
    }
    
    /* (non-Javadoc)
     * @see org.digijava.module.aim.util.HierarchyListable#getAdditionalSearchString()
     */
    @Override
    public String getAdditionalSearchString() {
        return null;
    }

    @Override
    public boolean getTranslateable() {
        return translateable;
    }

    /* (non-Javadoc)
     * @see org.digijava.module.aim.util.HierarchyListable#setTranslateable(boolean)
     */
    @Override
    public void setTranslateable(boolean translateable) {
        this.translateable  = translateable;

    }
    


    /**
     * @return the actionFormProperty
     */
    public String getActionFormProperty() {
        return actionFormProperty;
    }


    /**
     * @param actionFormProperty the actionFormProperty to set
     */
    public void setActionFormProperty(String actionFormProperty) {
        this.actionFormProperty = actionFormProperty;
    }


    /* (non-Javadoc)
     * @see org.digijava.module.aim.util.HierarchyListable#getChildren()
     */
    @Override
    public Collection<? extends HierarchyListable> getChildren() {
        return this.children;
    }
    

    /**
     * @param children the children to set
     */
    public void setChildren(Collection<? extends HierarchyListable> children) {
        this.children = children;
    }

    /* (non-Javadoc)
     * @see org.digijava.module.aim.util.HierarchyListable#getCountDescendants()
     */
    @Override
    public int getCountDescendants() {
        int ret = 1;
        if ( this.getChildren() != null ) {
            for ( HierarchyListable hl: this.getChildren() )
                ret += hl.getCountDescendants();
        }
        return ret;
    }


    public boolean isSelected() {
        return selected;
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
