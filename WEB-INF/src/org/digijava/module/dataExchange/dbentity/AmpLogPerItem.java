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
	
}
