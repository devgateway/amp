package org.digijava.module.aim.util;

import java.util.Comparator;

import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;

public class AmpFundingComparatorByDonor implements Comparator<AmpFunding> {

    @Override
    public int compare(AmpFunding o1, AmpFunding o2) {
        return o1.getAmpDonorOrgId().compareTo(o2.getAmpDonorOrgId());
    }

}
