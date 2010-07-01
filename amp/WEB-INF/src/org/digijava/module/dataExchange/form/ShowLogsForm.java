/**
 * 
 */
package org.digijava.module.dataExchange.form;

import org.apache.struts.action.ActionForm;

/**
 * @author Alex Gartner
 *
 */
public class ShowLogsForm extends ActionForm {
	private Long selectedSourceId;

	/**
	 * @return the selectedSourceId
	 */
	public Long getSelectedSourceId() {
		return selectedSourceId;
	}

	/**
	 * @param selectedSourceId the selectedSourceId to set
	 */
	public void setSelectedSourceId(Long selectedSourceId) {
		this.selectedSourceId = selectedSourceId;
	}


	
	
}
