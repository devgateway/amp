package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import org.digijava.kernel.ampapi.discriminators.DiscriminationConfigurer;
import org.digijava.module.aim.dbentity.AmpFundingAmount;

/**
 * @author Octavian Ciubotaru
 */
public class AmpFundingAmountDiscriminationConfigurer implements DiscriminationConfigurer {

    @Override
    public void configure(Object obj, String fieldName, String discriminationValue) {
        AmpFundingAmount fundingAmount = (AmpFundingAmount) obj;
        fundingAmount.setFunType(AmpFundingAmount.FundingType.valueOf(discriminationValue));
    }

}
