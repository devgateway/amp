package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import org.digijava.kernel.ampapi.discriminators.DiscriminationConfigurer;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;

/**
 * @author Octavian Ciubotaru
 */
public class AmpRegionalFundingDiscriminationConfigurer implements DiscriminationConfigurer {

    @Override
    public void configure(Object obj, String fieldName, String discriminationValue) {
        AmpRegionalFunding funding = (AmpRegionalFunding) obj;
        funding.setTransactionType(Integer.valueOf(discriminationValue));
    }
}
