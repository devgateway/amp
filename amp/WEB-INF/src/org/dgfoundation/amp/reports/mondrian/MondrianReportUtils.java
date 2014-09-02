/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.util.LinkedHashSet;
import java.util.Set;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.Mappings;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReports;

/**
 * Reports utility methods
 * @author Nadejda Mandrescu
 */
public class MondrianReportUtils {
	
	/**
	 * Translation of {@link AmpReports} report to Reports API report structure 
	 * @param report - {@link AmpReports} 
	 * @return {@link ReportSpecificationImpl}
	 * @throws AMPException 
	 */
	public static ReportSpecificationImpl toReportSpecification(AmpReports report) throws AMPException {
		ReportEntityType entityType = getReportEntityType(report);
		
		ReportSpecificationImpl spec = new ReportSpecificationImpl(report.getName());
		//configure report
		configureReportData(spec, report, entityType);
		configureHierarchies(spec, report, entityType);		
		
		//configure filters & settings
		AmpARFilter arFilter = FilterUtil.buildFilter(report, report.getAmpReportId());
		AmpARFilterTranslator arFilterTranslator = new AmpARFilterTranslator(arFilter); 
		spec.setFilters(arFilterTranslator.buildFilters());
		spec.setSettings(arFilterTranslator.buildSettings());
		
		//TODO:
		//report.getAlsoShowPledges()
		//report.getHideActivities()
		//report.isSummaryReportNoHierachies()
		//..
		
		return spec;
	}
	
	private static void configureReportData(ReportSpecificationImpl spec, AmpReports report, ReportEntityType entityType) {
		for (AmpReportColumn column : report.getColumns()) {
			spec.addColumn(new ReportColumn(column.getColumn().getColumnName(), getColumnEntityType(column.getColumn().getColumnName(), entityType)));
		}
		for (String measureName: report.getMeasureNames()) {
			spec.addMeasure(new ReportMeasure(measureName, getMeasuresEntityType(measureName, entityType)));
		}
		//default expectations
		spec.setDisplayEmptyFundingColumns(report.getAllowEmptyFundingColumns());
		spec.setDisplayEmptyFundingRows(false); 
		
		spec.setCalculateColumnTotals(true);
		spec.setCalculateRowTotals(true);
		
		switch(report.getOptions()) {
		case "A": spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY); break;
		case "Q": spec.setGroupingCriteria(GroupingCriteria.GROUPING_QUARTERLY); break;
		case "M": spec.setGroupingCriteria(GroupingCriteria.GROUPING_MONTHLY); break;
		default: spec.setGroupingCriteria(GroupingCriteria.GROUPING_TOTALS_ONLY); break;
		}
	}
	
	private static void configureHierarchies(ReportSpecificationImpl spec, AmpReports report, ReportEntityType entityType) {
		spec.setColsHierarchyTotals(0);
		spec.setRowsHierarchiesTotals(report.getHierarchies().size());

		Set<ReportColumn> hierarchies = new LinkedHashSet<ReportColumn>(report.getHierarchies().size());
		for (AmpReportHierarchy column : report.getHierarchies()) {
			hierarchies.add(new ReportColumn(column.getColumn().getColumnName(), getColumnEntityType(column.getColumn().getColumnName(), entityType)));
		}
		spec.setHierarchies(hierarchies);
	}
	
	private static ReportEntityType getColumnEntityType(String columnName, ReportEntityType currentEntityType) {
		if (Mappings.ALL_ENTITIES_COLUMNS.contains(columnName))
			return ReportEntityType.ENTITY_TYPE_ALL;
		return currentEntityType;
	}
	
	private static ReportEntityType getMeasuresEntityType(String measureName, ReportEntityType currentEntityType) {
		if (Mappings.ALL_ENTITIES_MEASURES.contains(measureName))
			return ReportEntityType.ENTITY_TYPE_ALL;
		return currentEntityType;
	}
	
	private static ReportEntityType getReportEntityType(AmpReports report) throws AMPException {
		switch (report.getType().intValue()) {
		case ArConstants.DONOR_TYPE:
			//the only supported type for now
			return ReportEntityType.ENTITY_TYPE_ACTIVITY;
		default: 
			throw new AMPException("Not supported report translation for report type: " + report.getType());
		}

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
