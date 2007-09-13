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

public class AmpModulesType implements Serializable {
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 63085660069700862L;
	private Long id;
	private String name;
	private Set modules;
	public Set getModules() {
		return modules;
	}
	public void setModules(Set modules) {
		this.modules = modules;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}