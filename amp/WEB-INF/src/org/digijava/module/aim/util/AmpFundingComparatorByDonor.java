package org.digijava.module.aim.util;

import org.digijava.module.aim.dbentity.AmpFunding;

import java.util.Comparator;

public class AmpFundingComparatorByDonor implements Comparator<AmpFunding> {

    @Override
    public int compare(AmpFunding o1, AmpFunding o2) {
        return o1.getAmpDonorOrgId().compareTo(o2.getAmpDonorOrgId());
    }

}
