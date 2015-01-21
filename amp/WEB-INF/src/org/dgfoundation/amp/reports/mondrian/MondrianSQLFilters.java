package org.dgfoundation.amp.reports.mondrian;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dgfoundation.amp.ar.ApprovalStatusQueryBuilder;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.SQLQueryGenerator;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.digijava.module.aim.util.ActivityUtil;

import clover.org.apache.log4j.Logger;

/**
 * Applies all the SQL filters for the selected date filters (Map<ReportColumn,
 * List<FilterRule>>) and filters (Map<ReportElement, List<FilterRule>>) and
 * returns the ids of the activities that match the criteria
 * 
 * @author eperez
 * 
 */
public class MondrianSQLFilters {

	private static final Logger LOGGER = Logger.getLogger(MondrianSQLFilters.class);
	@SuppressWarnings("serial")
	private static final Map<String, String> SQL_DATES_COLUMN_NAMES_MAP = new HashMap<String, String>() {
		{
			put(ColumnConstants.ORIGINAL_COMPLETION_DATE, "original_comp_date");
			put(ColumnConstants.FINAL_DATE_FOR_CONTRACTING, "contracting_date");
			put(ColumnConstants.FINAL_DATE_FOR_DISBURSEMENTS, "disbursments_date");
			put(ColumnConstants.PROPOSED_START_DATE, "proposed_start_date");
			put(ColumnConstants.ACTUAL_START_DATE, "actual_start_date");
			put(ColumnConstants.PROPOSED_APPROVAL_DATE, "proposed_approval_date");
			put(ColumnConstants.ACTUAL_APPROVAL_DATE, "actual_approval_date");
			put(ColumnConstants.ACTUAL_COMPLETION_DATE, "actual_completion_date");
			put(ColumnConstants.PROPOSED_COMPLETION_DATE, "proposed_completion_date");
			put(ColumnConstants.ACTIVITY_CREATED_ON, "date_created");
			put(ColumnConstants.ACTIVITY_UPDATED_ON, "date_updated");
		}
	};

	// Each ColumnConstant should have it's SQLQueryGenerator
	public static final Map<String, Class<? extends SQLQueryGenerator>> FILTER_RULE_CLASSES_MAP = new HashMap<String, Class<? extends SQLQueryGenerator>>() {
		{
			put(ColumnConstants.APPROVAL_STATUS, ApprovalStatusQueryBuilder.class);
		}
	};

	/**
	 * Executes the SQL filters and returns the activities ids that match the
	 * criteria
	 * 
	 * @param dateFilters
	 * @param filters
	 * @return Set with the list of activities ids, null if not filter was applied
	 */
	public static Set<Long> getActivityIds(Map<ReportColumn, List<FilterRule>> dateFilters,
			Map<ReportElement, List<FilterRule>> filters) {
		Set<Long> activityIds = null;

		// first generate the sql to filter by date

		String dateFiltersQuery = generateDateColumnsFilterQuery(dateFilters);
		if (dateFiltersQuery != null) {
			if (activityIds == null) {
				activityIds = new HashSet<Long>();
			}
			activityIds.addAll(ActivityUtil.fetchLongs(dateFiltersQuery));
		}

		// then generate the sql for other fields
		for (String entityName : FILTER_RULE_CLASSES_MAP.keySet()) {
			// check if a given column should be filtered
			List<FilterRule> rules = getFilterRules(filters, entityName);
			if (rules == null || rules.size() == 0) {
				continue;
			}
			Map<String, Boolean> ids = getIdsFromFilterRule(rules);
			SQLQueryGenerator sqlQueryGenerator;
			try {
				// get the class that is in charge of generating the sql query
				sqlQueryGenerator = FILTER_RULE_CLASSES_MAP.get(entityName).newInstance();
				String filterQuery = sqlQueryGenerator.generateSQLQuery(ids);
				if (filterQuery != null) {
					if (activityIds == null) {
						activityIds = new HashSet<Long>();
					}
					activityIds.addAll(ActivityUtil.fetchLongs(filterQuery));
				}

			} catch (InstantiationException | IllegalAccessException e) {
				LOGGER.warn("Could not generate the sql query for " + entityName, e);
			}
		}
		return activityIds;
	}

