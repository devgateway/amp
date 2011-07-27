/**
 * 
 */
package org.digijava.module.dataExchange.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.dataExchange.dbentity.DELogPerExecution;
import org.digijava.module.dataExchange.dbentity.DELogPerItem;
import org.digijava.module.dataExchange.dbentity.DESourceSetting;

/**
 * @author Alex Gartner
 *
 */
public class ShowLogsForm extends ActionForm {
	private Long selectedSourceId;
	private Long selectedLogPerExecId;
	private Long selectedLogPerItemId;
	
	private DELogPerItem lpi;
	private List<DELogPerExecution> logs;
	private List<DELogPerItem> logItems;
	private List <DESourceSetting> availableSourceSettings;
	private String selectedSourceName ;

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

	public List<DELogPerExecution> getLogs() {
		return logs;
	}

	public void setLogs(List<DELogPerExecution> logs) {
		this.logs = logs;
	}

	public String getSelectedSourceName() {
		return selectedSourceName;
	}

	public void setSelectedSourceName(String selectedSourceName) {
		this.selectedSourceName = selectedSourceName;
	}

	public List<DESourceSetting> getAvailableSourceSettings() {
		return availableSourceSettings;
	}

	public void setAvailableSourceSettings(
			List<DESourceSetting> availableSourceSettings) {
		this.availableSourceSettings = availableSourceSettings;
	}

	public List<DELogPerItem> getLogItems() {
		return logItems;
	}

	public void setLogItems(List<DELogPerItem> logItems) {
		this.logItems = logItems;
	}	
}
