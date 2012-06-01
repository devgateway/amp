/**
 * @author dan
 *
 * 
 */
package org.dgfoundation.amp.visibility;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.gateperm.core.ClusterIdentifiable;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.Permissible;

/**
 * @author dan
 *
 */
public abstract class AmpObjectVisibility  extends Permissible implements Serializable, Comparable,ClusterIdentifiable {
     private final static String [] IMPLEMENTED_ACTIONS=new String[] { GatePermConst.Actions.EDIT, GatePermConst.Actions.VIEW } ;

    @PermissibleProperty(type={Permissible.PermissibleProperty.PROPERTY_TYPE_ID})
	protected Long id;
	
	@PermissibleProperty(type={Permissible.PermissibleProperty.PROPERTY_TYPE_LABEL,Permissible.PermissibleProperty.PROPERTY_TYPE_CLUSTER_ID})
	protected String name;
	
	protected Set items;
	protected Set allItems;
	protected String nameTrimmed;
	protected String properName;
	protected Boolean hasLevel;
	protected String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getHasLevel() {
		return hasLevel;
	}

	public void setHasLevel(Boolean hasLevel) {
		this.hasLevel = hasLevel;
	}

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
	
	public String getDescriptionTrimmed()
	{
		return this.description.replaceAll(" ","");
	}
	
	public Set getAllItems() {
		return allItems;
	}
	
	public TreeSet getSortedAlphaAllItems()
	{
		if(this.getAllItems()==null) return null; 
		TreeSet mySet=new TreeSet(FeaturesUtil.ALPHA_ORDER);
		 mySet.addAll(this.getAllItems());
		 return mySet;
	}
	
	public void setAllItems(Set allItems) {
		this.allItems = allItems;
	}
	
	public String toString() {
		return this.name+" - id="+super.toString();
	}


	public Object getIdentifier() {
	    return id; 
	}
	
	public TreeSet getSortedAlphaItems()
	{
		if(this.getItems()==null) return null;
		 TreeSet mySet=new TreeSet(FeaturesUtil.ALPHA_ORDER);
		 mySet.addAll(this.getItems());
		 return mySet;
	}

	public String getProperName() throws IOException {
		////System.out.println("-----------------------"+FeaturesUtil.makeProperString(this.getName()));
		return FeaturesUtil.makeProperString(this.getName());
	}

	public void setProperName(String properName) {
		this.properName = properName;
	}
	
	public String getClusterIdentifier() { 
		return name;
	}
    public String[] getImplementedActions() {
	   return  IMPLEMENTED_ACTIONS.clone();
	}
	
		
}
