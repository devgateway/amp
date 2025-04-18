package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import org.digijava.kernel.ampapi.discriminators.DiscriminationConfigurer;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;

/**
 * @author Octavian Ciubotaru
 */
public class AmpIndicatorValueDiscriminationConfigurer implements DiscriminationConfigurer {

    @Override
    public void configure(Object obj, String fieldName, String discriminationValue) {
        AmpIndicatorValue value = (AmpIndicatorValue) obj;
        value.setValueType(Integer.parseInt(discriminationValue));
    }
}
