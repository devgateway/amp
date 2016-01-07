package org.dgfoundation.amp.nireports.schema;

import org.dgfoundation.amp.nireports.DatedCell;

/**
 * a specification of the behaviour of a given {@link NiReportColumn} / {@link NiReportMeasure}
 * @author Dolghier Constantin
 *
 */
public interface Behaviour {
	
	/**
	 * @return the maximum supported resolution. For any result which is not NONE, the column should contain cells which implement {@link DatedCell}
	 */
	public TimeRange getTimeRange();
	
	public default String getDebugDigest() {
		return String.format("timeRange: %s", getTimeRange());
	}
}
