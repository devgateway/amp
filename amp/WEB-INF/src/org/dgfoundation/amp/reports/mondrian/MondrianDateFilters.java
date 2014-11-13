/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;

/**
 * Mondrian DB utility class  
 * @author Nadejda Mandrescu
 *
 */
public class MondrianDateFilters {
	@SuppressWarnings("serial")
	private static final Map<String, String> SQL_COLUMN_NAMES_MAP = new HashMap<String, String>() {{
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
	}};
	
	/**
	 * Generates plain SQL query filter by date columns over AMP DB
	 * @param filters - date columns filters map
	 * @return query to retrieve the list of activities based on date column filters
	 */
	public static String generateDateColumnsFilterQuery(Map<ReportColumn, List<FilterRule>> filters) {
		if (filters == null || filters.size() == 0) return null;
		StringBuilder filterStr = new StringBuilder();
		boolean areRealFilters = false;
		final String or = " OR ";
		final String and = " AND ";
		//DO NOT REMOVE!!!
		final String select = "SELECT amp_activity_id FROM amp_activity WHERE ";
		
		for (Iterator<Entry<ReportColumn, List<FilterRule>>> entryIter = filters.entrySet().iterator(); entryIter.hasNext(); ) {
			Entry<ReportColumn, List<FilterRule>> entry = entryIter.next();
			final String sqlColumnName = SQL_COLUMN_NAMES_MAP.get(entry.getKey().getColumnName());
			//group same date column filters by OR rule
			if (entry.getValue().size() > 1)
				filterStr.append("("); 
			for (Iterator<FilterRule> iter = entry.getValue().iterator(); iter.hasNext(); ) {
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
			//add AND rule for different date column filter
			if (entryIter.hasNext())
				filterStr.append(and);
		}
		if (areRealFilters)
			return select + filterStr.toString();
		return null;
	}
	
	private static final String buildDatesFilter(String sqlColumnName, FilterRule rule) {
		StringBuilder result = new StringBuilder();
		switch(rule.filterType) {
		case RANGE:
			if (rule.min != null)
				result.append(sqlColumnName).append(rule.minInclusive ? " >= " : " > ").append(toSQLDate(rule.min));
			if (rule.min != null && rule.max != null)
				result.append(" AND ");
			if (rule.max != null)
				result.append(sqlColumnName).append(rule.maxInclusive ? " >= " : " > ").append(toSQLDate(rule.max));
			break;
		case SINGLE_VALUE:
			result.append(sqlColumnName).append(" = ").append(toSQLDate(rule.value));
			break;
		case VALUES:
			if (rule.values != null && rule.values.size() > 0) {
				final String sep = ", ";
				result.append(sqlColumnName).append(rule.valuesInclusive ? " IN " : " NOT IN ").append(" (");
				for (Iterator<String> iter = rule.values.iterator(); iter.hasNext(); ) {
					result.append(toSQLDate(iter.next()));
					if (iter.hasNext())
						result.append(sep);
				}
				result.append(")");
			}
			break;
		}
		return result.length() == 0 ? null : result.toString();
	}
	
	private static final String toSQLDate(String date) {
		return "DATE '" + date + "'";
	}
}
