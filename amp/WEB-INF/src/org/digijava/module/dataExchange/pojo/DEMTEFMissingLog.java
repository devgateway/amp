/**
 * 
 */
package org.digijava.module.dataExchange.pojo;

import org.digijava.module.dataExchange.jaxb.CodeValueType;
import org.digijava.module.dataExchange.util.DataExchangeConstants;

/**
 * @author dan
 *
 */
public class DEMTEFMissingLog extends DELog{
	private final String logType = DataExchangeConstants.MISSING_MTEF;
	
	public DEMTEFMissingLog() {
		// TODO Auto-generated constructor stub
	}

	public String getLogType() {
		return logType;
	}

	public DEMTEFMissingLog(CodeValueType cvt) {
		super(cvt);
		// TODO Auto-generated constructor stub
	}

	public DEMTEFMissingLog(String description, String entityName) {
		super(description, entityName);
		// TODO Auto-generated constructor stub
	}

	public DEMTEFMissingLog(String entityName) {
		super(entityName);
		// TODO Auto-generated constructor stub
	}


}
