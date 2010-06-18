package org.digijava.module.aim.util.filters;

import org.digijava.module.aim.util.HierarchyListable;

public class GroupingElement <T extends HierarchyListable> {
	private String name;
	private String htmlDivId;
	private T rootHierarchyListable;
	private String actionFormProperty;
	
	public GroupingElement() {
		;
	}
	
	public GroupingElement(String name, String htmlDivId, T rootHierarchyLIstable, String actionFormProperty ) {
		this.name					= name;
		this.htmlDivId				= htmlDivId;
		this.rootHierarchyListable	= rootHierarchyLIstable;
		this.actionFormProperty		= actionFormProperty;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the htmlDivId
	 */
	public String getHtmlDivId() {
		return htmlDivId;
	}
	/**
	 * @param htmlDivId the htmlDivId to set
	 */
	public void setHtmlDivId(String htmlDivId) {
		this.htmlDivId = htmlDivId;
	}
	/**
	 * @return the rootHierarchyListable
	 */
	public T getRootHierarchyListable() {
		return rootHierarchyListable;
	}
	/**
	 * @param rootHierarchyListable the rootHierarchyListable to set
	 */
	public void setRootHierarchyListable(T rootHierarchyListable) {
		this.rootHierarchyListable = rootHierarchyListable;
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
}
