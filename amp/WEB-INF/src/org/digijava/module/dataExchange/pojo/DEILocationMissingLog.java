/**
 * 
 */
package org.digijava.module.dataExchange.pojo;

import java.util.Iterator;

import org.digijava.module.dataExchange.jaxb.CodeValueType;
import org.digijava.module.dataExchange.util.DataExchangeConstants;

/**
 * @author dan
 *
 */
public class DEILocationMissingLog extends DELog{
	private final String logType = DataExchangeConstants.MISSING_LOCATION;
	
	public DEILocationMissingLog() {
		// TODO Auto-generated constructor stub
	}

	public String getLogType() {
		return logType;
	}

	public DEILocationMissingLog(CodeValueType cvt) {
		super(cvt);
		// TODO Auto-generated constructor stub
	}

	public DEILocationMissingLog(String description, String entityName) {
		super(description, entityName);
		// TODO Auto-generated constructor stub
	}

	public DEILocationMissingLog(String entityName) {
		super(entityName);
		// TODO Auto-generated constructor stub
	}


}
