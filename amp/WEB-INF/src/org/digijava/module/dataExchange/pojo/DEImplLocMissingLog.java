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
public class DEImplLocMissingLog extends DELog{
	private final String logType = DataExchangeConstants.MISSING_IMPLEMENTATION_LOCATION;
	
	public DEImplLocMissingLog() {
		// TODO Auto-generated constructor stub
	}

	public String getLogType() {
		return logType;
	}

	public DEImplLocMissingLog(CodeValueType cvt) {
		super(cvt);
		// TODO Auto-generated constructor stub
	}

	public DEImplLocMissingLog(String description, String entityName) {
		super(description, entityName);
		// TODO Auto-generated constructor stub
	}

	public DEImplLocMissingLog(String entityName) {
		super(entityName);
		// TODO Auto-generated constructor stub
	}


}
