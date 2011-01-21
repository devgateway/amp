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
public class DEFinancInstrMissingLog extends DELog{
	private final String logType = DataExchangeConstants.MISSING_FINANCING_INSTRUMENT;
	
	public DEFinancInstrMissingLog() {
		// TODO Auto-generated constructor stub
	}

	public String getLogType() {
		return logType;
	}

	public DEFinancInstrMissingLog(CodeValueType cvt) {
		super(cvt);
		// TODO Auto-generated constructor stub
	}

	public DEFinancInstrMissingLog(String description, String entityName) {
		super(description, entityName);
		// TODO Auto-generated constructor stub
	}

	public DEFinancInstrMissingLog(String entityName) {
		super(entityName);
		// TODO Auto-generated constructor stub
	}


}
