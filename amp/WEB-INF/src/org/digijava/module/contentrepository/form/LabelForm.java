/**
 * 
 */
package org.digijava.module.contentrepository.form;

import org.apache.struts.action.ActionForm;

/**
 * @author Alex Gartner
 *
 */
public class LabelForm extends ActionForm {
	private String action;
	private String docUUID;
	private String labelUUID;
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getDocUUID() {
		return docUUID;
	}
	public void setDocUUID(String docUUID) {
		this.docUUID = docUUID;
	}
	public String getLabelUUID() {
		return labelUUID;
	}
	public void setLabelUUID(String labelUUID) {
		this.labelUUID = labelUUID;
	}
}
