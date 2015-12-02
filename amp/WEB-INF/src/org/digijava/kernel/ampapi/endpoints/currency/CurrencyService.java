/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.currency;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.dgfoundation.amp.currency.CurrencyInflationUtil;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpInflationRate;
import org.digijava.module.aim.dbentity.AmpInflationSource;
import org.digijava.module.aim.util.CurrencyUtil;


/**
 * Currency Endpoint utility methods
 * 
 * @author Nadejda Mandrescu
 */
public class CurrencyService {
	
	/**
	 * @see Currencies#getCurrencyInflationDataSources()
	 */
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
	
	/**
	 * @see Currencies#getAmpInflationRates()
	 */
	public static JsonBean getAmpInflationRates(){
		JsonBean result = new JsonBean();
		SimpleDateFormat dbDateExportFormat = new SimpleDateFormat(CurrencyEPConstants.DATE_FORMAT); 
		List<AmpInflationRate> rates = CurrencyInflationUtil.getInflationRates();
		if (rates != null && rates.size() > 0) {
			for (AmpInflationRate rate : rates) {
				String currencyCode = rate.getCurrency().getCurrencyCode();
				SortedMap<String, Double> currencyRates = (TreeMap<String, Double>) result.get(currencyCode);
				if (currencyRates == null) {
					currencyRates = new TreeMap<String, Double>();
					result.set(currencyCode, currencyRates);
				}
				currencyRates.put(dbDateExportFormat.format(rate.getPeriodStart()), rate.getInflationRate());
			}
		}
		
		return result;
	}

}
