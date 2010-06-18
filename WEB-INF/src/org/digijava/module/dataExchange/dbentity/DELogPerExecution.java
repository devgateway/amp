/**
 * 
 */
package org.digijava.module.dataExchange.dbentity;

import java.sql.Timestamp;
import java.util.List;

import org.digijava.module.dataExchange.util.WrapperLogPerExecution;
import org.digijava.module.dataExchange.util.XmlWrappable;
import org.digijava.module.dataExchange.util.XmlWrapper;


public class DELogPerExecution implements XmlWrappable{
	
	private Long id;
	private DESourceSetting deSourceSetting;
	private String description;
	private Timestamp executionTime;
	private String externalTimestamp;
	private List<DELogPerItem> logItems;
	
	
	public DELogPerExecution() {
		super();
	}
	public DELogPerExecution(DESourceSetting deSourceSetting) {
		super();
		this.deSourceSetting = deSourceSetting;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * @return the logItems
	 */
	public List<DELogPerItem> getLogItems() {
		return logItems;
	}
	/**
	 * @param logItems the logItems to set
	 */
	public void setLogItems(List<DELogPerItem> logItems) {
		this.logItems = logItems;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the executionTime
	 */
	public Timestamp getExecutionTime() {
		return executionTime;
	}
	/**
	 * @param timestamp the executionTime to set
	 */
	public void setExecutionTime(Timestamp timestamp) {
		this.executionTime = timestamp;
	}
	/**
	 * @return the externalTimestamp
	 */
	public String getExternalTimestamp() {
		return externalTimestamp;
	}
	/**
	 * @param externalTimestamp the externalTimestamp to set
	 */
	public void setExternalTimestamp(String externalTimestamp) {
		this.externalTimestamp = externalTimestamp;
	}
	/**
	 * @return the deSourceSetting
	 */
	public DESourceSetting getDeSourceSetting() {
		return deSourceSetting;
	}
	/**
	 * @param deSourceSetting the deSourceSetting to set
	 */
	public void setDeSourceSetting(DESourceSetting deSourceSetting) {
		this.deSourceSetting = deSourceSetting;
	}
	@Override
	public XmlWrapper getXmlWrapperInstance() {
		return new WrapperLogPerExecution(this);
	}
	
}
