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
public class DEImplLevelMissingLog extends DELog{
	private final String logType = DataExchangeConstants.MISSING_IMPLEMENTATION_LEVEL;
	
	public DEImplLevelMissingLog() {
		// TODO Auto-generated constructor stub
	}

	public String getLogType() {
		return logType;
	}

	public DEImplLevelMissingLog(CodeValueType cvt) {
		super(cvt);
		// TODO Auto-generated constructor stub
	}

	public DEImplLevelMissingLog(String description, String entityName) {
		super(description, entityName);
		// TODO Auto-generated constructor stub
	}

	public DEImplLevelMissingLog(String entityName) {
		super(entityName);
		// TODO Auto-generated constructor stub
	}


}
