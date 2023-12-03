package org.digijava.kernel.ampapi.endpoints.activity.preview;

import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.util.Comparator;

/**
 * 
 * @author Viorel Chihai
 *
 */
public final class PreviewFundingTransactionComparator {

    private static final Comparator<PreviewFundingTransaction> COMPARATOR_BY_REPORTING_DATE = Comparator.comparing(
            PreviewFundingTransaction::getReportingDate, Comparator.nullsFirst(Comparator.naturalOrder()));

    private static final Comparator<PreviewFundingTransaction> COMPARATOR_BY_TRANSACTION_DATE = Comparator.comparing(
            PreviewFundingTransaction::getTransactionDate, Comparator.nullsFirst(Comparator.naturalOrder()));

    private PreviewFundingTransactionComparator() {
    }


    public static Comparator<PreviewFundingTransaction> getTransactionComparator() {

        String gsComparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.REORDER_FUNDING_ITEMS);

        switch (gsComparator) {
            case Constants.COMPARATOR_REPORTING_DATE_ASC:
                return COMPARATOR_BY_REPORTING_DATE;
            case Constants.COMPARATOR_TRANSACTION_DATE_DESC:
                return COMPARATOR_BY_TRANSACTION_DATE.reversed();
            case Constants.COMPARATOR_REPORTING_DATE_DESC:
                return COMPARATOR_BY_REPORTING_DATE.reversed();
            default:
                return COMPARATOR_BY_REPORTING_DATE;
        }
    }
}