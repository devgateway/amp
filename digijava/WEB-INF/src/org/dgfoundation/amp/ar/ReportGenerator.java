/**
 * ReportGenerator.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpReports;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 23, 2006
 * The report generator class. This class gathers data from the db, performs
 * processing and creates the structures that need to be displayed.
 *
 */
public abstract class ReportGenerator {

	protected GroupColumn rawColumns;
	protected GroupReportData report;
	
	protected Filter filter;
	
	protected AmpReports reportMetadata;
	
	protected static Logger logger = Logger.getLogger(ReportGenerator.class);
	
	/**
	 * retrieves data from the DB (using views) and creates the basic structures
	 */
	protected abstract void retrieveData();
	
	/**
	 * prepares the extracted data for display
	 */
	protected abstract void prepareData();
	
	/**
	 * retrieve the categories that will serve to create subsets (subColumnS) of
	 * Categorizable CellS
	 * @param columnName the name of the column, to customize the returned category list
	 * in order to suit that particular column
	 * @return the list of category names
	 */
	protected abstract List getColumnSubCategories(String columnName);
	
	/**
	 * the main method of this class. it generates a displayable report object
	 */
	public void generate() {
		retrieveData();
		
		prepareData();
	}
	
	
	public ReportGenerator() {
		super();
	}

	/**
	 * @return Returns the report.
	 */
	public GroupReportData getReport() {
		return report;
	}

	/**
	 * @param report The report to set.
	 */
	public void setReport(GroupReportData report) {
		this.report = report;
	}

	/**
	 * @return Returns the filter.
	 */
	public Filter getFilter() {
		return filter;
	}

	/**
	 * @param filter The filter to set.
	 */
	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	/**
	 * @return Returns the reportMetadata.
	 */
	public AmpReports getReportMetadata() {
		return reportMetadata;
	}

	/**
	 * @param reportMetadata The reportMetadata to set.
	 */
	public void setReportMetadata(AmpReports reportMetadata) {
		this.reportMetadata = reportMetadata;
	}

}
