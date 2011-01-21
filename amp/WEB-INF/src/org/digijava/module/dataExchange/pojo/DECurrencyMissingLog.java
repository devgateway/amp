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
public class DECurrencyMissingLog extends DELog{
	private final String logType = DataExchangeConstants.MISSING_CURRENCY;
	
	public DECurrencyMissingLog() {
		// TODO Auto-generated constructor stub
	}

	public String getLogType() {
		return logType;
	}

	public DECurrencyMissingLog(CodeValueType cvt) {
		super(cvt);
		// TODO Auto-generated constructor stub
	}

	public DECurrencyMissingLog(String description, String entityName) {
		super(description, entityName);
		// TODO Auto-generated constructor stub
	}

	public DECurrencyMissingLog(String entityName) {
		super(entityName);
		// TODO Auto-generated constructor stub
	}


}
