package org.digijava.module.aim.util.filters;

import java.util.Collection;

import org.digijava.module.aim.util.HierarchyListable;

/**
 * @author Alex Gartner
 *
 */
public class HierarchyListableImplementation implements HierarchyListable {
	
	private String label;
	private String uniqueId;
	private Collection<? extends HierarchyListable> children;
    private String additionalSearchableString;

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
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}


	/**
	 * @return the uniqueId
	 */
	public String getUniqueId() {
		return uniqueId;
	}


	/**
	 * @param uniqueId the uniqueId to set
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
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

    public String getAdditionalSearchString() {
        return this.additionalSearchableString;
    }

	
}
