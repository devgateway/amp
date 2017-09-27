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
public class DESectorPercentageLog extends DELog{
	private final String logType = DataExchangeConstants.SECTOR_PERCENTAGE;
	
	public DESectorPercentageLog() {
		// TODO Auto-generated constructor stub
	}

	public String getLogType() {
		return logType;
	}

	public DESectorPercentageLog(CodeValueType cvt) {
		super(cvt);
		// TODO Auto-generated constructor stub
	}

	public DESectorPercentageLog(String description, String entityName) {
		super(description, entityName);
		// TODO Auto-generated constructor stub
	}

	public DESectorPercentageLog(String entityName) {
		super(entityName);
		// TODO Auto-generated constructor stub
	}


}
