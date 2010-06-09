package org.digijava.module.dataExchange.dbentity;

import java.sql.Timestamp;

public class AmpLogPerItem {
	public static final String LOG_TYPE_INFO	= "INFO";
	public static final String LOG_TYPE_WARN	= "WARN";
	public static final String LOG_TYPE_ERROR	= "ERROR";
	
	public static final String ITEM_TYPE_ACTIVITY	= "ACTIVITY";
	
	private AmpLogPerExecution executionInstance;
	private String logType;

	private String itemType;
	private String description;
	
	private Timestamp executionTime;

	/**
	 * @return the executionInstance
	 */
	public AmpLogPerExecution getExecutionInstance() {
		return executionInstance;
	}

	/**
	 * @param executionInstance the executionInstance to set
	 */
	public void setExecutionInstance(AmpLogPerExecution executionInstance) {
		this.executionInstance = executionInstance;
	}

	/**
	 * @return the logType
	 */
	public String getLogType() {
		return logType;
	}

	/**
	 * @param logType the logType to set
	 */
	public void setLogType(String logType) {
		this.logType = logType;
	}

	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
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
	
	
	
}
