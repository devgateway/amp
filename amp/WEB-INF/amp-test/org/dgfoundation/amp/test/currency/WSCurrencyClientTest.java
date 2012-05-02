package org.dgfoundation.amp.test.currency;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.currencyrates.DailyCurrencyRateSingleton;
import org.digijava.module.currencyrates.WSCurrencyClient;
import org.digijava.module.currencyrates.WSCurrencyClientImp;

public class WSCurrencyClientTest extends TestCase {
	private WSCurrencyClient myWSCurrencyClient;
	private static Logger logger = Logger.getLogger(WSCurrencyClientTest.class);	

	public static Test suite() {
		junit.framework.TestSuite suite = new junit.framework.TestSuite();
		suite.addTest(new TestSuite(WSCurrencyClientTest.class));
		return suite;

	}

	protected void setUp() throws Exception {
		myWSCurrencyClient = new WSCurrencyClientImp();
		
	}

	public void testGetRates() {
		//list of all supported currencies
		//String[] ampCurrencies = {"ADF", "AED", "AUD", "CAD", "CHF", "CNY", "CZK", "DKK", "ETB", "EUR", "FFR", "GBP", "HUF", "INR", "IRR", "JPY", "KRW", "KWD", "NOK", "NZD", "PLN", "RUB", "SEK", "SKK", "TRL", "UNA", "USD", "XOF"};
		String[] ampCurrencies = {"EUR", "AED"};
		
		HashMap<String, Double> wsCurrencyValues=null;
		try {
			wsCurrencyValues = myWSCurrencyClient
						.getCurrencyRates(ampCurrencies, "USD");
			showValues(ampCurrencies, wsCurrencyValues);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertFalse("Connection fails getting currency rates!!", wsCurrencyValues==null || wsCurrencyValues.isEmpty());
	}
	public void testGetRateBasedUSD() {
		String currency = "AUD";
		double rate = 0;
		try {
			rate = myWSCurrencyClient.getCurrencyRateBasedUSD(currency);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("END Getting Currencies Rates from WS..." + currency + ": "+rate);
		assertFalse("method getCurrencyRateBasedUSD fails!!", rate == 0);
	}	
	private void showValues(String[] curr,
			HashMap<String, Double> currencyValues) {
		for (int i = 0; i < curr.length; i++) {
			logger.info(curr[i].trim() + ": "
					+ currencyValues.get(curr[i].trim()));
		}
	}
	
	protected void tearDown() throws Exception {
	}

}
