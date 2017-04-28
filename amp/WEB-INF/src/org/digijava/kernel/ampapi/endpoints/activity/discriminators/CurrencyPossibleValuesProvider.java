package org.digijava.kernel.ampapi.endpoints.activity.discriminators;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.module.aim.annotations.interchange.PossibleValuesEntity;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;

@PossibleValuesEntity(AmpCurrency.class)
public class CurrencyPossibleValuesProvider extends PossibleValuesProvider {
	
	@Override
	public Map<String, Object> getPossibleValues() {
		List<AmpCurrency> currencies = CurrencyUtil.getActiveAmpCurrencyByCode();
		Map<String, Object> valuesMap = new HashMap<String, Object>();
		for (AmpCurrency currency : currencies)
			valuesMap.put(currency.getCurrencyCode(), currency.getCurrencyCode());
		return valuesMap;
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
