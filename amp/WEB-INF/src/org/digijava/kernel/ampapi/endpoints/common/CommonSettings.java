package org.digijava.kernel.ampapi.endpoints.common;

import org.digijava.kernel.ampapi.endpoints.activity.discriminators.CurrencyPossibleValuesProvider;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.PossibleValues;

/**
 * Structure used for common settings.
 *
 * @author Viorel Chihai
 */
public class CommonSettings {

    @PossibleValues(CurrencyPossibleValuesProvider.class)
    @Interchangeable(fieldTitle = "Currency")
    private String currency;

}
