package org.dgfoundation.amp.reports.mondrian;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.digijava.module.aim.util.ActivityUtil;


/**
 * central point for Mondrian's SQL filters which filter amp_activity_ids, excluding filtering by FactTableColumns
 * @author Nadejda Mandrescu, Emanuel Perez, Constantin Dolghier
 * 
 */
public class MondrianSQLFilters {

	/**
	 * this one is being filled by addDateColumn() below
	 */
	public static final Set<String> DATE_COLUMNS = new HashSet<String>();
	
	/**
	 * complete list of columns which are filtered though amp_activity_id filters
	 */
	@SuppressWarnings("serial")
	public static final Map<String, MondrianActivityFilter> SQL_COLUMNS = new HashMap<String, MondrianActivityFilter>() {{
		addDateColumn(ColumnConstants.ORIGINAL_COMPLETION_DATE, "original_comp_date");
		addDateColumn(ColumnConstants.FINAL_DATE_FOR_CONTRACTING, "contracting_date");
		addDateColumn(ColumnConstants.FINAL_DATE_FOR_DISBURSEMENTS, "disbursments_date");
		addDateColumn(ColumnConstants.PROPOSED_START_DATE, "proposed_start_date");
		addDateColumn(ColumnConstants.ACTUAL_START_DATE, "actual_start_date");
		addDateColumn(ColumnConstants.PROPOSED_APPROVAL_DATE, "proposed_approval_date");
		addDateColumn(ColumnConstants.ACTUAL_APPROVAL_DATE, "actual_approval_date");
		addDateColumn(ColumnConstants.ACTUAL_COMPLETION_DATE, "actual_completion_date");
		addDateColumn(ColumnConstants.CURRENT_COMPLETION_DATE, "actual_completion_date");
		addDateColumn(ColumnConstants.PROPOSED_COMPLETION_DATE, "proposed_completion_date");
		addDateColumn(ColumnConstants.ACTIVITY_CREATED_ON, "date_created");
		addDateColumn(ColumnConstants.ACTIVITY_UPDATED_ON, "date_updated");		
		put(ColumnConstants.APPROVAL_STATUS, new MondrianApprovalStatusFilter());
		put(ColumnConstants.AMP_ID, new MondrianAmpIdFilter());
		put(ColumnConstants.HUMANITARIAN_AID, new MondrianHumanitarianAidFilter());
		put(ColumnConstants.DRAFT, new MondrianDraftFilter());		
	}
		private void addDateColumn(String ampColumn, String aavColumn) {
			put(ampColumn, new MondrianDateFilters(ampColumn, aavColumn));
			DATE_COLUMNS.add(ampColumn);
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
	public static Set<Long> getActivityIds(MondrianReportFilters mrf) {
		String query = generateFilteringQuery(mrf);
		if (query == null)
			return null;
		Set<Long> activityIds = ActivityUtil.fetchLongs(query);		
		return activityIds;
	}

	/**
	 * Generates plain SQL query filter by date columns over AMP DB
	 * @param filters - date columns filters map
	 * @return query to retrieve the list of activities based on date column filters
	 */
	public static String generateFilteringQuery(MondrianReportFilters mrf) {
		StringBuilder query = new StringBuilder();
		for(String ampColumn:SQL_COLUMNS.keySet()) {
			String columnQuery = SQL_COLUMNS.get(ampColumn).buildQuery(mrf);
			if (columnQuery == null || columnQuery.isEmpty())
				continue;
			query.append(columnQuery);
		}
		
		if (query.length() == 0)
			return null;

		final String select = "SELECT amp_activity_id FROM amp_activity_version WHERE 1=1";
		return select + query.toString();
	}
}
