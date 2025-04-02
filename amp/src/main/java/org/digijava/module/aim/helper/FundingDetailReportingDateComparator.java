package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.AmpFundingDetail;

import java.util.Comparator;

/**
 * Created by Aldo Picca.
 */
public class FundingDetailReportingDateComparator extends FundingDetailGenericComparator {

    private final Comparator<AmpFundingDetail> comparator = Comparator.comparing(
            AmpFundingDetail::getReportingDate, Comparator.nullsFirst(Comparator.naturalOrder()));

    @Override
    public Comparator<AmpFundingDetail> getDefaultComparator() {
        return Comparator.comparing(
                AmpFundingDetail::getReportingDate, Comparator.nullsFirst(Comparator.naturalOrder()));
    }
}
