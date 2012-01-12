package org.digijava.module.aim.util;

import java.util.Collection;

/**
 * 
 * @author Alex Gartner
 * 
 *
 */
public interface HierarchyListable {
	public String getLabel();
	public String getUniqueId();
    public String getAdditionalSearchString();
	public Collection<? extends HierarchyListable> getChildren();
	/**
	 * 
	 * @return number of descendants including self
	 */
	public int getCountDescendants();
}
