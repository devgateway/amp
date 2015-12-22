package org.dgfoundation.amp.ar.amp212;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javafx.util.Pair;

import org.dgfoundation.amp.currencyconvertor.AmpCurrencyConvertor;
import org.dgfoundation.amp.currencyconvertor.CurrencyConvertor;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
		MockHttpServletRequest request = new MockHttpServletRequest();
		ServletRequestAttributes attr = new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(attr);
		TLSUtils.populate(request);
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
		SortedMap<String, Pair> dates = new TreeMap<String, Pair>();

		// MDL rates
		dates.put("2014-08-17", new Pair<>("MDL", 13.67));
		dates.put("2014-08-18", new Pair<>("MDL", 13.67));
		dates.put("2014-08-19", new Pair<>("MDL", 13.67));
		dates.put("2014-08-20", new Pair<>("MDL", 13.67));
		// rate should change
		dates.put("2014-08-21", new Pair<>("MDL", 13.766));
		dates.put("2014-08-22", new Pair<>("MDL", 13.766));
		dates.put("2014-08-23", new Pair<>("MDL", 13.766));
		dates.put("2014-08-24", new Pair<>("MDL", 13.766));

		for (String dateKey : dates.keySet()) {
			Pair<String, Double> rate = dates.get(dateKey);
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

					List<CategAmountCell> cells = engine.schema.getFundingFetcher().fetchColumn(engine);

					LocalDate date = LocalDate.parse("2014-12-16");
					assertEquals(
							"["
									+ "(actId: 79, 106227.592617 on 2015-10-06 with meta: {MetaInfoSet: [donor_org: 21699, terms_of_assistance: 2119, financing_instrument: 2125, source_role: DN, adjustment_type: Actual, mode_of_payment: 2094, transaction_type: 0]}, "
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

				if (!fromCurrencyCode.equals(toCurrencyCode)) {
					if (fromCurrencyCode.equals(BASE_CURRENCY) || toCurrencyCode.equals(BASE_CURRENCY)) {

						assertEquals(exchangeRate, 1 / exchangeRateInverse);
						
					} else {
						double fromCurrencyExchangeRate = convertor.getExchangeRate(fromCurrencyCode, BASE_CURRENCY, null,
								date);
						double toCurrencyExchangeRate = convertor.getExchangeRate(toCurrencyCode, BASE_CURRENCY, null, date);
						
						assertEquals(fromCurrencyExchangeRate / toCurrencyExchangeRate,
								((fromCurrencyExchangeRate * 1) / toCurrencyExchangeRate));
						
					}
				}
			}
		}
	}

	@Test
	public void testRates() {

		SortedMap<String, Pair> dates = new TreeMap<String, Pair>();

		// MDL rates
		dates.put("2014-05-25", new Pair<>("MDL", 12.167));
		dates.put("2014-08-24", new Pair<>("MDL", 13.766));
		dates.put("2014-08-17", new Pair<>("MDL", 13.67));
		dates.put("2015-12-15", new Pair<>("MDL", 19.71));
		// EUR rates
		dates.put("2012-12-15", new Pair<>("EUR", 0.76));
		dates.put("2013-02-10", new Pair<>("EUR", 0.7482));
		dates.put("2012-12-27", new Pair<>("EUR", 0.7559));

		for (String dateKey : dates.keySet()) {
			Pair<String, Double> rate = dates.get(dateKey);
			LocalDate date = LocalDate.parse(dateKey);

			double exchangeRate = convertor.getExchangeRate(BASE_CURRENCY, rate.getKey(), null, date);

			assertEquals(Math.round(rate.getValue().doubleValue() * 1000) / 1000.0, Math.round(exchangeRate * 1000) / 1000.0);

		}

	}
}
