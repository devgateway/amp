package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.util.FeaturesUtil;

import java.io.Serializable;
import java.util.Comparator;

import static org.digijava.module.aim.helper.GlobalSettingsConstants.FUNDING_ITEM_LIST_EXPANDABLE;

/**
 * Created by Aldo Picca.
 */
public abstract class FundingDetailGenericComparator implements Serializable {

    public Comparator<AmpFundingDetail> getAscending() {
        return getComparator(true);
    }

    public Comparator<AmpFundingDetail> getDescending() {
        return getComparator(false);
    }

    public Comparator<AmpFundingDetail> getComparator(boolean asc) {
        if (FeaturesUtil.getGlobalSettingValueBoolean(FUNDING_ITEM_LIST_EXPANDABLE)) {
            return getExpandableListComparator(asc);
        }

        return getDefaultComparator();
    }

    public Comparator<AmpFundingDetail> getExpandableListComparator(boolean asc) {
        return (o1, o2) -> {
            if (o1.getAmpFundDetailId() == null && o2.getAmpFundDetailId() != null) {
                return -1;
            }

            if (o2.getAmpFundDetailId() == null && o1.getAmpFundDetailId() != null) {
                return 1;
            }

            return asc ? getDefaultComparator().compare(o1, o2) : getDefaultComparator().compare(o2, o1);
        };
    }

    public abstract Comparator<AmpFundingDetail> getDefaultComparator();
}
