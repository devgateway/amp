/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.Mappings;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.digijava.module.aim.dbentity.AmpReports;

/**
 * Reports utility methods
 * @author Nadejda Mandrescu
 */
public class MondrianReportUtils {
	
	public static ReportEntityType getColumnEntityType(String columnName, ReportEntityType currentEntityType) {
		if (Mappings.ALL_ENTITIES_COLUMNS.contains(columnName))
			return ReportEntityType.ENTITY_TYPE_ALL;
		return currentEntityType;
	}
	
	public static ReportEntityType getMeasuresEntityType(String measureName, ReportEntityType currentEntityType) {
		if (Mappings.ALL_ENTITIES_MEASURES.contains(measureName))
			return ReportEntityType.ENTITY_TYPE_ALL;
		return currentEntityType;
	}
	
	public static ReportEntityType getReportEntityType(AmpReports report) throws AMPException {
		switch (report.getType().intValue()) {
		case ArConstants.DONOR_TYPE:
			//the only supported type for now
			return ReportEntityType.ENTITY_TYPE_ACTIVITY;
		default: 
			throw new AMPException("Not supported report translation for report type: " + report.getType());
		}
	}
	
	public static ReportColumn getColumn(String columnName, ReportEntityType currentEntityType) {
		return new ReportColumn(columnName, getColumnEntityType(columnName, currentEntityType));
	}
	
	public static ReportMeasure getMeasure(String measureName, ReportEntityType currentEntityType) {
		return new ReportMeasure(measureName, getMeasuresEntityType(measureName, currentEntityType));
	}
	
	public static ReportAreaImpl getNewReportArea(Class<? extends ReportAreaImpl> reportAreaType) throws AMPException {
		ReportAreaImpl reportArea = null;
		try {
			reportArea = reportAreaType.newInstance();
		} catch(Exception e) {
			throw new AMPException("Cannot instantiate " + reportAreaType.getName() + ". " + e.getMessage());
		}
		return reportArea;
	}
}
