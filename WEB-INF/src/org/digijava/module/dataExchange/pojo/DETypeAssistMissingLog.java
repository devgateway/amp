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
public class DETypeAssistMissingLog extends DELog{
	private final String logType = DataExchangeConstants.MISSING_TYPE_OF_ASSISTANCE;
	
	public DETypeAssistMissingLog() {
		// TODO Auto-generated constructor stub
	}

	public String getLogType() {
		return logType;
	}

	public DETypeAssistMissingLog(CodeValueType cvt) {
		super(cvt);
		// TODO Auto-generated constructor stub
	}

	public DETypeAssistMissingLog(String description, String entityName) {
		super(description, entityName);
		// TODO Auto-generated constructor stub
	}

	public DETypeAssistMissingLog(String entityName) {
		super(entityName);
		// TODO Auto-generated constructor stub
	}


}
