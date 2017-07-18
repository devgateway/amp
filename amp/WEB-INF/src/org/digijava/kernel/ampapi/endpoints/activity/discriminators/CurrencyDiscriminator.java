package org.digijava.kernel.ampapi.endpoints.activity.discriminators;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.FieldsDiscriminator;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;

public class CurrencyDiscriminator extends FieldsDiscriminator {
	
	@Override
	public Map<Object, Object> getPossibleValues() {
		List<AmpCurrency> currencies = CurrencyUtil.getActiveAmpCurrencyByCode();
		Map<Object, Object> valuesMap = new HashMap<>();
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
