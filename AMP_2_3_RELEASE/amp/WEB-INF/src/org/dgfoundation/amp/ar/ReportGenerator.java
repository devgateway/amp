/**
 * ReportGenerator.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpReportColumn;
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
	protected Map<String, CellColumn> rawColumnsByName;
	
	protected GroupReportData report;
	
	protected AmpARFilter filter;
	
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
		long startTS = System.currentTimeMillis();
		retrieveData();
		long retrTS = System.currentTimeMillis();
		
		
		prepareData();
		long endTS = System.currentTimeMillis();
		logger.info("Report "+getReport().getName()+" generated in "+(endTS-startTS)/1000.0+" seconds. Data retrieval completed in "+(retrTS-startTS)/1000.0+" seconds");
	}
	
	
	public ReportGenerator() {
		super();
		rawColumnsByName=new HashMap<String,CellColumn>();
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
	public AmpARFilter getFilter() {
		return filter;
	}

	/**
	 * @param filter The filter to set.
	 */
	public void setFilter(AmpARFilter filter) {
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
