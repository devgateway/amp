package org.digijava.module.aim.form;

import org.apache.struts.action.*;

import java.io.Serializable;
import java.util.Collection;
/*
 *@author Govind G Dalwani 
 */

public class ComponentsForm extends ActionForm implements Serializable{

	private Collection components ;

	/**
	 * @return Returns the components.
	 */
	public Collection getComponents() {
		return components;
	}

	/**
	 * @param components The components to set.
	 */
	public void setComponents(Collection components) {
		this.components = components;
	}

	
	
}