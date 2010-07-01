package org.digijava.module.dataExchange.util;

import java.util.Calendar;

import org.digijava.module.dataExchange.dbentity.DELogPerExecution;

public class WrapperLogPerExecution implements XmlWrapper{
	private DELogPerExecution lpe;

	public WrapperLogPerExecution(DELogPerExecution lpe) {
		super();
		this.lpe = lpe;
	}
	
	@Override
	public String toXMLString () {
		StringBuffer sb	= new StringBuffer();
		sb.append("<LogPerExecution>");
		
		sb.append("<Date>");
		if ( lpe.getExecutionTime() != null ) {
			Calendar cal	= Calendar.getInstance();
			cal.setTime(lpe.getExecutionTime() );
			sb.append(cal.get(Calendar.MONTH)+1);
			sb.append( "/" + cal.get(Calendar.DAY_OF_MONTH) );
			sb.append( "/" + cal.get(Calendar.YEAR) );
		}
		sb.append("</Date>");
		
		sb.append("<ExternalTimestamp>" + lpe.getExternalTimestamp() + "</ExternalTimestamp>");
		
		sb.append("<Description>" + lpe.getDescription() + "</Description>");
		
		sb.append("</LogPerExecution>");
		
		return sb.toString();
	}
}
