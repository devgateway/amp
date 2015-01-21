package org.dgfoundation.amp.ar;

import java.util.Collection;
import java.util.Map;

/**
 * Generates the SQL query to filter by approval status
 * @author eperez
 *
 */
public class ApprovalStatusQueryBuilder implements SQLQueryGenerator {

	/**
	 * Generate the SQL query to filter by approval status
	 * 
	 * @param approvalStatuses, Collection <String> containing the ids of the 
	 * approval statuses to filter
	 * @return String with the sql query
	 */
	public String generateSQLQuery(Collection<String> approvalStatuses) {
		StringBuffer query = new StringBuffer("");
		for (final String id : approvalStatuses) {
			query.append(mapIdToQueryFragment(id, false));
		}
		return cleanUpQuery(query);

	}

	/**
	 * Generate the SQL query to filter by approval status
	 * @param approvalStatuses,Map<String, Boolean> containing the ids of the 
	 * approval statuses to filter and whether each id should be included or excluded
	 * (negative filter)
	 * @return String, the SQL query
	 */
	public  String generateSQLQuery(Map<String, Boolean> approvalStatuses) {
		StringBuffer query = new StringBuffer("");
		for (final String id : approvalStatuses.keySet()) {
			query.append(mapIdToQueryFragment(id, approvalStatuses.get(id)));
		}
		return cleanUpQuery(query);
	}

	/**
	 * 
	 * @param actStatusValue
	 * @return
	 */
	private static String cleanUpQuery(StringBuffer actStatusValue) {
		int posi = actStatusValue.lastIndexOf("OR");
		if (posi > 0)
			actStatusValue.delete(posi, posi + 2);
		return "select amp_activity_id from amp_activity where " + actStatusValue.toString();
	}

	/**
	 * Generates a fragment of the query to filter for a given approval status 
	 * 
	 * @param id, the approval status to query
	 * @param inclusive, whether the element should be included or if it is a negative filter 
	 * @return the fragment of the query
	 */
	private static String mapIdToQueryFragment(String id, Boolean inclusive) {
		String queryFragment = "";
		switch (Integer.parseInt(id)) {
		case -1:
			queryFragment = "1=1 ";
			break;
		case 0:// Existing Un-validated - This will show all the activities that
				// have been approved at least once and have since been edited
				// and not validated.
			if (inclusive) {
				queryFragment = " (approval_status IN  ('edited', 'not_approved', 'rejected') and draft <> true)";
			}
			else {
				queryFragment = " (approval_status NOT IN  ('edited', 'not_approved', 'rejected') OR draft is true)";
					
			}
			break;
		case 1:// New Draft - This will show all the activities that have never
				// been approved and are saved as drafts.
			if (inclusive) {
				queryFragment = " (approval_status  IN  ('started', 'startedapproved') and draft is true) ";
						
			}
			else {
				queryFragment = " (approval_status  NOT IN  ('started', 'startedapproved') OR draft <> true) ";
			}
			break;
		case 2:// New Un-validated - This will show all activities that are new
				// and have never been approved by the workspace manager.
			if (inclusive) {
				queryFragment = " (approval_status =  'started' and draft <> true)";
							
			}
			else {
				queryFragment = " (approval_status <> 'started' OR  draft IS true)";
						
			}
			break;
		case 3:// existing draft. This is because when you filter by Existing
				// Unvalidated you get draft activites that were edited and
				// saved as draft
			if (inclusive) {
				queryFragment = " ( approval_status IN ('edited', 'approved')  and draft is true ) ";
			}
			else {
				queryFragment = " ( approval_status NOT IN ('edited', 'approved')  OR draft <> true ) ";
				
			}
			break;

		case 4:// Validated Activities
			if (inclusive) {
				queryFragment = " (approval_status  IN  ('approved', 'startedapproved') and draft<>true)";
			}
			else {
				queryFragment = " (approval_status  NOT IN  ('approved', 'startedapproved') and draft is true)";
						
			}
			break;
		default:
			queryFragment = "1=1 ";
			break;
		}
		queryFragment += " OR ";
		return queryFragment;
	}

}
