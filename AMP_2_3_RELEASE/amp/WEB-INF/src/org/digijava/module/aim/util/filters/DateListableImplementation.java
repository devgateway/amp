/**
 * 
 */
package org.digijava.module.aim.util.filters;

import java.util.Collection;

import org.digijava.module.aim.util.HierarchyListable;

/**
 * @author alex
 *
 */
public class DateListableImplementation extends HierarchyListableImplementation {

	private String label;
	private String uniqueId;
	private boolean translateable;
	
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
	public DateListableImplementation() {
		// TODO Auto-generated constructor stub
	}

	
	public String getType () {
		return "datelist";
	}
	
	/* (non-Javadoc)
	 * @see org.digijava.module.aim.util.HierarchyListable#getLabel()
	 */
	@Override
	public String getLabel() {
		
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/* (non-Javadoc)
	 * @see org.digijava.module.aim.util.HierarchyListable#getUniqueId()
	 */
	@Override
	public String getUniqueId() {
		return uniqueId;
	}
	

	/**
	 * @param uniqueId the uniqueId to set
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
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
		this.translateable	= translateable;

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

}
