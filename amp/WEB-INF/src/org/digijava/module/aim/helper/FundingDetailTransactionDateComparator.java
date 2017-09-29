package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.AmpFundingDetail;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Aldo Picca.
 */
public class FundingDetailTransactionDateComparator implements Serializable {

    private static final Comparator<AmpFundingDetail> comparator = Comparator.comparing(
            AmpFundingDetail::getTransactionDate, Comparator.nullsFirst(Comparator.naturalOrder()));

    public static Comparator<AmpFundingDetail> getAscending() {
        return comparator;
    }

    public static Comparator<AmpFundingDetail> getDescending() {
        return comparator.reversed();
    }

}
