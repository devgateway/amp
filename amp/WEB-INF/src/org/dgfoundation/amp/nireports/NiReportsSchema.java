package org.dgfoundation.amp.nireports;

import java.util.Map;

/**
 * an interface describind the Schema of a reports' implementation
 * @author Constantin Dolghier
 *
 */
public interface NiReportsSchema {
	/**
	 * returns the list of columns which exist in the schema
	 * @return
	 */
	public Map<String, NiReportColumn> getColumns();
	
	/**
	 * returns the list of measures which exist in the schema
	 * @return
	 */
	public Map<String, NiReportMeasure> getMeasures();
	
	/**
	 * returns the fetcher of funding
	 * @return
	 */
	public NiReportMeasure getFundingFetcher();
}
