/**
 * @author dan
 *
 * 
 */
package org.dgfoundation.amp.visibility;

import java.io.Serializable;
import java.util.Set;

import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;

/**
 * @author dan
 *
 */
public abstract class AmpObjectVisibility implements Serializable, Comparable {

	/**
	 * 
	 */
	protected Long id;
	protected String name;
	protected Set items;
	protected Set allItems;
	protected String nameTrimmed;

	public void setNameTrimmed(String nameTrimmed) {
		this.nameTrimmed = nameTrimmed;
	}

	public abstract AmpTemplatesVisibility getTemplate();
	
	protected AmpObjectVisibility parent;
	protected Set templates;
	
	public abstract String getVisible();

	public Set getTemplates() {
		return templates;
	}
	public void setTemplates(Set templates) {
		this.templates = templates;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Set getItems() {
		return items;
	}
	public void setItems(Set items) {
		this.items = items;
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
	public Set getAllItems() {
		return allItems;
	}
	public void setAllItems(Set allItems) {
		this.allItems = allItems;
	}
	
	public String toString() {
		return this.name+" - id="+super.toString();
	}

}
