/**
 * 
 */
package org.digijava.module.dataExchange.pojo;

import java.util.ArrayList;

import org.digijava.module.dataExchange.jaxb.CodeValueType;
import org.digijava.module.dataExchange.util.DataExchangeConstants;

/**
 * @author dan
 *
 */
public class DEActivityLog extends DELog{
	private final String logType = DataExchangeConstants.MISSING_ORGANIZATION;
	
	public DEActivityLog() {
		// TODO Auto-generated constructor stub
		this.setItems(new ArrayList());
	}

	public DEActivityLog(String description, String entityName) {
		super(description, entityName);
		this.setItems(new ArrayList());
	}

	public DEActivityLog(String entityName) {
		super();
		this.setItems(new ArrayList());
	}

	
	public String getLogType() {
		return logType;
	}

	public DEActivityLog(CodeValueType cvt) {
		super(cvt);
		// TODO Auto-generated constructor stub
	}


}
