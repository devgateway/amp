package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.digijava.kernel.translator.LocalizableLabel;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;

import java.util.Optional;
import java.util.Set;

/**
 * class for fetching any of the MTEF columns/measures.
 * It is a very simple subclass of {@link AmpFundingColumn}, since the fetching SQL filtering logic is slightly different, being based on the MTEF year
 * @author Dolghier Constantin
 *
 */
public class MtefColumn extends AmpFundingColumn {

    public final int mtefYear;
    public final Optional<Long> adjustmentTypeCode;
    public final boolean directed;

    public MtefColumn(String columnName, LocalizableLabel label, int mtefYear, String totalColumnName,
            boolean directed, Optional<HardCodedCategoryValue> adjustmentType, SubDimensions subDimensions) {
        super(columnName, label, "v_ni_mtef_funding",
            directed ? new DirectedMeasureBehaviour(totalColumnName) : new MtefBehaviour(totalColumnName),
            subDimensions);
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
}
