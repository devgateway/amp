/*
* AMP FEATURE TEMPLATES
*/
/**
 * @author dan
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

import org.dgfoundation.amp.visibility.AmpObjectVisibility;

public class AmpModulesVisibility extends AmpObjectVisibility implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 292612393819900427L;
	/**
	 * @author dan
	 */
	private Set templates;
	private Set submodules;
	private AmpModulesType type;
	
	public AmpModulesType getType() {
		return type;
	}

	public void setType(AmpModulesType type) {
		this.type = type;
	}

	public Set getTemplates() {
		return templates;
	}	

	public void setTemplates(Set templates) {
		this.templates = templates;
	}

	public AmpObjectVisibility getParent() {
		// TODO Auto-generated method stub
		//templates.iterator().next();
		return super.parent;
	}

	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		
		AmpObjectVisibility x=(AmpObjectVisibility) arg0;
		return this.getId().compareTo(x.getId());
		
	}

	public String getVisible() {
		return getTemplate().getItems().contains(this)?"true":"false";
	}
	
	public boolean isVisibleId(Long id){
		////System.out.println("MODULUL:"+name+" are template-uri:"+this.getTemplates().size());
		for(Iterator it=this.getTemplates().iterator();it.hasNext();)
		{
			AmpTemplatesVisibility x=(AmpTemplatesVisibility) it.next();
			////System.out.println("			compar template-ul"+x.getId()+" cu id:"+id);	
			if(x.getId().compareTo(id)==0) return true;
			
		}
		return false;
	}

	public boolean isVisibleTemplateObj(AmpObjectVisibility aObjVis){
		for(Iterator it=aObjVis.getItems().iterator();it.hasNext();)
		{
			AmpModulesVisibility x=(AmpModulesVisibility) it.next();
			if(x.getId().compareTo(id)==0) return true;
			
		}
		return false;
	}
	
	public AmpTemplatesVisibility getTemplate() {
		return (AmpTemplatesVisibility) parent;
	}

	public Set getSubmodules() {
		return submodules;
	}

	public void setSubmodules(Set submodules) {
		this.submodules = submodules;
	}
		
}