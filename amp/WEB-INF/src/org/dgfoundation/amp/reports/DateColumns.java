/**
 * 
 */
package org.dgfoundation.amp.reports;

import java.util.LinkedHashSet;
import java.util.Set;

import org.dgfoundation.amp.ar.ColumnConstants;

/**
 * Stores the date columns
 * @author Nadejda Mandrescu
 */
public class DateColumns {
	public final static Set<String> ACTIVITY_DATES = new LinkedHashSet<String>() {{
		add(ColumnConstants.ACTIVITY_CREATED_ON);
		add(ColumnConstants.ACTIVITY_UPDATED_ON);
		add(ColumnConstants.ACTUAL_APPROVAL_DATE);
		add(ColumnConstants.ACTUAL_COMPLETION_DATE);
		add(ColumnConstants.ACTUAL_START_DATE);
		add(ColumnConstants.AGREEMENT_CLOSE_DATE);
		add(ColumnConstants.AGREEMENT_EFFECTIVE_DATE);
		add(ColumnConstants.AGREEMENT_SIGNATURE_DATE);
		add(ColumnConstants.DONOR_COMMITMENT_DATE);
		add(ColumnConstants.FINAL_DATE_FOR_CONTRACTING);
		add(ColumnConstants.FINAL_DATE_FOR_DISBURSEMENTS);
		add(ColumnConstants.FUNDING_CLASSIFICATION_DATE);
		add(ColumnConstants.FUNDING_END_DATE);
		add(ColumnConstants.FUNDING_START_DATE);
		add(ColumnConstants.ORIGINAL_COMPLETION_DATE);
		add(ColumnConstants.PLEDGES_DETAIL_DATE_RANGE);
		add(ColumnConstants.PLEDGES_DETAIL_END_DATE);
		add(ColumnConstants.PLEDGES_DETAIL_START_DATE);
		add(ColumnConstants.PROPOSED_APPROVAL_DATE);
		add(ColumnConstants.PROPOSED_COMPLETION_DATE);
		add(ColumnConstants.PROPOSED_START_DATE);
	}};
}
