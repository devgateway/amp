package org.dgfoundation.amp.ar.amp212;

import java.time.LocalDate;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.dgfoundation.amp.currencyconvertor.AmpCurrencyConvertor;
import org.dgfoundation.amp.currencyconvertor.CurrencyConvertor;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.junit.Test;

/**
 * 
 * testcases for the AMP currency convertor
 * 
 * @author Constantin Dolghier
 *
 */
public class CurrencyConvertorTests extends MondrianReportsTestCase {
	final CurrencyConvertor convertor;
	final String BASE_CURRENCY = "USD";
	
	public CurrencyConvertorTests() {
		super("Currency Convertor tests");
		convertor = AmpCurrencyConvertor.getInstance();
	}

	@Test
	public void testLocalDateToJulian() {
		assertEquals(2456320, DateTimeUtil.toJulianDayNumber(LocalDate.of(2013, 1, 27)));
		assertEquals(2456268, DateTimeUtil.toJulianDayNumber(LocalDate.of(2012, 12, 06)));
		assertEquals(2454236, DateTimeUtil.toJulianDayNumber(LocalDate.of(2007, 05, 15)));
	}

	@Test
	public void testClosestDate() {
		SortedMap<String, SimpleImmutableEntry<String, Double>> dates = new TreeMap<String, SimpleImmutableEntry<String, Double>>();

		// MDL rates
		dates.put("2014-08-17", new SimpleImmutableEntry<>("MDL", 13.67));
		dates.put("2014-08-18", new SimpleImmutableEntry<>("MDL", 13.67));
		dates.put("2014-08-19", new SimpleImmutableEntry<>("MDL", 13.67));
		dates.put("2014-08-20", new SimpleImmutableEntry<>("MDL", 13.67));
		// rate should change
		dates.put("2014-08-21", new SimpleImmutableEntry<>("MDL", 13.766));
		dates.put("2014-08-22", new SimpleImmutableEntry<>("MDL", 13.766));
		dates.put("2014-08-23", new SimpleImmutableEntry<>("MDL", 13.766));
		dates.put("2014-08-24", new SimpleImmutableEntry<>("MDL", 13.766));

		for (String dateKey : dates.keySet()) {
			SimpleImmutableEntry<String, Double> rate = dates.get(dateKey);
			LocalDate date = LocalDate.parse(dateKey);

			double exchangeRate = convertor.getExchangeRate(BASE_CURRENCY, rate.getKey(), null, date);

			assertEquals(Math.round(rate.getValue().doubleValue() * 1000) / 1000.0, Math.round(exchangeRate * 1000) / 1000.0);

		}
	}

	@Test
	public void testDonorFunding() throws Exception {
		runNiReportsTestcase(
				Arrays.asList("with weird currencies"),
				engine -> {

					List<CategAmountCell> cells = engine.schema.getFundingFetcher().fetch(engine);

					assertEquals(
							"["
									+ "(actId: 79, 87680.841736 on 2015-10-06 with meta: {MetaInfoSet: [donor_org: 21699, terms_of_assistance: 2119, financing_instrument: 2125, source_role: DN, adjustment_type: Actual, mode_of_payment: 2094, transaction_type: 0]}, "
									+ "(actId: 79, 3632.137149 on 2014-12-16 with meta: {MetaInfoSet: [donor_org: 21699, terms_of_assistance: 2119, financing_instrument: 2125, source_role: DN, adjustment_type: Actual, mode_of_payment: 2094, transaction_type: 0]}, "
									+ "(actId: 79, 6250 on 2015-10-14 with meta: {MetaInfoSet: [donor_org: 21699, terms_of_assistance: 2119, financing_instrument: 2125, source_role: DN, adjustment_type: Actual, mode_of_payment: 2094, transaction_type: 0]}"
									+ "]", cells.toString());

				});

	}

	@Test
	public void testCurrencyCombinations() {

		String strDatewithTime = "2014-12-16";
		LocalDate date = LocalDate.parse(strDatewithTime);
		System.out.println("Date with Time: " + date);

		for (AmpCurrency currencyCodeA : CurrencyUtil.getAllCurrencies(1)) {
			for (AmpCurrency currencyCodeB : CurrencyUtil.getAllCurrencies(1)) {
				String fromCurrencyCode = currencyCodeA.getCurrencyCode();
				String toCurrencyCode = currencyCodeB.getCurrencyCode();

				double exchangeRate = convertor.getExchangeRate(fromCurrencyCode, toCurrencyCode, null, date);
				double exchangeRateInverse = convertor.getExchangeRate(toCurrencyCode, fromCurrencyCode, null, date);
				
				assertEquals(exchangeRate, 1 / exchangeRateInverse);
				
			}
		}
	}

	@Test
	public void testRates() {

		SortedMap<String, SimpleImmutableEntry<String, Double>> dates = new TreeMap<String, SimpleImmutableEntry<String, Double>>();

		// MDL rates
		dates.put("2014-05-25", new SimpleImmutableEntry<>("MDL", 12.167));
		dates.put("2014-08-24", new SimpleImmutableEntry<>("MDL", 13.766));
		dates.put("2014-08-17", new SimpleImmutableEntry<>("MDL", 13.67));
		dates.put("2015-12-15", new SimpleImmutableEntry<>("MDL", 19.71));
		// EUR rates
		dates.put("2012-12-15", new SimpleImmutableEntry<>("EUR", 0.76));
		dates.put("2013-02-10", new SimpleImmutableEntry<>("EUR", 0.7482));
		dates.put("2012-12-27", new SimpleImmutableEntry<>("EUR", 0.7559));

		for (String dateKey : dates.keySet()) {
			SimpleImmutableEntry<String, Double> rate = dates.get(dateKey);
			LocalDate date = LocalDate.parse(dateKey);

			double exchangeRate = convertor.getExchangeRate(BASE_CURRENCY, rate.getKey(), null, date);

			assertEquals(Math.round(rate.getValue().doubleValue() * 1000) / 1000.0, Math.round(exchangeRate * 1000) / 1000.0);

		}

	}


	@Test
	public void testXtoX() {

		LocalDate date = LocalDate.now();
		double exchangeRateBaseCurrency = convertor.getExchangeRate(BASE_CURRENCY, BASE_CURRENCY, null, date);
		double exchangeRateMDL = convertor.getExchangeRate("MDL", "MDL", null, date);
		double exchangeRateEUR = convertor.getExchangeRate("EUR", "EUR", null, date);
		
		assertEquals(1.0, exchangeRateBaseCurrency);
		assertEquals(1.0, exchangeRateMDL);
		assertEquals(1.0, exchangeRateEUR);

	}
	

	@Test
	public void testEURtoMDL() {

		LocalDate date = LocalDate.parse("2015-05-24");
		
		double exchangeRateMDLToBase = convertor.getExchangeRate("MDL", BASE_CURRENCY, null, date);
		double exchangeRateEURToBase = convertor.getExchangeRate("EUR", BASE_CURRENCY, null, date);
		
		double exchangeRateMDLtoEUR = convertor.getExchangeRate("MDL", "EUR", null, date);
		double exchangeRateEURtoMDL = convertor.getExchangeRate("EUR", "MDL", null, date);
		
		assertEquals(0.04629122272957889, exchangeRateMDLtoEUR );
		
		assertEquals(21.602367382726875, exchangeRateEURtoMDL );
		
		assertEquals(exchangeRateMDLtoEUR, exchangeRateMDLToBase / exchangeRateEURToBase );
		
		assertEquals(exchangeRateEURtoMDL, exchangeRateEURToBase / exchangeRateMDLToBase );

	}
}
