/**
 * 
 */
package org.digijava.module.dataExchange.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.dataExchange.dbentity.DELogPerItem;

/**
 * @author Alex Gartner
 *
 */
public class ShowLogsForm extends ActionForm {
	private Long selectedSourceId;
	private Long selectedLogPerExecId;
	private Long selectedLogPerItemId;
	
	private DELogPerItem lpi;

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

	/**
	 * @return the selectedLogPerExecId
	 */
	public Long getSelectedLogPerExecId() {
		return selectedLogPerExecId;
	}

	/**
	 * @param selectedLogPerExecId the selectedLogPerExecId to set
	 */
	public void setSelectedLogPerExecId(Long selectedLogPerExecId) {
		this.selectedLogPerExecId = selectedLogPerExecId;
	}

	/**
	 * @return the selectedLogPerItemId
	 */
	public Long getSelectedLogPerItemId() {
		return selectedLogPerItemId;
	}

	/**
	 * @param selectedLogPerItemId the selectedLogPerItemId to set
	 */
	public void setSelectedLogPerItemId(Long selectedLogPerItemId) {
		this.selectedLogPerItemId = selectedLogPerItemId;
	}

	/**
	 * @return the lpi
	 */
	public DELogPerItem getLpi() {
		return lpi;
	}

	/**
	 * @param lpi the lpi to set
	 */
	public void setLpi(DELogPerItem lpi) {
		this.lpi = lpi;
	}
	
	

	
	
	
}
