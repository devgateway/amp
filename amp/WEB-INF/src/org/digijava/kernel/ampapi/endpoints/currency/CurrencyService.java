/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.currency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.currency.CurrencyInflationUtil;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpInflationSource;


/**
 * Currency Endpoint utility methods
 * 
 * @author Nadejda Mandrescu
 */
public class CurrencyService {
	
	public static List<JsonBean> getInflationDataSources(){
		List<JsonBean> dataSources = new ArrayList<JsonBean>();
		List<AmpInflationSource> ampSources = CurrencyInflationUtil.getInflationDataSources();
		if (ampSources != null && ampSources.size() > 0) {
			for (AmpInflationSource ais : ampSources) {
				JsonBean ds = new JsonBean();
				ds.set(CurrencyEPConstants.ID, ais.getId());
				ds.set(CurrencyEPConstants.NAME, TranslatorWorker.translateText(ais.getName()));
				ds.set(CurrencyEPConstants.DESCRIPTION, TranslatorWorker.translateText(ais.getDescription()));
				ds.set(CurrencyEPConstants.SELECTED, ais.getSelected());
				//DEFLATOR: temporary just for information, until full settings support based on Settings V2 API can be used
				Map<String, Object> settings = new HashMap<String, Object>();
				settings.put(CurrencyEPConstants.CURRENCY_CODE, ais.getCurrency().getCurrencyCode());
				settings.put(CurrencyEPConstants.FREQUENCY, ais.getFrequency());
				settings.put(CurrencyEPConstants.API_TOKEN, ais.getApiToken());
				ds.set(EPConstants.SETTINGS, settings);
				
				dataSources.add(ds);
			}
		}
		
		return dataSources;
	}

}
