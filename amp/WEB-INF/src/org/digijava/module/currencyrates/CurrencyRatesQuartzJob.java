
package org.digijava.module.currencyrates;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.calendar.exception.CalendarException;
import org.digijava.module.calendar.util.AmpDbUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * @author Marcelo Sotero
 * 
 */
public class CurrencyRatesQuartzJob implements Job {
	private static Logger logger = Logger
			.getLogger(CurrencyRatesQuartzJob.class);
	private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	private WSCurrencyClient myWSCurrencyClient;
	private String baseCurrency;
	private Date lastExcecution;
	private static final int tries = 3;

	public CurrencyRatesQuartzJob() {
		myWSCurrencyClient = DailyCurrencyRateSingleton.getInstance()
				.getMyWSCurrencyClient();
		this.baseCurrency = DailyCurrencyRateSingleton.getInstance()
				.getBaseCurrency();
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
		logger
				.info("START Getting Currencies Rates from WS............................."
						+ formatter.format(new Date()));

		Collection<AmpCurrency> currencies = CurrencyUtil
				.getAllCurrencies(CurrencyUtil.ORDER_BY_CURRENCY_CODE);

		String[] ampCurrencies = this.getCurrencies(currencies);
		HashMap<String, Double> wsCurrencyValues=null;

		try {
			
			int mytries = 1;			
			while (mytries <= tries && ampCurrencies!=null) {
				logger.info("Attempt.........................." + mytries);
				wsCurrencyValues = this.myWSCurrencyClient
						.getCurrencyRates(ampCurrencies, baseCurrency);
				showValues(ampCurrencies, wsCurrencyValues);
				save(ampCurrencies, wsCurrencyValues);
				//checks if all values are wrong
				if(!isRightRate(ampCurrencies, wsCurrencyValues))
					Thread.sleep(1000*60*60);//wait an hour before next intent
				ampCurrencies = this.getWrongCurrencies(ampCurrencies,
						wsCurrencyValues);
				mytries++;
			}
			this.lastExcecution = new Date();
			DailyCurrencyRateSingleton.getInstance().setLastExcecution(
					this.lastExcecution);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if(ampCurrencies!=null){
			sendEmailToAdmin();
		}
		logger
				.info("END Getting Currencies Rates from WS............................."
						+ formatter.format(new Date()));
	}

	private boolean isRightRate(String[] currencies,
			HashMap<String, Double> wsCurrencyValues) {
		for (int i=0; i<currencies.length; i++) {
			double value = wsCurrencyValues.get(currencies[i]);
			if (value != WSCurrencyClient.CONNECTION_ERROR && value != WSCurrencyClient.INVALID_CURRENCY_CODE && value != 1) {
				//we were able to get a valid rate at least, so there is internet connection
				return true;
			}
		}
		return false;
	}

	private String[] getWrongCurrencies(String[] currencies,
			HashMap<String, Double> wsCurrencyValues) {
		String[] wrongValuesArray = null;
		ArrayList<AmpCurrencyRate> wrongAList = new ArrayList<AmpCurrencyRate>();
		for (int i=0; i<currencies.length; i++) {
			AmpCurrencyRate currRate = new AmpCurrencyRate();
			currRate.setToCurrencyCode(currencies[i]);
			double value = wsCurrencyValues.get(currencies[i]);
			if (value == WSCurrencyClient.CONNECTION_ERROR) {
				wrongAList.add(currRate);
			}
		}
		if (wrongAList.size() != 0) {
			wrongValuesArray = new String[wrongAList.size()];
			int i = 0;
			for (AmpCurrencyRate ampCurrency : wrongAList) {
				wrongValuesArray[i++] = ampCurrency.getToCurrencyCode();
			}
		}
		return wrongValuesArray;
	}
	private void save(String[] currencies,
			HashMap<String, Double> wsCurrencyValues) {
		for (int i=0; i<currencies.length; i++) {
			AmpCurrencyRate currRate = new AmpCurrencyRate();
			//currRate.setAmpCurrencyRateId(ampCurrency.getAmpCurrencyId());
			double value = wsCurrencyValues.get(currencies[i]);
			if (value == WSCurrencyClient.INVALID_CURRENCY_CODE) {
				logger.info(currencies[i]
						+ " Not Supported...");
				continue;
			} else if (value == WSCurrencyClient.CONNECTION_ERROR) {
				logger.info("Connection Error trying to get "
						+ currencies[i]);
				continue;
			}
			currRate.setExchangeRate(value);
			
			
			Date aDate=new Date();
			try {
				String sDate = DateTimeUtil.formatDate(aDate);
				aDate = DateTimeUtil.parseDate(sDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			currRate.setExchangeRateDate(aDate);
			currRate.setToCurrencyCode(currencies[i]);
			currRate.setDataSource(CurrencyUtil.RATE_FROM_WEB_SERVICE);
			CurrencyUtil.saveCurrencyRate(currRate);
		}
	}
 
	private void save(Collection<AmpCurrency> currencies,
			HashMap<String, Double> wsCurrencyValues) {
		for (AmpCurrency ampCurrency : currencies) {
			AmpCurrencyRate currRate = new AmpCurrencyRate();
			currRate.setAmpCurrencyRateId(ampCurrency.getAmpCurrencyId());
			double value = wsCurrencyValues.get(ampCurrency.getCurrencyCode()
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
			
			
			Date aDate=new Date();
			try {
				String sDate = this.formatter.format(new Date());
				aDate = DateTimeUtil.parseDate(sDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			currRate.setExchangeRateDate(aDate);
			currRate.setToCurrencyCode(ampCurrency.getCurrencyCode().trim());
			currRate.setDataSource(CurrencyUtil.RATE_FROM_WEB_SERVICE);
			CurrencyUtil.saveCurrencyRate(currRate);
		}
	}

	@SuppressWarnings("unused")
	private String[] getCurrencies(Collection<AmpCurrency> currencies) {
		String[] curr = new String[currencies.size()];
		int i = 0;
		for (AmpCurrency ampCurrency : currencies) {
			curr[i] = ampCurrency.getCurrencyCode().trim();
			// logger.info("Get: " + curr[i]);
			i++;
		}
		return curr;
	}

	private void showValues(String[] curr,
			HashMap<String, Double> currencyValues) {
		for (int i = 0; i < curr.length; i++) {
			logger.info(curr[i].trim() + ": "
					+ currencyValues.get(curr[i].trim()));
		}
	}

	void sendEmailToAdmin(){
		try {
			Collection<User> users = (List <User>)AmpDbUtil.getUsers();
			for(User user:users){
				if(user.isGlobalAdmin()){
					System.out.println("An email has been sent to "+ user.getFirstNames()+" "+user.getLastName() +"-"+user.getEmail());
					DgEmailManager.sendMail(user.getEmail(), "Daily Currency Rates Update ERROR","Please, check your internet connection and Timeout at Admin Tools > Global Settings >Timeout Daily Currency Update.");
				}
			}
		} catch (CalendarException e) {
			e.printStackTrace();
		} catch (Exception e) {
		    e.printStackTrace();
	    }
		logger.info("There were connection error trying to update currencies");
	}
}
