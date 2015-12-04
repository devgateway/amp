/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.currency;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.currency.CurrencyInflationUtil;
import org.dgfoundation.amp.currency.inflation.ds.FredDataSource;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpInflationRate;
import org.digijava.module.aim.dbentity.AmpInflationSource;
import org.digijava.module.aim.util.CurrencyUtil;


/**
 * Currency Endpoint utility methods
 * 
 * @author Nadejda Mandrescu
 */
public class CurrencyService {
	
	protected static final Logger logger = Logger.getLogger(CurrencyService.class);
	protected static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(CurrencyEPConstants.DATE_FORMAT);
	{
		DATE_FORMATTER.setLenient(false);
	}
	
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
		List<AmpInflationRate> rates = CurrencyInflationUtil.getInflationRates();
		if (rates != null && rates.size() > 0) {
			for (AmpInflationRate rate : rates) {
				String currencyCode = rate.getCurrency().getCurrencyCode();
				SortedMap<String, Double> currencyRates = (TreeMap<String, Double>) result.get(currencyCode);
				if (currencyRates == null) {
					currencyRates = new TreeMap<String, Double>();
					result.set(currencyCode, currencyRates);
				}
				currencyRates.put(DATE_FORMATTER.format(rate.getPeriodStart()), rate.getInflationRate());
			}
		}
		
		return result;
	}
	
	/**
	 * @see Currencies#saveInflationRates(JsonBean)
	 */
	public static JsonBean saveInflationRates(JsonBean jsonRates){
		JsonBean result = null;
		ApiEMGroup errors = new ApiEMGroup();
		
		// prepare and validate data
		Map<AmpCurrency, Map<Date, Double>> ratesPerCurrency = new HashMap<AmpCurrency, Map<Date, Double>>();
		for(Entry<String, Object> entry : jsonRates.any().entrySet()) {
			String currCode = entry.getKey();
			AmpCurrency currency = CurrencyUtil.getCurrencyByCode(currCode);
			Object series = entry.getValue();
			if (currency == null) {
				errors.addApiErrorMessage(CurrencyErrors.INVALID_CURRENCY_CODE, currCode);
			}
			String sInfo = currCode + " : ... %s ..."; 
			boolean invalidSeries = series == null || !(series instanceof Map);
			if (!invalidSeries) {
				Iterator<?> iter = ((Map) series).keySet().iterator();
				if (iter.hasNext())
					invalidSeries = !(iter.next() instanceof String);
			}
				
			if (invalidSeries) {
				errors.addApiErrorMessage(CurrencyErrors.INVALID_INFLATION_RATE_SERIES, String.format(sInfo,  
						(series == null ? "null" : StringUtils.substring(series.toString(), 0, 20))));
			} else {
				// reset iterator and process the series
				for (Iterator<?> iter = ((Map) series).keySet().iterator(); iter.hasNext(); ) {
					String dateStr = (String) iter.next();
					Object value = ((Map) series).get(dateStr);
					Date date = null;
					try {
						date = DATE_FORMATTER.parse(dateStr);
					} catch (ParseException e) {
						logger.error(e.getMessage());
						errors.addApiErrorMessage(CurrencyErrors.INVALID_DATE_FORMAT, String.format(sInfo, dateStr));
					}
					if (!(value instanceof Number)) {
						errors.addApiErrorMessage(CurrencyErrors.INVALID_INFLATION_RATE_VALUE, String.format(sInfo, value));
					} else if (currency != null && date != null) {
						// now if everything is valid, record it
						Map<Date, Double> dateValues = ratesPerCurrency.get(currency);
						if (dateValues == null) {
							dateValues = new TreeMap<Date, Double>();
							ratesPerCurrency.put(currency, dateValues);
						}
						dateValues.put(date, ((Number) value).doubleValue());
					}
				}
			}
		}
		
		if (errors.size() > 0) {
			result = ApiError.toError(errors.getAllErrors());
		} else {
			// if no errors, then cleanup existing rates and create new ones
			CurrencyInflationUtil.deleteAllInflationRates();
			for (Entry<AmpCurrency, Map<Date, Double>> entry : ratesPerCurrency.entrySet()) {
				for (Entry<Date, Double> vEntry : entry.getValue().entrySet()) {
					AmpInflationRate air = new AmpInflationRate(entry.getKey(), vEntry.getKey(), vEntry.getValue());
					PersistenceManager.getRequestDBSession().save(air);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * @see Currencies#getAmpInflationRates(Long)
	 */
	public static JsonBean getInflationRatesFromSource(Long sourceId) {
		AmpInflationSource ds = CurrencyInflationUtil.getInflationDataSource(sourceId);
		if (ds == null) {
			return ApiError.toError(new ApiErrorMessage(CurrencyErrors.INVALID_SOURCE_ID, String.valueOf(sourceId)));
		}
		JsonBean result = null;
		switch (ds.getName()) {
		case CurrencyEPConstants.FRED_GNPDEF:
			FredDataSource fredDS = new FredDataSource(ds.getApiToken(), ds.getFrequency());
			Map<String, SortedMap<String, Double>> inflRates = new HashMap<String, SortedMap<String, Double>>();
			inflRates.put(ds.getCurrency().getCurrencyCode(), fredDS.getGNPDEFObservationsAsIs());
			result = toInflationRatesResult(inflRates);
			break;
		}
		return result;
	}
	
	protected static Map<String, SortedMap<String, Double>> convert(Map<String, SortedMap<Date, Double>> inflRates) {
		Map<String, SortedMap<String, Double>> result = new HashMap<String, SortedMap<String, Double>>();
		for(Entry<String, SortedMap<Date, Double>> entry : inflRates.entrySet()) {
			SortedMap<String, Double> series = new TreeMap<String, Double>();
			for (Entry<Date, Double> sEntry : entry.getValue().entrySet()) {
				series.put(DATE_FORMATTER.format(sEntry.getKey()), sEntry.getValue());
			}
			result.put(entry.getKey(), series);
		}
		return result;
	}
	
	protected static JsonBean toInflationRatesOnDates(Map<String, SortedMap<Date, Double>> inflationRatesPerCurrency) {
		return toInflationRatesResult(convert(inflationRatesPerCurrency));
	}
	
	protected static JsonBean toInflationRatesResult(Map<String, SortedMap<String, Double>> inflationRatesPerCurrency) {
		JsonBean result = new JsonBean();
		result.any().putAll(inflationRatesPerCurrency);
		return result;
	}
	
}
