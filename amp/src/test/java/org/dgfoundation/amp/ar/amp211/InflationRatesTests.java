package org.dgfoundation.amp.ar.amp211;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.VirtualCurrenciesMaintainer;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.converters.MtefConverter;
import org.dgfoundation.amp.testutils.AmpRunnable;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.dbentity.AmpInflationRate;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.util.CurrencyUtil;
import org.junit.Test;

/**
 * testcases for Currency Deflator (https://jira.dgfoundation.org/browse/AMP-20534)
 * @author Constantin Dolghier
 *
 */
public class InflationRatesTests extends MondrianReportsTestCase {

	public final static List<String> activities = Arrays.asList(
			"TAC_activity_1", "Test MTEF directed", "Pure MTEF Project", "mtef activity 1", "Activity with both MTEFs and Act.Comms");
	
	public InflationRatesTests() {
		super("currency deflator tests");
	}
	
	@Test
	public void testValidator() {
		AmpCurrency usd = CurrencyUtil.getAmpcurrency("USD");
		AmpInflationRate impossibleYear = new AmpInflationRate(usd, 1940, -2.7, true);
		shouldFailSaving(impossibleYear);
		
		AmpInflationRate impossibleInflation = new AmpInflationRate(usd, 2010, -999, true);
		shouldFailSaving(impossibleInflation);
		
		AmpInflationRate noCurrencyGiven = new AmpInflationRate(null, 2010, 3.5, true);
		shouldFailSaving(noCurrencyGiven);
	}
	
	@Test
	public void shouldFailSaving(final AmpInflationRate inflationRate) {
		// tests that Hibernate does not allow one to save invalid instances of AmpInflationRates
		shouldFail(new AmpRunnable() {
			@Override public void run() throws Exception {
				PersistenceManager.getSession().save(inflationRate);
				PersistenceManager.getSession().flush();
			}
		});
		PersistenceManager.endSessionLifecycle(); // else the entity remains sticky in the session and unrelated flush()-es will fail
	}
	
	@Test
	public void testComputePriceIndices() {
		AmpCurrency usd = CurrencyUtil.getCurrencyByCode("USD");
		List<AmpInflationRate> rates = Arrays.asList(
				new AmpInflationRate(usd, 2007, 1.1, true),
				new AmpInflationRate(usd, 2008, 2.0, true),
				new AmpInflationRate(usd, 2009, -5.0, true),
				new AmpInflationRate(usd, 2011, 1.7, true));
		
		SortedMap<Integer, Double> priceIndices = VirtualCurrenciesMaintainer.computePriceIndices(rates, 2006);
		assertEquals("{2006=1.0, 2007=1.011, 2008=1.03122, 2009=0.979659, 2010=0.979659, 2011=0.996313}", priceIndices.toString());
		
		SortedMap<Integer, Double> rebased2006 = VirtualCurrenciesMaintainer.rebasePriceIndices(priceIndices, 2006, 2006);
		assertEquals("{2006=1.0, 2007=1.011, 2008=1.03122, 2009=0.979659, 2010=0.979659, 2011=0.996313}", rebased2006.toString());
		
		SortedMap<Integer, Double> rebased2008 = VirtualCurrenciesMaintainer.rebasePriceIndices(priceIndices, 2006, 2008);
		assertEquals("{2006=0.969725, 2007=0.980392, 2008=1.0, 2009=0.95, 2010=0.95, 2011=0.96615}", rebased2008.toString());
		
		SortedMap<Integer, Double> priceIndices2008 = VirtualCurrenciesMaintainer.computePriceIndices(rates, 2008);
		assertEquals("{2007=0.98912, 2008=1.0, 2009=0.95, 2010=0.95, 2011=0.96615}", priceIndices2008.toString());
	
		SortedMap<Integer, Double> priceIndices2005 = VirtualCurrenciesMaintainer.computePriceIndices(rates, 2005);
		assertEquals("{2005=1.0, 2006=1.0, 2007=1.011, 2008=1.03122, 2009=0.979659, 2010=0.979659, 2011=0.996313}", priceIndices2005.toString());

		System.out.println("mwahahah " + priceIndices);
	}
	
