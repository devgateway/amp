package org.digijava.kernel.ampapi.endpoints.gis.services;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;

/**
 * 
 * @author Diego Dimunzio
 *
 */

public class LocationService {
	
	/**
	 * Get totals (actual commitments/ actual disbursements) by administrative level
	 * @param admlevel
	 * @param type
	 * @return
	 */
	public GeneratedReport getTotals(String admlevel, String type) {
		String err = null;
		
		switch (admlevel) {
		case "adm0":
			admlevel = ColumnConstants.COUNTRY; 
			break;
		case "adm1":
			admlevel = ColumnConstants.REGION; 
			break;
		case "adm2":
			admlevel = ColumnConstants.ZONE; 
			break;
		case "adm3":
			admlevel = ColumnConstants.DISTRICT; 
			break;
		default:
			admlevel = ColumnConstants.REGION; 
			break;
		}
		
		switch (type) {
		case "ac":
			type = MeasureConstants.ACTUAL_COMMITMENTS; 
			break;
		case "ad":
			type = MeasureConstants.ACTUAL_DISBURSEMENTS; 
			break;
		default:
			type = MeasureConstants.ACTUAL_COMMITMENTS;
			break;
		}
		ReportSpecificationImpl spec = new ReportSpecificationImpl("LocationsTotals");
		spec.addColumn(new ReportColumn(admlevel, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(type, ReportEntityType.ENTITY_TYPE_ALL));
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class, new ReportEnvironment("en", null),false);
		GeneratedReport report = null;
		try {
			report = generator.executeReport(spec);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			err = e.getMessage();
		}
		return report;
	}
}
