/**
 * 
 */
package org.dgfoundation.amp.reports;

import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReports;

/**
 * Utility methods
 * @author Nadejda Mandrescu
 */
public class ReportUtils {
	
	/**
	 * Translation of 
	 * @param report
	 * @return
	 */
	public static ReportSpecificationImpl toReportSpecification(AmpReports report) {
		//basic
		ReportSpecificationImpl spec = new ReportSpecificationImpl(report.getName());
		for (AmpReportColumn column : report.getColumns()) {
			spec.addColumn(new ReportColumn(column.getColumn().getColumnName()));
		}
		for (String measureName: report.getMeasureNames()) {
			spec.addMeasure(new ReportMeasure(measureName));
		}
		spec.setDisplayEmptyFundingColumns(report.getAllowEmptyFundingColumns());
		spec.setDisplayEmptyFundingRows(false); //default expectation
		
		//TODO:
		//report.getAlsoShowPledges()
		//report.getHideActivities()
		//report.getHierarchyNames()
		//report.isSummaryReportNoHierachies()
		//..
		
		return spec;
	}

}
