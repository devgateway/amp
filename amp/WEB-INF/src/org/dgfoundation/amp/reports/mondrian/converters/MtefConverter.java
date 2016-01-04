package org.dgfoundation.amp.reports.mondrian.converters;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.mondrian.MondrianETL;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.onepager.models.MTEFYearsModel;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportSpec;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.KeyValue;
import org.hibernate.jdbc.Work;

/**
 * singleton class used for injecting into a ReportSpecificationImpl the data needed for including the MTEF columns as measures in the Mondrian reports implementation
 * @author Dolghier Constantin
 *
 */
public class MtefConverter {
	public final static MtefConverter instance = new MtefConverter();
	
	/**
	 * Map<mtef-first-year-in-range, Pair<day-code-of-first-day-in-year, day-code-of-last-day-in-year>>. Year is in the system's Calendar (this is historical behaviour, should normally be the AMP installation's default calendar)
	 */
	public final Map<Integer, YearMtefInfo> mtefInfos;
	
	/**
	 * the element types which are MTEF filtering elements (those are special and hacky)
	 */
	public final static Set<ReportElement.ElementType> MTEF_DATE_ELEMENT_TYPES = Collections.unmodifiableSet(
			new HashSet<>(Arrays.asList(ReportElement.ElementType.MTEF_DATE, ReportElement.ElementType.REAL_MTEF_DATE, 
					ReportElement.ElementType.PIPELINE_MTEF_DATE, ReportElement.ElementType.PROJECTION_MTEF_DATE)));

	
	public class YearMtefInfo {
		public final int periodStartDayCode;
		public final int periodEndDayCode;
		
		public YearMtefInfo(int periodStartDayCode, int periodEndDayCode) {
			this.periodStartDayCode = periodStartDayCode;
			this.periodEndDayCode = periodEndDayCode;
		}
		
		@Override public String toString() {
			return String.format("(%d - %d)", periodStartDayCode, periodEndDayCode);
		}
	}
	
	private MtefConverter() {
		final SortedMap<Integer, YearMtefInfo> mtefInfos = new TreeMap<>();
		
		boolean isFiscal = MTEFYearsModel.getFiscal();
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.DAY_OF_YEAR, 1);
		for(int year = ArConstants.MIN_SUPPORTED_YEAR - 100; year <= ArConstants.MAX_SUPPORTED_YEAR + 100; year++) {
			int day_code = MondrianETL.MTEF_RANGES_START_DAY_CODE + year;
//			calendar.set(Calendar.YEAR, year);
			KeyValue kv = MTEFYearsModel.convert(year, isFiscal);
			int yearCode = kv.getKeyAsLong().intValue() - 0;
			mtefInfos.put(yearCode, new YearMtefInfo(day_code, day_code));
		}
//		PersistenceManager.getSession().doWork(new Work() {
//			@Override public void execute(Connection connection) throws SQLException {
//				String query = String.format("SELECT yr, to_char((yr || '-01-01')::date, 'J')::integer AS year_start_day_code, " + 
//						"to_char((yr+1 || '-01-01')::date, 'J')::integer - 1 AS year_end_day_code " + 
//						"FROM generate_series(%d, %d) yr", ArConstants.MIN_SUPPORTED_YEAR, ArConstants.MAX_SUPPORTED_YEAR);
//
//				try(RsInfo rsi = SQLUtils.rawRunQuery(connection, query, null)) {
//					while (rsi.rs.next()) {
//						int yearNr = rsi.rs.getInt(1);
//						int firstDay = rsi.rs.getInt(2);
//						int lastDay = rsi.rs.getInt(3);
//						mtefInfos.put(yearNr, new YearMtefInfo(firstDay, lastDay));
//					}
//				}
//			}});
		
		this.mtefInfos = Collections.unmodifiableMap(mtefInfos);
	}
	
	public static int getMtefStartDayCode() {
		return MondrianETL.MTEF_RANGES_START_DAY_CODE + ArConstants.MIN_SUPPORTED_YEAR - 100;
	}
	
	public static int getMtefEndDayCode() {
		return MondrianETL.MTEF_RANGES_START_DAY_CODE + ArConstants.MAX_SUPPORTED_YEAR + 100;
	}	
	
	public List<FilterRule> buildRulesFor(SortedSet<Integer> years) {
		if (years.isEmpty()) return null; // nothing to do

		// build list of items to add
		List<FilterRule> filterRules = new ArrayList<>();
		for(int mtefYear:years) {
			YearMtefInfo yearInfo = this.mtefInfos.get(mtefYear);
			filterRules.add(new FilterRule(
					Integer.toString(yearInfo.periodStartDayCode),
					Integer.toString(yearInfo.periodStartDayCode),
				true, true));
		}
		return filterRules;
	}
}
