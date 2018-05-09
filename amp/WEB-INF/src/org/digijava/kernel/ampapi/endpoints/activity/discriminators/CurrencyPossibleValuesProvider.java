package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import java.util.ArrayList;
import java.util.List;

import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.module.aim.annotations.interchange.PossibleValuesEntity;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;

@PossibleValuesEntity(AmpCurrency.class)
public class CurrencyPossibleValuesProvider extends PossibleValuesProvider {
    
    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<AmpCurrency> currencies = CurrencyUtil.getActiveAmpCurrencyByCode();
        List<PossibleValue> values = new ArrayList<>();
        for (AmpCurrency currency : currencies) {
            values.add(new PossibleValue(currency.getCurrencyCode(), currency.getCurrencyCode(),
                    translatorService.translateLabel(currency.getCurrencyName())));
        }
        return values;
    }
    
    @Override
    public Object toJsonOutput(Object object) {
        return object;
    }

    @Override
    public Long getIdOf(Object value) {
        return null;
    }

    public Object toAmpFormat(Object obj) {
        return obj;
    }

}
