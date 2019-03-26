package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import java.util.ArrayList;
import java.util.List;

import org.digijava.kernel.ampapi.endpoints.activity.CurrencyExtraInfo;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.currency.CurrencyEPConstants;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;

public class CurrencyCommonPossibleValuesProvider implements PossibleValuesProvider {
    
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
    public boolean isAllowed(Long id) {
        return CurrencyUtil.isUsableAmpCurrency(id);
    }
}
