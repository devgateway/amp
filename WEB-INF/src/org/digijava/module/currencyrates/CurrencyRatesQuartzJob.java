/*
 * Created on Sep 21, 2006
 * 
 * This class is a simple Job which prints out execution time with its trigger's name
 */
package org.digijava.module.currencyrates;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.util.CurrencyUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * @author Marcelo Sotero
 * 
 */
public class CurrencyRatesQuartzJob implements Job {
	private static Logger logger = Logger.getLogger(CurrencyRatesQuartzJob.class);
	private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	private WSCurrencyClient myWSCurrencyClient;
	private String baseCurrency; 
	
	public CurrencyRatesQuartzJob() {
		myWSCurrencyClient = DailyCurrencyRateSingleton.getInstance().getMyWSCurrencyClient();
		this.baseCurrency = DailyCurrencyRateSingleton.getInstance().getBaseCurrency();
	}
	
	public void executeTest(JobExecutionContext context)
	throws JobExecutionException {
		logger
		.info("START Getting Currencies Rates from WS............................."
				+ formatter.format(new Date()));
		
	}
	
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("START Getting Currencies Rates from WS............................."
						+ formatter.format(new Date()));

		Collection<AmpCurrency> currencies = CurrencyUtil.getAllCurrencies(-1);

		String[] curr = new String[currencies.size()];
		int i = 0;
		for (AmpCurrency ampCurrency : currencies) {
			curr[i] = ampCurrency.getCurrencyCode().trim();
			// logger.info("Get: " + curr[i]);
			i++;
		}
		HashMap<String, Double> currencyValues=null;
		try {
			currencyValues = this.myWSCurrencyClient
					.getCurrencyRates(curr, baseCurrency);
			for (i = 0; i < curr.length; i++) {
				logger.info(curr[i].trim() + ": "
						+ currencyValues.get(curr[i].trim()));
			}
			for (AmpCurrency ampCurrency : currencies) {
				AmpCurrencyRate currRate = new AmpCurrencyRate();
				currRate.setAmpCurrencyRateId(ampCurrency.getAmpCurrencyId());
				double value = currencyValues.get(ampCurrency.getCurrencyCode()
						.trim());
				if (value == WSCurrencyClient.INVALID_CURRENCY_CODE) {					
					logger.info(ampCurrency.getCurrencyCode().trim()
							+ " Not Supported...");
					continue;
				} else if (value == WSCurrencyClient.CONNECTION_ERROR) {
					logger.info("Connection Error trying to get "
							+ ampCurrency.getCurrencyCode().trim());
					continue;
				}
				currRate.setExchangeRate(value);
				currRate.setExchangeRateDate(new Date());
				currRate.setToCurrencyCode(ampCurrency.getCurrencyCode().trim());
				CurrencyUtil.saveCurrencyRate(currRate);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("END Getting Currencies Rates from WS............................."
						+ formatter.format(new Date()));
	}
}
