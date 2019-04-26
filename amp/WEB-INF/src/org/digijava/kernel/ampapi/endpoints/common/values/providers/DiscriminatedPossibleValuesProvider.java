package org.digijava.kernel.ampapi.endpoints.common.values.providers;

import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;

/**
 * @author Nadejda Mandrescu
 */
public interface DiscriminatedPossibleValuesProvider extends PossibleValuesProvider {

    String getDiscriminatorValue();

}
