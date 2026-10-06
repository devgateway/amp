package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import org.digijava.kernel.ampapi.discriminators.DiscriminationConfigurer;
import org.digijava.module.aim.dbentity.AmpComponentFunding;

/**
 * @author Viorel Chihai
 */
public class AmpComponentFundingDiscriminationConfigurer implements DiscriminationConfigurer {

    @Override
    public void configure(Object obj, String fieldName, String discriminationValue) {
        AmpComponentFunding componentFunding = (AmpComponentFunding) obj;
        componentFunding.setTransactionType(Integer.valueOf(discriminationValue));
    }
}
