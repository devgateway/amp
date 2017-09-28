package org.digijava.module.aim.util.filters;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.digijava.module.aim.util.HierarchyListable;

/**
 * @author Alex Gartner
 *
 */
public class HierarchyListableImplementation implements HierarchyListable {
    
    private final String label;
    private final String uniqueId;
    
    private String additionalSearchableString;
    private Collection<? extends HierarchyListable> children;
    //children id-parent id mapping for all children
    private Map <Long,Long> parentMapping= new HashMap<Long,Long>(); 
    private boolean translateable   = true;


    public HierarchyListableImplementation(String label, String uniqueId) {
        this(label, uniqueId, null);
    }
    
    public HierarchyListableImplementation(String label, String uniqueId, Collection<? extends HierarchyListable> children) {
        this.label = label;
        this.uniqueId = uniqueId;
        this.children = children;
    }
    
    public String getType () {
        return "checkboxlist";
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


    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }


    /**
     * @return the uniqueId
     */
    public String getUniqueId() {
        return uniqueId;
    }


    /**
     * @return the children
     */
    public Collection<? extends HierarchyListable> getChildren() {
        return children;
    }


    /**
     * @param children the children to set
     */
    public void setChildren(Collection<? extends HierarchyListable> children) {
        this.children = children;
    }


    @Override
    public boolean getTranslateable() {
        return translateable;
    }


    @Override
    public void setTranslateable(boolean translateable) {
        this.translateable = translateable;
    }


    public String getAdditionalSearchString() {
        return this.additionalSearchableString;
    }
    
    public Map<Long, Long> getParentMapping() {
        return parentMapping;
    }

    public void setParentMapping(Map<Long, Long> parentMapping) {
        this.parentMapping = parentMapping;
    }



}
