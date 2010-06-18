package org.digijava.module.dataExchange.dbentity;

import java.sql.Timestamp;

public class DELogPerItem {
	public static final String LOG_TYPE_INFO	= "INFO";
	public static final String LOG_TYPE_WARN	= "WARN";
	public static final String LOG_TYPE_ERROR	= "ERROR";
	
	public static final String ITEM_TYPE_ACTIVITY	= "ACTIVITY";
	
	
	private Long id;
	private DELogPerExecution deLogPerExecution;
	private String logType;

	private String name;
	private String itemType;
	private String description;
	
	private Timestamp executionTime;

	
	
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
	 * @return the deLogPerExecution
	 */
	public DELogPerExecution getDeLogPerExecution() {
		return deLogPerExecution;
	}

	/**
	 * @param deLogPerExecution the deLogPerExecution to set
	 */
	public void setDeLogPerExecution(DELogPerExecution deLogPerExecution) {
		this.deLogPerExecution = deLogPerExecution;
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

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
