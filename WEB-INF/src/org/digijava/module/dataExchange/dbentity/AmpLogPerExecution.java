/**
 * 
 */
package org.digijava.module.dataExchange.dbentity;

import java.sql.Timestamp;


public class AmpLogPerExecution {
	private AmpSourceSetting ampSourceSetting;
	private String description;
	private Timestamp executionTime;
	private String externalTimestamp;
	/**
	 * @return the ampSourceSetting
	 */
	public AmpSourceSetting getAmpSourceSetting() {
		return ampSourceSetting;
	}
	/**
	 * @param ampSourceSetting the ampSourceSetting to set
	 */
	public void setAmpSourceSetting(AmpSourceSetting ampSourceSetting) {
		this.ampSourceSetting = ampSourceSetting;
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
	 * @param executionTime the executionTime to set
	 */
	public void setExecutionTime(Timestamp executionTime) {
		this.executionTime = executionTime;
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
	
	
	
}
