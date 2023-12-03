package org.digijava.kernel.ampapi.endpoints.common;

import org.digijava.kernel.ampapi.endpoints.activity.discriminators.CurrencyCommonPossibleValuesProvider;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.dbentity.AmpCurrency;

/**
 * Structure used for common settings.
 *
 * @author Viorel Chihai
 */
public class CommonSettings {

    @PossibleValues(CurrencyCommonPossibleValuesProvider.class)
    @Interchangeable(fieldTitle = "Currency", pickIdOnly = true)
    private AmpCurrency currency;

}
