package org.dgfoundation.amp.nireports.amp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.AmpFundingColumn.FundingFetcherContext;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;

/**
 * class for fetching any of the MTEF columns/measures
 * @author Dolghier Constantin
 *
 */
public class MtefColumn extends AmpFundingColumn {

	final int mtefYear;
	final Optional<Long> adjustmentTypeCode;
	final boolean directed;

	public MtefColumn(String columnName, int mtefYear, String totalColumnName, boolean directed, Optional<HardCodedCategoryValue> adjustmentType) {
		super(columnName, "v_ni_mtef_funding", "amp_activity_id", directed ? new DirectedMeasureBehaviour(totalColumnName) : new MtefBehaviour(totalColumnName));
		this.mtefYear = mtefYear;
		this.adjustmentTypeCode = adjustmentType.map(z -> z.existsInDatabase() ? z.getIdInDatabase() : -1);
		this.directed = directed;
	}
	
	@Override
	protected String buildSupplementalCondition(NiReportsEngine engine, Set<Long> ids, FundingFetcherContext context) {
		String directedCond = directed ? "(source_role_id IS NOT NULL) AND (recipient_role_id IS NOT NULL) AND (recipient_org_id IS NOT NULL) AND (donor_org_id IS NOT NULL)" : 
			"source_role_id = (SELECT amp_role_id FROM amp_role WHERE role_code = 'DN')"; 
		
		return String.format("(%s) AND (mtef_year = %d)%s", directedCond, mtefYear,
			adjustmentTypeCode.isPresent() ? String.format(" AND (adjustment_type = %d)", adjustmentTypeCode.get()) : "");
	}
	
	@Override
	protected Set<String> getIgnoredColumns() {
		return new HashSet<>(Arrays.asList("pledge_id", "transaction_type", "disaster_response_code"));
	}
}
