/**
 * 
 */
package org.digijava.module.dataExchange.util;

import java.util.Calendar;

import org.digijava.module.dataExchange.dbentity.DELogPerItem;

/**
 * @author Alex Gartner
 *
 */
public class WrapperLogPerItem implements XmlWrapper {

	private DELogPerItem lpi;
	
	
	public WrapperLogPerItem(DELogPerItem lpi) {
		super();
		this.lpi = lpi;
	}


	/* (non-Javadoc)
	 * @see org.digijava.module.dataExchange.util.XmlWrapper#toXMLString()
	 */
	@Override
	public String toXMLString() {
		StringBuffer sb	= new StringBuffer();
		sb.append("<LogPerItem>");
		sb.append("<Number>" + lpi.getId() + "</Number>");
		sb.append("<LogLevel>" + lpi.getLogType() + "</LogLevel>");
		sb.append("<Name>" + lpi.getName() + "</Name>");
		
		sb.append("<Date>");
		sb.append( lpi.getDateAsString() );
		sb.append("</Date>");
		
		sb.append("<Time>");
		sb.append( lpi.getTimeAsString() );
		sb.append("</Time>");
		
		
		sb.append("<Description>" + lpi.getDescription() + "</Description>");
		
		sb.append("</LogPerItem>");
		
		return sb.toString();
	}

}
