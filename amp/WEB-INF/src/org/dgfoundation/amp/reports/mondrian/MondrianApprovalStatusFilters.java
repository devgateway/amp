package org.dgfoundation.amp.reports.mondrian;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportElement;

public class MondrianApprovalStatusFilters {

	public static String getSupportedColumn() {
		return ColumnConstants.APPROVAL_STATUS;
	}

	public static String generateFilterQuery(Map<ReportElement, List<FilterRule>> filters) {
		List<FilterRule> rules = getFilterRules(filters);
		if (rules == null || rules.size() == 0) {
			return null;
		}
		final StringBuffer select = new StringBuffer("SELECT amp_activity_id FROM amp_activity_version WHERE ");

		for (FilterRule rule : rules) {
			switch (rule.filterType) {
			case RANGE:
				throw new RuntimeException("range filter for ids makes no sense");

			case SINGLE_VALUE:
				select.append(buildApprovalStatusQuery(rule.value));
				break;
			case VALUES:
				if (rule.values != null && rule.values.size() > 0) {
					for (Iterator<String> iter = rule.values.iterator(); iter.hasNext();) {
						select.append(buildApprovalStatusQuery(iter.next()));
					}
					break;
				}

			}
		}
		int posi = select.lastIndexOf("OR");
		if (posi == -1) {
			return null;
		}
		select.delete(posi, posi + 2);
		return select.toString();

	}

	private static List<FilterRule> getFilterRules(Map<ReportElement, List<FilterRule>> filters) {
		for (Iterator<Entry<ReportElement, List<FilterRule>>> entryIter = filters.entrySet().iterator(); entryIter
				.hasNext();) {
			Entry<ReportElement, List<FilterRule>> entry = entryIter.next();
			if (entry.getKey().entity != null
					&& ColumnConstants.APPROVAL_STATUS.equals(entry.getKey().entity.getEntityName())) {
				return entry.getValue();
			}
		}
		return null;

	}

	private static String buildApprovalStatusQuery(String value) {
		String actStatusValue;
		switch (Integer.parseInt(value)) {
		case -1:
			actStatusValue = "1=1 ";
			break;
		case 0:// Existing Un-validated - This will show all the activities that
				// have been approved at least once and have since been edited
				// and not validated.
			actStatusValue = " (approval_status IN ('edited', 'not_approved', 'rejected') and draft <> true)";
			break;
		case 1:// New Draft - This will show all the activities that have never
				// been approved and are saved as drafts.
			actStatusValue = " (approval_status in ('started', 'startedapproved') and draft is true) ";
			break;
		case 2:// New Un-validated - This will show all activities that are new
				// and have never been approved by the workspace manager.
			actStatusValue = " (approval_status='started' and draft <> true)";
			break;
		case 3:// existing draft. This is because when you filter by Existing
				// Unvalidated you get draft activites that were edited and
				// saved as draft
			actStatusValue = " ( (approval_status='edited' or approval_status='approved') and draft is true ) ";
			break;
		case 4:// Validated Activities
			actStatusValue = " (approval_status in ('approved', 'startedapproved') and draft<>true)";
			break;
		default:
			actStatusValue = "1=1 ";
			break;
		}
		actStatusValue += " OR ";
		return actStatusValue;
	}
}
