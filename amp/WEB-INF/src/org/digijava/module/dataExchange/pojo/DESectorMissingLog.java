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
public class DESectorMissingLog extends DELog{
	private final String logType = DataExchangeConstants.MISSING_SECTOR;
	
	public DESectorMissingLog() {
		// TODO Auto-generated constructor stub
	}

	public String getLogType() {
		return logType;
	}

	public DESectorMissingLog(CodeValueType cvt) {
		super(cvt);
		// TODO Auto-generated constructor stub
	}

	public DESectorMissingLog(String description, String entityName) {
		super(description, entityName);
		// TODO Auto-generated constructor stub
	}

	public DESectorMissingLog(String entityName) {
		super(entityName);
		// TODO Auto-generated constructor stub
	}


}
