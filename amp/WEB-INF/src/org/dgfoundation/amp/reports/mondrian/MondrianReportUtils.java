/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.reports.CustomMeasures;
import org.dgfoundation.amp.reports.DateColumns;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.dgfoundation.amp.visibility.data.MeasuresVisibility;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.ampapi.mondrian.util.MondrianMapping;
import org.digijava.kernel.ampapi.saiku.SaikuGeneratedReport;
import org.digijava.kernel.ampapi.saiku.SaikuReportSorter;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.DbUtil;

/**
 * Reports utility methods
 * 
 * @author Nadejda Mandrescu
 */
public class MondrianReportUtils {
	protected static final Logger logger = Logger.getLogger(MondrianReportUtils.class);
	
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
		if (ampAppSettings == null) {
			settings.setCalendar(DbUtil.getAmpFiscalCalendar(DbUtil.getBaseFiscalCalendar()));
		} else { 
			settings.setCalendar(ampAppSettings.getFiscalCalendar());
		}
		return settings;
	}
	
	public static MondrianReportFilters getCurrentUserDefaultFilters(MondrianReportFilters oldFilters) {
		MondrianReportFilters filters = oldFilters != null ? oldFilters : new MondrianReportFilters(); 
		if (oldFilters != null && oldFilters.getCalendar() != null) {
			filters.setCalendar(oldFilters.getCalendar());
		} else {
			// configure default
			filters.setCalendar(AmpARFilter.getDefaultCalendar());
		}
		return filters;
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
	
	/**
	 * Retrieves column index for the specified column from the given ReportSpecification
	 * @param col
	 * @param spec
	 * @return
	 */
	public static int getColumnId(ReportColumn col, ReportSpecification spec) {
		if (spec == null || spec.getColumns() == null) return -1;
		int colId = 0;
		for (Iterator<ReportColumn> iter = spec.getColumns().iterator(); iter.hasNext(); colId++)
			if (iter.next().equals(col))
				break;
		return colId == spec.getColumns().size() ? -1 : colId; 
	}
	
	/**
	 * Filters out null dates. 
	 * The current solution is add an explicit upper limit to filter by if no filter is already configured for the date. 
	 * @param spec
	 */
	public static void filterOutNullDates(ReportSpecificationImpl spec) {
		MondrianReportFilters filters = spec.getFilters() == null ? new MondrianReportFilters() : (MondrianReportFilters)spec.getFilters();
		Set<ReportColumn> existingFilters = new HashSet<ReportColumn>();
		existingFilters.addAll(filters.getDateFilterRules().keySet());
		for(ReportColumn column : spec.getColumns()) {
			if (DateColumns.ACTIVITY_DATES.contains(column.getColumnName()) && !existingFilters.contains(column)) {
				try {
					filters.addDateRangeFilterRule(column, null, new Date(MoConstants.UNDEFINED_KEY -1));
				} catch (AmpApiException e) {
					logger.error(e);
				}
				existingFilters.add(column);
			}	
		}
		spec.setFilters(filters);
	}
	
	/**
	 * Applies sorting over generated report
	 * @param report
	 * @throws AMPException
	 */
	public static final void sort(GeneratedReport report) throws AMPException {
		ReportEnvironment env = ReportEnvironment.buildFor(TLSUtils.getRequest());
		if (SaikuGeneratedReport.class.isAssignableFrom(report.getClass()))
			SaikuReportSorter.sort(report, env);
		else 
			MondrianReportSorter.sort(report, env);
	}
	
	/**
	 * Verifies if a list of 2 sorting lists are identical
	 * @param sorting1
	 * @param sorting2
	 * @return true if they are equal
	 */
	public static boolean equals(List<SortingInfo> sorting1, List<SortingInfo> sorting2) {
		boolean equals = false;
		if ((sorting1 == null || sorting1.size() == 0) && (sorting2 == null || sorting2.size() == 0))
			equals = true;
		else if (sorting1 != null && sorting2 != null && sorting1.size() == sorting2.size()) {
			equals = sorting1.equals(sorting2);
		}
		return equals;
	}
	
	public static Set<String> getConfigurableColumns() {
		Set<String> configurableColumns = new HashSet<String>(ColumnsVisibility.getVisibleColumns());
		configurableColumns.retainAll(MondrianMapping.definedColumns);
		return configurableColumns;
	}
	
	public static Set<String> getConfigurableMeasures() {
		Set<String> configurableMeasures = new HashSet<String>(
				MeasuresVisibility.getVisibleMeasures());
		configurableMeasures.retainAll(MondrianMapping.definedMeasures);
		return configurableMeasures;
	}
			
	/**
	 * returns a list of all the ACVL IDs 
	 * @param geoid
	 * @return
	 */
	public static List<String> geoIdToLocationIds(String geoid) {
		List<?> acvlIds = PersistenceManager.getSession().createSQLQuery("SELECT id FROM amp_category_value_location WHERE geo_code=" + SQLUtils.stringifyObject(geoid)).list();

		List<String> res = new ArrayList<>();
		for(Object geoId:acvlIds)
			if (geoId != null)
				res.add(PersistenceManager.getLong(geoId).toString());
		return res;
	}
	
	/**
	 * switches geocodes for location IDs
	 * @param in
	 * @return
	 */
	public static FilterRule postprocessGeocodeRule(FilterRule in) {
		if (in == null) return null;
		Set<String> locationIds = new HashSet<>();
		
		switch(in.filterType) {
			case RANGE:
				throw new RuntimeException("no range for geoids!");
				
			case SINGLE_VALUE:
				locationIds.addAll(geoIdToLocationIds(in.value));
				break;
				
			case VALUES:
				for(String value:in.values)
					locationIds.addAll(geoIdToLocationIds(value));
				break;
		}
		return new FilterRule(new ArrayList<>(locationIds), in.valuesInclusive);
	}
	
	public static boolean isDateColumn(String columnName) {
		return DateColumns.ACTIVITY_DATES.contains(columnName);
	}
	
	/**
	 * Retrieves order indexes of measures without column row totals   
	 * @param spec
	 * @return set of indexes
	 */
	public static Set<Integer> getEmptyColTotalsMeasuresIndexes(ReportSpecification spec) {
		Set<Integer> orderIds = new TreeSet<Integer>();
		if (spec != null) {
			int pos = 0;
			for (ReportMeasure rm : spec.getMeasures()) {
				if (CustomMeasures.NO_RAW_TOTALS.contains(rm.getMeasureName())) {
					orderIds.add(pos);
				}
				pos++;
			}
		}
		return orderIds;
	}
	
	/**
	 * Retrieves order indexes of measures without row totals 
	 * @param leafHeaders
	 * @return
	 */
	public static Set<Integer> getEmptyRowTotalsMeasuresIndexes(ReportSpecification spec, List<ReportOutputColumn> leafHeaders) {
		boolean isTotalsOnly = GroupingCriteria.GROUPING_TOTALS_ONLY.equals(spec.getGroupingCriteria());
		Set<Integer> orderIds = new TreeSet<Integer>();
		if (spec != null && leafHeaders != null) {
			int pos = 0;
			for (ReportOutputColumn roc : leafHeaders) {
				if (CustomMeasures.NO_RAW_TOTALS.contains(roc.originalColumnName) 
						&& (isTotalsOnly || roc.parentColumn != null 
								&& !MoConstants.TOTAL_MEASURES.equals(roc.parentColumn.originalColumnName))) {
					orderIds.add(pos - spec.getColumns().size());
				}
				pos++;
			}
		}
		return orderIds;
	}
	
	
	// this call makes test cases much slower and I expect an impact on the first report
//	/**
//	 * Cleans up ReportSpecification from columns unsupported by Mondrian 
//	 * @param spec
//	 */
//	public static void removeUnsupportedColumns(ReportSpecification spec) {
//		// there can be also some measures automatically moved as computed columns in Mondrian
//		Set<String> allowedColumns = getConfigurableMeasures();
//		allowedColumns.retainAll(CustomAmounts.ACTIVITY_AMOUNTS);
//		allowedColumns.addAll(getConfigurableColumns());
//		
//		removeUnsupportedColumns(spec.getColumns(), allowedColumns);
//		removeUnsupportedColumns(spec.getHierarchies(), allowedColumns);
//	}
//	
//	protected static void removeUnsupportedColumns(Set<ReportColumn> columnsSet, Set<String> allowedColumns) {
//		for(Iterator<ReportColumn> iter = columnsSet.iterator(); iter.hasNext(); ) {
//			if (!allowedColumns.contains(iter.next().getColumnName())) {
//				iter.remove();
//			}
//		}
//	}
	
}
