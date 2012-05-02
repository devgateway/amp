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
public class DEOrgMissingLog extends DELog{
	private final String logType = DataExchangeConstants.MISSING_ORGANIZATION;
	
	public DEOrgMissingLog() {
		// TODO Auto-generated constructor stub
	}

	public String getLogType() {
		return logType;
	}

	public DEOrgMissingLog(CodeValueType cvt) {
		super(cvt);
		// TODO Auto-generated constructor stub
	}

	public DEOrgMissingLog(String description, String entityName) {
		super(description, entityName);
		// TODO Auto-generated constructor stub
	}

	public DEOrgMissingLog(String entityName) {
		super(entityName);
		// TODO Auto-generated constructor stub
	}
	
	


}