	private static List<FilterRule> getFilterRules(Map<ReportElement, List<FilterRule>> filters, String entityName) {
		for (Iterator<Entry<ReportElement, List<FilterRule>>> entryIter = filters.entrySet().iterator(); entryIter
				.hasNext();) {
			Entry<ReportElement, List<FilterRule>> entry = entryIter.next();
			if (entry.getKey().entity != null && entityName.equals(entry.getKey().entity.getEntityName())) {
				return entry.getValue();
			}
		}
		return null;

	}

	private static Map<String, Boolean> getIdsFromFilterRule(List<FilterRule> rules) {
		Map<String, Boolean> idsMap = new HashMap<String, Boolean>();
		for (FilterRule rule : rules) {
			switch (rule.filterType) {
			case RANGE:
				throw new RuntimeException("range filter for ids makes no sense");

			case SINGLE_VALUE:
				idsMap.put(rule.value, true);
				break;
			case VALUES:
				if (rule.values != null && rule.values.size() > 0) {
					for (Iterator<String> iter = rule.values.iterator(); iter.hasNext();) {
						idsMap.put(iter.next(), rule.valuesInclusive);
					}
					break;
				}

			}
		}
		return idsMap;

	}

	/**
	 * Generates plain SQL query filter by date columns over AMP DB
	 * 
	 * @param filters
	 *            - date columns filters map
	 * @return query to retrieve the list of activities based on date column
	 *         filters
	 */
	private static String generateDateColumnsFilterQuery(Map<ReportColumn, List<FilterRule>> filters) {
		if (filters == null || filters.size() == 0)
			return null;
		StringBuilder filterStr = new StringBuilder();
		boolean areRealFilters = false;
		final String or = " OR ";
		final String and = " AND ";
		// DO NOT REMOVE!!!
		final String select = "SELECT amp_activity_id FROM amp_activity_version WHERE ";

		for (Iterator<Entry<ReportColumn, List<FilterRule>>> entryIter = filters.entrySet().iterator(); entryIter
				.hasNext();) {
			Entry<ReportColumn, List<FilterRule>> entry = entryIter.next();
			final String sqlColumnName = SQL_DATES_COLUMN_NAMES_MAP.get(entry.getKey().getColumnName());
			// group same date column filters by OR rule
			if (entry.getValue().size() > 1)
				filterStr.append("(");
			for (Iterator<FilterRule> iter = entry.getValue().iterator(); iter.hasNext();) {
				final String dateFilter = buildDatesFilter(sqlColumnName, iter.next());
				if (dateFilter != null) {
					areRealFilters = true;
					filterStr.append(dateFilter);
					if (iter.hasNext())
						filterStr.append(or);
				}
			}
			if (entry.getValue().size() > 1)
				filterStr.append(")");
			// add AND rule for different date column filter
			if (entryIter.hasNext())
				filterStr.append(and);
		}

		if (areRealFilters)
			return select + filterStr.toString();

		return null;
	}

	private static String buildDatesFilter(String columnName, FilterRule rule) {
		StringBuilder result = new StringBuilder();
		String sqlColumnName = getJulianCodeSql(columnName);

		switch (rule.filterType) {
		case RANGE:
			if (rule.min != null)
				result.append(sqlColumnName).append(rule.minInclusive ? " >= " : " > ").append(rule.min);
			if (rule.min != null && rule.max != null)
				result.append(" AND ");
			if (rule.max != null)
				result.append(sqlColumnName).append(rule.maxInclusive ? " <= " : " < ").append(rule.max);
			break;

		case SINGLE_VALUE:
			result.append(sqlColumnName).append(" = ").append(rule.value);
			break;

		case VALUES:
			if (rule.values != null && rule.values.size() > 0) {
				final String sep = ", ";
				result.append(sqlColumnName).append(rule.valuesInclusive ? " IN " : " NOT IN ").append(" (");
				for (Iterator<String> iter = rule.values.iterator(); iter.hasNext();) {
					result.append(iter.next());
					if (iter.hasNext())
						result.append(sep);
				}
				result.append(")");
			}
			break;
		}
		return result.length() == 0 ? null : result.toString();
	}

	private static String getJulianCodeSql(String columnName) {
		return String.format("to_char(%s, 'J')::integer", columnName);
	}

}
