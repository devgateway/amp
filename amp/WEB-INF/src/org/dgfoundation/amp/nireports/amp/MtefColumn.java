package org.dgfoundation.amp.nireports.amp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;

/**
 * class for fetching any of the MTEF columns/measures
 * @author Dolghier Constantin
 *
 */
public class MtefColumn extends AmpFundingColumn {

	final int mtefYear;
	final Optional<Long> adjustmentTypeCode;
	
	public MtefColumn(String columnName, int mtefYear, String totalColumnName, Optional<HardCodedCategoryValue> adjustmentType) {
		super(columnName, "v_ni_mtef_funding", "amp_activity_id", new MtefBehaviour(totalColumnName));
		this.mtefYear = mtefYear;
		this.adjustmentTypeCode = adjustmentType.map(z -> z.existsInDatabase() ? z.getIdInDatabase() : -1);
	}
	
	@Override
	protected String buildCondition(NiReportsEngine engine) {
		return String.format("%s AND (source_role_id = (SELECT amp_role_id FROM amp_role WHERE role_code = 'DN')) AND (mtef_year = %d)%s", super.buildCondition(engine), mtefYear, adjustmentTypeCode.isPresent() ? String.format(" AND (adjustment_type = %d)", adjustmentTypeCode.get()) : "");
	}
	
	@Override
	protected Set<String> getIgnoredColumns() {
		return new HashSet<>(Arrays.asList("pledge_id", "transaction_type"));
	}
}