	@Test
	public void testKeepNDecimals() {
		assertEquals(0.001, AlgoUtils.keepNDecimals(0.001, 3));
		assertEquals(0.001, AlgoUtils.keepNDecimals(0.0013, 3));
		assertEquals(0.001, AlgoUtils.keepNDecimals(0.0008, 3));
		assertEquals(0.001, AlgoUtils.keepNDecimals(0.0000001, 3));
		assertEquals(0.0001, AlgoUtils.keepNDecimals(0.0000001, 4));
	}
	
	class MyVirtualCurrenciesMaintainer extends VirtualCurrenciesMaintainer {
		
		public SortedMap<String, List<AmpCurrencyRate>> savedExchangeRates = new TreeMap<>();
		
		@Override protected void saveExchangeRates(AmpCurrency virtualCurrency, List<AmpCurrencyRate> ratesToWrite) {
			savedExchangeRates.put(virtualCurrency.getCurrencyCode(), ratesToWrite);
		}
		
		@Override
		protected AmpCurrency ensureVirtualCurrencyExists(AmpInflationRate rate) {
			return createVirtualCurrency(rate);
		}
	}
	
	@Test
	public void testEntriesCreation() {
		MyVirtualCurrenciesMaintainer mvcm = new MyVirtualCurrenciesMaintainer();
		AmpCurrency usd = CurrencyUtil.getCurrencyByCode("USD");
		List<AmpInflationRate> rates = Arrays.asList(
				new AmpInflationRate(usd, 2007, 1.1, true),
				new AmpInflationRate(usd, 2008, 2.0, true),
				new AmpInflationRate(usd, 2009, -5.0, true),
				new AmpInflationRate(usd, 2011, 1.7, true));
		mvcm.createVirtualCurrenciesBasedOn(rates, usd);
		assertEquals("[USD2007, USD2008, USD2009, USD2011]", mvcm.savedExchangeRates.keySet().toString());
		assertEquals("[from USD2007 to USD on 2006-01-01 equals 0.9891, from USD2007 to USD on 2006-12-31 equals 0.9891, from USD to USD2007 on 2006-01-01 equals 1.0110, from USD to USD2007 on 2006-12-31 equals 1.0110, from USD2007 to USD on 2007-01-01 equals 1.0000, from USD2007 to USD on 2007-12-31 equals 1.0000, from USD to USD2007 on 2007-01-01 equals 1.0000, from USD to USD2007 on 2007-12-31 equals 1.0000, from USD2007 to USD on 2008-01-01 equals 1.0200, from USD2007 to USD on 2008-12-31 equals 1.0200, from USD to USD2007 on 2008-01-01 equals 0.9804, from USD to USD2007 on 2008-12-31 equals 0.9804, from USD2007 to USD on 2009-01-01 equals 0.9690, from USD2007 to USD on 2009-12-31 equals 0.9690, from USD to USD2007 on 2009-01-01 equals 1.0320, from USD to USD2007 on 2009-12-31 equals 1.0320, from USD2007 to USD on 2010-01-01 equals 0.9690, from USD2007 to USD on 2010-12-31 equals 0.9690, from USD to USD2007 on 2010-01-01 equals 1.0320, from USD to USD2007 on 2010-12-31 equals 1.0320, from USD2007 to USD on 2011-01-01 equals 0.9855, from USD2007 to USD on 2011-12-31 equals 0.9855, from USD to USD2007 on 2011-01-01 equals 1.0147, from USD to USD2007 on 2011-12-31 equals 1.0147]", 
				mvcm.savedExchangeRates.get("USD2007").toString());
		assertEquals("[from USD2008 to USD on 2006-01-01 equals 0.9697, from USD2008 to USD on 2006-12-31 equals 0.9697, from USD to USD2008 on 2006-01-01 equals 1.0312, from USD to USD2008 on 2006-12-31 equals 1.0312, from USD2008 to USD on 2007-01-01 equals 0.9804, from USD2008 to USD on 2007-12-31 equals 0.9804, from USD to USD2008 on 2007-01-01 equals 1.0200, from USD to USD2008 on 2007-12-31 equals 1.0200, from USD2008 to USD on 2008-01-01 equals 1.0000, from USD2008 to USD on 2008-12-31 equals 1.0000, from USD to USD2008 on 2008-01-01 equals 1.0000, from USD to USD2008 on 2008-12-31 equals 1.0000, from USD2008 to USD on 2009-01-01 equals 0.9500, from USD2008 to USD on 2009-12-31 equals 0.9500, from USD to USD2008 on 2009-01-01 equals 1.0526, from USD to USD2008 on 2009-12-31 equals 1.0526, from USD2008 to USD on 2010-01-01 equals 0.9500, from USD2008 to USD on 2010-12-31 equals 0.9500, from USD to USD2008 on 2010-01-01 equals 1.0526, from USD to USD2008 on 2010-12-31 equals 1.0526, from USD2008 to USD on 2011-01-01 equals 0.9662, from USD2008 to USD on 2011-12-31 equals 0.9662, from USD to USD2008 on 2011-01-01 equals 1.0350, from USD to USD2008 on 2011-12-31 equals 1.0350]", 
				mvcm.savedExchangeRates.get("USD2008").toString());
		assertEquals("[from USD2009 to USD on 2006-01-01 equals 1.0208, from USD2009 to USD on 2006-12-31 equals 1.0208, from USD to USD2009 on 2006-01-01 equals 0.9797, from USD to USD2009 on 2006-12-31 equals 0.9797, from USD2009 to USD on 2007-01-01 equals 1.0320, from USD2009 to USD on 2007-12-31 equals 1.0320, from USD to USD2009 on 2007-01-01 equals 0.9690, from USD to USD2009 on 2007-12-31 equals 0.9690, from USD2009 to USD on 2008-01-01 equals 1.0526, from USD2009 to USD on 2008-12-31 equals 1.0526, from USD to USD2009 on 2008-01-01 equals 0.9500, from USD to USD2009 on 2008-12-31 equals 0.9500, from USD2009 to USD on 2009-01-01 equals 1.0000, from USD2009 to USD on 2009-12-31 equals 1.0000, from USD to USD2009 on 2009-01-01 equals 1.0000, from USD to USD2009 on 2009-12-31 equals 1.0000, from USD2009 to USD on 2010-01-01 equals 1.0000, from USD2009 to USD on 2010-12-31 equals 1.0000, from USD to USD2009 on 2010-01-01 equals 1.0000, from USD to USD2009 on 2010-12-31 equals 1.0000, from USD2009 to USD on 2011-01-01 equals 1.0170, from USD2009 to USD on 2011-12-31 equals 1.0170, from USD to USD2009 on 2011-01-01 equals 0.9833, from USD to USD2009 on 2011-12-31 equals 0.9833]",
				mvcm.savedExchangeRates.get("USD2009").toString());
		assertEquals("[from USD2011 to USD on 2006-01-01 equals 1.0037, from USD2011 to USD on 2006-12-31 equals 1.0037, from USD to USD2011 on 2006-01-01 equals 0.9963, from USD to USD2011 on 2006-12-31 equals 0.9963, from USD2011 to USD on 2007-01-01 equals 1.0147, from USD2011 to USD on 2007-12-31 equals 1.0147, from USD to USD2011 on 2007-01-01 equals 0.9855, from USD to USD2011 on 2007-12-31 equals 0.9855, from USD2011 to USD on 2008-01-01 equals 1.0350, from USD2011 to USD on 2008-12-31 equals 1.0350, from USD to USD2011 on 2008-01-01 equals 0.9662, from USD to USD2011 on 2008-12-31 equals 0.9662, from USD2011 to USD on 2009-01-01 equals 0.9833, from USD2011 to USD on 2009-12-31 equals 0.9833, from USD to USD2011 on 2009-01-01 equals 1.0170, from USD to USD2011 on 2009-12-31 equals 1.0170, from USD2011 to USD on 2010-01-01 equals 0.9833, from USD2011 to USD on 2010-12-31 equals 0.9833, from USD to USD2011 on 2010-01-01 equals 1.0170, from USD to USD2011 on 2010-12-31 equals 1.0170, from USD2011 to USD on 2011-01-01 equals 1.0000, from USD2011 to USD on 2011-12-31 equals 1.0000, from USD to USD2011 on 2011-01-01 equals 1.0000, from USD to USD2011 on 2011-12-31 equals 1.0000]", 
				mvcm.savedExchangeRates.get("USD2011").toString());
	}
}
