package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import org.digijava.kernel.ampapi.discriminators.DiscriminationConfigurer;
import org.digijava.module.aim.dbentity.AmpFundingDetail;

/**
 * @author Viorel Chihai
 */
public class AmpFundingDetailDiscriminationConfigurer implements DiscriminationConfigurer {

    @Override
    public void configure(Object obj, String fieldName, String discriminationValue) {
        AmpFundingDetail transaction = (AmpFundingDetail) obj;
        transaction.setTransactionType(Integer.valueOf(discriminationValue));
    }

}
