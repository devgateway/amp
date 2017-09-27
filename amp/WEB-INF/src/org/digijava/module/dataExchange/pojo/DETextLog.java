/**
 * 
 */
package org.digijava.module.dataExchange.pojo;

import org.digijava.module.dataExchange.util.DataExchangeConstants;

/**
 * @author dan
 *
 */
public class DETextLog extends DELog{
	private final String logType = DataExchangeConstants.TEXT;
	
	public DETextLog() {
		// TODO Auto-generated constructor stub
	}

	public String getLogType() {
		return logType;
	}


}
