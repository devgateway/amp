package org.digijava.kernel.ampapi.endpoints.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianSQLFilters;
import org.dgfoundation.amp.utils.ConstantsUtil;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.Filters;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.LoggerIdentifiable;
import org.digijava.module.search.util.SearchUtil;

public class FilterUtils {
	protected static Logger logger = Logger.getLogger(FilterUtils.class);
	
	private static List<String> COLUMN_DATES_FILTER = Collections.unmodifiableList(new ArrayList<>(MondrianSQLFilters.DATE_COLUMNS));
	
	public static MondrianReportFilters getApiOtherFilters(Map<String, Object> filter, MondrianReportFilters filterRules) {
		for (String columnName : COLUMN_DATES_FILTER) {
			if (filter.get(columnName) != null) {
				filterRules = addDateFilterRule(columnName, filter, filterRules);
			}
		}
		if (filter.get("date") != null) {
			filterRules = addDateFilterRule("date", filter, filterRules);
		}

		return filterRules;
	}

	private static MondrianReportFilters addDateFilterRule(String dateColumn, Map<String, Object> filter,
			MondrianReportFilters filterRules) {
		try {
			if (filterRules == null) {
				filterRules = new MondrianReportFilters();
			}
			Map<String, Object> date = (Map<String, Object>) filter.get(dateColumn);
			String start = denull(String.valueOf(date.get("start")));
			String end = denull(String.valueOf(date.get("end")));
			
			if (start != null || end != null) {
				SimpleDateFormat sdf = new SimpleDateFormat(MoConstants.DATE_FORMAT);
				if (COLUMN_DATES_FILTER.contains(dateColumn)) {
					filterRules.addDateRangeFilterRule(new ReportColumn(dateColumn),
							start == null ? null : sdf.parse(start), end == null ? null : sdf.parse(end));
				} else {
					filterRules.addDateRangeFilterRule(start == null ? null : sdf.parse(start), end == null ? null
							: sdf.parse(end));
				}
			}
		} catch (AmpApiException | ParseException e) {
			logger.error("cannot process date", e);
			//throw new RuntimeException(e);
		}
		return filterRules;
	}

	/**
     * returns the original String instance, unless it equals "null", case in which null will be returned
     * @param s
     * @return
     */
	public static String denull(String s) {
		if (StringUtils.isNotBlank(s) && !s.equalsIgnoreCase("null")) return s;
		return null;
	}

	/**
	 * returns a MondrianReportFilters based on the End point parameter
	 * 
	 * @param filter
	 * @return
	 */
	public static MondrianReportFilters getApiColumnFilter(LinkedHashMap<String, Object> filter, 
			MondrianReportFilters filterRules) {
		if (filter == null) {
			return filterRules;
		}
		if (filterRules == null) {
			filterRules = new MondrianReportFilters();
		}
		Set<String> validColumns = ConstantsUtil.getConstantsSet(ColumnConstants.class);
		for (Entry<String, Object> entry : filter.entrySet()) {
			if (validColumns.contains(entry.getKey())) {
				if (entry.getValue() instanceof List) {
					List<String> ids = getStringsFromArray((List<?>) filter.get(entry.getKey()));
					filterRules.addFilterRule(new ReportColumn(entry.getKey()), new FilterRule(ids, true)); 
				} else 
				if (entry.getValue() != null) {
					String value = entry.getValue().toString();
					filterRules.addFilterRule(new ReportColumn(entry.getKey()), new FilterRule(value, true));
				}
			}
		}
		
		return filterRules;
	}
	
	private static List<String> getStringsFromArray(List<?> theArray) {
		List<String> s = new ArrayList<String>();
		for (Object obj : theArray) {
			if (obj != null) {
				if(Filters.ANY_BOOLEAN.equals(obj.toString())) {
					s.add(FilterRule.FALSE_VALUE);
					s.add(FilterRule.TRUE_VALUE);
				} else {
					s.add(obj.toString());
				}
			}
		}
		return s;
	}
	
