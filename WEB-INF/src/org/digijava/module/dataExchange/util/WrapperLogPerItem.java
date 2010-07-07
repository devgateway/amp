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
		
		sb.append("<Date>");
		if ( lpi.getExecutionTime() != null ) {
			Calendar cal	= Calendar.getInstance();
			cal.setTime(lpi.getExecutionTime() );
			sb.append(cal.get(Calendar.MONTH)+1);
			sb.append( "/" + cal.get(Calendar.DAY_OF_MONTH) );
			sb.append( "/" + cal.get(Calendar.YEAR) );
		}
		sb.append("</Date>");
		
		
		sb.append("<Description>" + lpi.getDescription() + "</Description>");
		
		sb.append("</LogPerItem>");
		
		return sb.toString();
	}

}
