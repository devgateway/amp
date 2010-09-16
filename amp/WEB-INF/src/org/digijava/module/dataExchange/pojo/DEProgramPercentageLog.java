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
public class DEProgramPercentageLog extends DELog{
	private final String logType = DataExchangeConstants.PROGRAM_PERCENTAGE;
	
	public DEProgramPercentageLog() {
		// TODO Auto-generated constructor stub
	}

	public String getLogType() {
		return logType;
	}

	public DEProgramPercentageLog(CodeValueType cvt) {
		super(cvt);
		// TODO Auto-generated constructor stub
	}

	public DEProgramPercentageLog(String description, String entityName) {
		super(description, entityName);
		// TODO Auto-generated constructor stub
	}

	public DEProgramPercentageLog(String entityName) {
		super(entityName);
		// TODO Auto-generated constructor stub
	}


}
