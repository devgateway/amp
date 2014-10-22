package org.digijava.kernel.ampapi.endpoints.reports;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.converters.AmpReportsToReportSpecification;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.util.DbUtil;

public class ReportsUtil {
	protected static final Logger logger = Logger.getLogger(ReportsUtil.class);

	public static JSONTab convert(AmpReports report, Boolean visible) {
		JSONTab tab = new JSONTab();
		tab.setId(report.getAmpReportId());
		tab.setName(report.getName());
		tab.setVisible(visible);
		return tab;
	}

	/**
	 * Retrieves report configuration for the specified report id 
	 * @param reportId - report id to provide the specification for 
	 * @return ReportSpecification object
	 * @throws AMPException
	 */
	public static ReportSpecificationImpl getReport(Long reportId) {
		AmpReports ampReport = DbUtil.getAmpReport(reportId);
		try {
			return AmpReportsToReportSpecification.convert(ampReport);
		} catch (AMPException e) {
			logger.error(e);
		}
		return null;
	}
	
	/**
	 * Updates report specification with the configuration comming from   
	 * @param spec - the specification that will be updated
	 * @param formParams
	 * @return the updated spec
	 */
	public static ReportSpecification update(ReportSpecification spec, MultivaluedMap<String, String> formParams) {
		if (spec == null || formParams == null) return spec;
		//adding new columns if not present
		if (formParams.containsKey(EPConstants.ADD_COLUMNS)) {
			List<String> columns = formParams.get(EPConstants.ADD_COLUMNS);
			for (String columnName : columns) {
				ReportColumn column = new ReportColumn(columnName);
				if (!spec.getColumns().contains(column)) {
					spec.getColumns().add(column);
				}
			}
		}
		//adding new hierarchies if not present
		if (formParams.containsKey(EPConstants.ADD_HIERARCHIES)) {
			List<String> hierarchies = formParams.get(EPConstants.ADD_HIERARCHIES);
			List<ReportColumn> existingColumns = new ArrayList<ReportColumn>();
			existingColumns.addAll(spec.getColumns());
			for (String columnName : hierarchies) {
				ReportColumn column = new ReportColumn(columnName);
				if (!spec.getHierarchies().contains(column)) {
					//add as a column if not present 
					if (!existingColumns.contains(column)) {
						existingColumns.add(spec.getHierarchies().size(), column);
					}
					spec.getHierarchies().add(column);
				}
			}
			spec.getColumns().clear();
			spec.getColumns().addAll(existingColumns);
		}
		//add new measures if not present
		if (formParams.containsKey(EPConstants.ADD_MEASURES)) {
			List<String> measures = formParams.get(EPConstants.ADD_MEASURES);
			for (String measureName : measures) {
				ReportMeasure measure = new ReportMeasure(measureName);
				if (!spec.getMeasures().contains(measure)) {
					spec.getMeasures().add(measure);		}
			}
		}
		return spec;
	}
}
