package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.AmpFundingDetail;

import java.util.Comparator;

/**
 * Created by Aldo Picca.
 */
public class FundingDetailTransactionDateComparator extends FundingDetailGenericComparator {

    @Override
    public Comparator<AmpFundingDetail> getDefaultComparator() {
        return Comparator.comparing(
                AmpFundingDetail::getTransactionDate, Comparator.nullsFirst(Comparator.naturalOrder()));
    }
}
