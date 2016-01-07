package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.newreports.ReportSpecification;

public interface SchemaSpecificScratchpad extends AutoCloseable {
	
	/** 
	 * @return the precision with which to run the internal calculations. Should be at least 2 orders of magnitude better than the format used for displaying
	 */
	public NiPrecisionSetting getPrecisionSetting();
	
	/**
	 * 
	 * @return the Calendar to use in a report in case {@link ReportSpecification} does not specify one
	 */
	public CalendarConverter getDefaultCalendar();
}
