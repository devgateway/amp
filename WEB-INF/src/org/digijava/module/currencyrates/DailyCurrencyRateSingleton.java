package org.digijava.module.currencyrates;

import org.apache.log4j.Logger;

/**
 * 
 * @author Marcelo Sotero
 * 
 */
public final class DailyCurrencyRateSingleton {
	private static Logger logger = Logger
			.getLogger(DailyCurrencyRateSingleton.class);
	public static final String AM = "AM";
	public static final String PM = "PM";
	private static volatile DailyCurrencyRateSingleton instance;
	private boolean status;
	private CurrencyRatesRunner currencyRunner;

	private WSCurrencyClient myWSCurrencyClient;
	private String baseCurrency; 

	private DailyCurrencyRateSingleton() {
		myWSCurrencyClient = new WSCurrencyClientImp();
		baseCurrency = "USD";		
	}

	public static synchronized DailyCurrencyRateSingleton getInstance() {
		if (instance == null)
			synchronized (DailyCurrencyRateSingleton.class) {
				if (instance == null)
					instance = new DailyCurrencyRateSingleton();
			}
		return instance;
	}

	public void start(int hour, int min, String ampm, int hour_repeat,
			int min_repeat) {
		int ampmVal = (ampm.compareToIgnoreCase(DailyCurrencyRateSingleton.PM) == 0) ? 12
				: 0;
		if (hour == 12) {
			if (ampm.compareToIgnoreCase(DailyCurrencyRateSingleton.AM) == 0) {
				hour = 0;
			}
		} else {
			hour += ampmVal;
		}
		if (status) {
			if (currencyRunner.getHour_update() == hour && currencyRunner.getMin_update() == min) {
				logger.info("Daily Currency Rate Service already started...at "
						+ currencyRunner.getHour_update() + " hour, "
						+ currencyRunner.getMin_update() + " min.");
			} else {
				currencyRunner.restart(hour, min, hour_repeat, min_repeat);
				status = true;
				logger.info("Daily Currency Rate Service re-scheduled...at "
						+ currencyRunner.getHour_update() + " hour, "
						+ currencyRunner.getMin_update() + " min.");
			}
		} else {
			currencyRunner = new CurrencyRatesRunner(hour, min, hour_repeat, min_repeat);
			currencyRunner.launch();
			status = true;
			logger.info("Daily Currency Rate Service started...at "
					+ currencyRunner.getHour_update() + " hour, " + currencyRunner.getMin_update()
					+ " min.");
		}
	}

	public void start(int hour, int min, String ampm) {
		start(hour, min, ampm, 0, 0);
	}

	public boolean isRunning() {
		return status;
	}

	public void stop() {
		if(status){
			currencyRunner.stop();
			status = false;
			logger.info("Daily Currency Rate Service ...... STOPED");
		}else{
			logger.info("Daily Currency Rate Service ......already STOPED");
		}
	}

	public static void main(String... strings) {
		DailyCurrencyRateSingleton.getInstance().start(9, 29, "AM", 0, 1);
	}

	public WSCurrencyClient getMyWSCurrencyClient() {
		return myWSCurrencyClient;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public void setMyWSCurrencyClient(WSCurrencyClient myWSCurrencyClient) {
		this.myWSCurrencyClient = myWSCurrencyClient;
	}

}
