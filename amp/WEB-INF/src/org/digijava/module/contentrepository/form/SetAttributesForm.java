/**
 * 
 */
package org.digijava.module.contentrepository.form;

import java.util.Set;

import org.apache.struts.action.ActionForm;

/**
 * @author Alex Gartner
 *
 */
public class SetAttributesForm extends ActionForm {

	private String uuid;
	private String action;
	
	public SetAttributesForm() {
		uuid	= null;
		action	= null;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
	
}
