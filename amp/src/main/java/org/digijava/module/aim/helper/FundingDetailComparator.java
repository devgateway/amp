package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.util.FeaturesUtil;

import java.util.Comparator;

/**
 * Created by Aldo Picca.
 */
public class FundingDetailComparator {

    public static Comparator<AmpFundingDetail> getFundingDetailComparator() {
        Comparator<AmpFundingDetail> comparator = null;
        String globalSettingComparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants
                .REORDER_FUNDING_ITEMS);
        switch (globalSettingComparator) {
            case Constants.COMPARATOR_TRANSACTION_DATE_DESC:
                comparator = new FundingDetailTransactionDateComparator().getDescending();
                break;
            case Constants.COMPARATOR_REPORTING_DATE_ASC:
                comparator = new FundingDetailReportingDateComparator().getAscending();
                break;
            case Constants.COMPARATOR_REPORTING_DATE_DESC:
                comparator = new FundingDetailReportingDateComparator().getDescending();
                break;
            default:
                comparator = new FundingDetailTransactionDateComparator().getAscending();
        }

        return comparator;
    }

}