	public static List<String> applyKeywordSearch(LinkedHashMap<String, Object> otherFilter) {
		List<String> activitIds = new ArrayList<String>();

		if (otherFilter!=null && otherFilter.get("keyword") != null) {
			String keyword = ((Map<String,Object>)otherFilter).get("keyword").toString();
			Collection<LoggerIdentifiable> activitySearch = SearchUtil
					.getActivities(keyword,
							TLSUtils.getRequest(), (TeamMember) TLSUtils.getRequest().getSession().getAttribute("currentMember"));
			if (activitySearch != null && activitySearch.size() > 0) {
				for (LoggerIdentifiable loggerIdentifiable : activitySearch) {
					activitIds.add(loggerIdentifiable.getIdentifier().toString());
				}
			}
		}
		return activitIds;
	}
	
	public static MondrianReportFilters getFilterRules(LinkedHashMap<String, Object> columnFilter, 
			LinkedHashMap<String, Object> otherFilter, List<String> activityIds) {
		return getFilterRules(columnFilter, otherFilter, activityIds, null);
	}
			
	public static MondrianReportFilters getFilterRules(LinkedHashMap<String, Object> columnFilter, 
			LinkedHashMap<String, Object> otherFilter, List<String> activityIds, MondrianReportFilters filterRules) {
			if(columnFilter!=null){
				filterRules = FilterUtils.getApiColumnFilter(columnFilter, filterRules);	
			}
			if(otherFilter!=null){
				filterRules = FilterUtils.getApiOtherFilters(otherFilter, filterRules);
			}
		if(activityIds!=null && activityIds.size()>0){
			//if we have activityIds to add to the filter comming from the search by keyworkd
			if(filterRules==null){
				filterRules = new MondrianReportFilters();
			}

			filterRules.addFilterRule(new ReportColumn(ColumnConstants.ACTIVITY_ID), new FilterRule(activityIds, true)); 

		}
		return filterRules;
	}
	
	/**
	 * Builds MondrianReportFilters based on the json filters request
	 * @param filtersConfig
	 * @return MondrianReportFilters
	 * @see #getFilters(JsonBean, List)
	 */
	public static MondrianReportFilters getFilters(JsonBean filtersConfig, MondrianReportFilters filters) {
		return getFilters(filtersConfig, null, filters);
	}
	
	/**
	 * Builds MondrianReportFilters based on the json filters request and additional options
	 * @param filtersConfig json filters config request
	 * @param activitIds    the list of activities to filter by
	 * @return MondrianReportFilters
	 */
	public static MondrianReportFilters getFilters(JsonBean filtersConfig, List<String> activitIds,
			MondrianReportFilters filters) {
		
		//we check if we have filter by keyword
		LinkedHashMap<String, Object> otherFilter=null;
		if (filtersConfig != null) {
			otherFilter = (LinkedHashMap<String, Object>) filtersConfig.get("otherFilters");
			if(activitIds == null){
				activitIds = new ArrayList<String>();
			}
			activitIds.addAll(FilterUtils.applyKeywordSearch( otherFilter));
		}
		
		filters = FilterUtils.getFilterRules(
				(LinkedHashMap<String, Object>) filtersConfig.get("columnFilters"),
				otherFilter, activitIds, filters);
		
		return filters;
	}
	
	/**
	 * @param status
	 * @return
	 */
	public static String getApprovalStatusByNumber(Integer status){
		for (Entry<String, Integer> entry : AmpARFilter.activityApprovalStatus.entrySet()) {
			if (entry.getValue().equals(status)) {
				return entry.getKey();
			}
		}
		
		return "";
	}
	
	/**
	 * 
	 * @param JsonBean 
	 * @param value
	 * @return
	 */
	public static String getSettingbyName(JsonBean config, String value){
		Map<String, Object> settings = config == null ? null : (Map<String, Object>) config.get(EPConstants.SETTINGS);
		String retval = settings == null ? null : (String) settings.get(value);
		return retval;
	}

	/**
	 * Apply filterRules. In case the spec already have filterRules, append them
	 * 
	 * @param config
	 * @param spec
	 */
	public static void applyFilterRules(JsonBean config, ReportSpecificationImpl spec, Integer months) {
		MondrianReportFilters filterRules = FilterUtils.getFilters(config, (MondrianReportFilters) spec.getFilters());
		if (months != null) {
			Calendar cal = Calendar.getInstance();
			Calendar currentCal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -months);

			try {
				if (filterRules == null) {
					filterRules = new MondrianReportFilters();
				}
				filterRules.addDateRangeFilterRule(cal.getTime(), currentCal.getTime());
			} catch (AmpApiException e) {
				logger.error(e);

			}
		}
		if (filterRules != null) {
			spec.setFilters(filterRules);
		}
	}

}
