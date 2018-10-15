package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import java.util.ArrayList;
import java.util.List;

import org.digijava.kernel.ampapi.endpoints.activity.CurrencyExtraInfo;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.currency.CurrencyEPConstants;
import org.digijava.module.aim.annotations.interchange.PossibleValuesEntity;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;

@PossibleValuesEntity(AmpCurrency.class)
public class CurrencyCommonPossibleValuesProvider extends PossibleValuesProvider {
    
    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<AmpCurrency> currencies = CurrencyUtil.getAllAmpCurrencies();
        List<PossibleValue> values = new ArrayList<>();
        for (AmpCurrency currency : currencies) {
            values.add(new PossibleValue(currency.getAmpCurrencyId(), currency.getCurrencyCode(),
                    translatorService.translateLabel(currency.getCurrencyName()),
                    new CurrencyExtraInfo(CurrencyEPConstants.CURRENCY_ACTIVE_MAP.get(currency.getActiveFlag()))));
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
