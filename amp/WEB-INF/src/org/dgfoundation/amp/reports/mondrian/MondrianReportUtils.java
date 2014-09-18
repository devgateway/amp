/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.util.Iterator;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.Mappings;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.CurrencyUtil;

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
	
	/**
	 * flushes Mondrian Cache
	 */
	public static void flushCache() {
		new mondrian.rolap.CacheControlImpl(null).flushSchemaCache();
//		try {
//			RolapConnection rolapConn = olapConnection.unwrap(mondrian.rolap.RolapConnection.class);
//			rolapConn.getCacheControl(null).flushSchema(rolapConn.getSchema());
//		}
//		catch (SQLException e) {
//			throw new RuntimeException(e);
//		}
	}
	
	/**
	 * @return default configuration for the current user settings
	 */
	public static MondrianReportSettings getCurrentUserDefaultSettings() {
		MondrianReportSettings settings = new MondrianReportSettings();
		settings.setCurrencyFormat(FormatHelper.getDefaultFormat());
		AmpApplicationSettings ampAppSettings = AmpARFilter.getEffectiveSettings();
		if (ampAppSettings == null)
			settings.setCurrencyCode(CurrencyUtil.getDefaultCurrency().getCurrencyCode());
		else 
			settings.setCurrencyCode(ampAppSettings.getCurrency().getCurrencyCode());
		//TODO: for calendar
		return settings;
	}
	
	/**
	 * Configures default & mandatory behavior
	 * @param spec - report specification
	 */
	public static void configureDefaults(ReportSpecification spec) {
		if (spec instanceof ReportSpecificationImpl) {
			ReportSpecificationImpl s = (ReportSpecificationImpl)spec;
			if (s.getSettings() == null) {
				s.setSettings(getCurrentUserDefaultSettings());
			}
			if (GroupingCriteria.GROUPING_TOTALS_ONLY.equals(s.getGroupingCriteria()))
				s.setCalculateColumnTotals(false);
		}
	}
	
	public static int getColumnId(ReportColumn col, ReportSpecification spec) {
		if (spec == null || spec.getColumns() == null) return -1;
		int colId = 0;
		for (Iterator<ReportColumn> iter = spec.getColumns().iterator(); iter.hasNext(); colId++)
			if (iter.next().equals(col))
				break;
		return colId == spec.getColumns().size() ? -1 : colId; 
	}
}
