package org.digijava.kernel.ampapi.endpoints.common.valueproviders;

import com.google.common.collect.ImmutableMap;
import org.digijava.module.aim.annotations.interchange.InterchangeableValueProvider;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;

/**
 * @author Octavian Ciubotaru
 */
public class ClassificationConfigurationValueProvider
        implements InterchangeableValueProvider<AmpClassificationConfiguration> {

    @Override
    public String getValue(AmpClassificationConfiguration configuration) {
        return configuration.getName();
    }

    @Override
    public Object getExtraInfo(AmpClassificationConfiguration configuration) {
        return ImmutableMap.of("scheme_name", configuration.getClassification().getSecSchemeName());
    }
}
