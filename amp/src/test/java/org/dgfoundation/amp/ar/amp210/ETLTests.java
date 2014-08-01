package org.dgfoundation.amp.ar.amp210;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.mondrian.CurrencyETL;
import org.dgfoundation.amp.mondrian.DateRateInfo;
import org.dgfoundation.amp.mondrian.ExchangeRates;
import org.dgfoundation.amp.mondrian.MondrianETL;
import org.dgfoundation.amp.mondrian.MondrianTableDescription;
import org.dgfoundation.amp.mondrian.PercentagesDistribution;
import org.dgfoundation.amp.newreports.NumberedTypedEntity;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * ETL Testcases
 * @author Dolghier Constantin
 *
 */
public class ETLTests extends AmpTestCase
{
	
	private ETLTests(String name)
	{
		super(name);
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(ETLTests.class.getName());
		suite.addTest(new ETLTests("testPercentagesDistribution"));
		suite.addTest(new ETLTests("testPercentagesDistributionWithNulls"));
		
		suite.addTest(new ETLTests("testDateRateInfo"));
		suite.addTest(new ETLTests("testExchangeRates"));
		suite.addTest(new ETLTests("testCurrencyETL"));
		suite.addTest(new ETLTests("testCurrencyCombining"));
		suite.addTest(new ETLTests("testSQLUtilsWriter"));
		suite.addTest(new ETLTests("testSQLUtilsMultiLineWriter"));
		suite.addTest(new ETLTests("testMondrianTableDescription"));
		return suite;
	}
	
	public void testMondrianTableDescription() {
		// test that idColumnNames = null means to it mirroring indexedColumns and is iterated in the right sequence
		MondrianTableDescription mtd = new MondrianTableDescription("someTableName", Arrays.asList("1", "2", "3", "c", "5", "a"));
		//assertEquals("[1, 2, 3, c, 5, a]", mtd.idColumnNames.toString());
		assertEquals("[1, 2, 3, c, 5, a]", mtd.indexedColumns.toString());
		
		mtd = new MondrianTableDescription("someTableName", Arrays.asList("1", "2", "3", "c", "5", "a"));
		//assertEquals("[1, 5, c]", mtd.idColumnNames.toString());
		assertEquals("[1, 2, 3, c, 5, a]", mtd.indexedColumns.toString());

	}
	
	public void testSQLUtilsMultiLineWriter() {
		Map<String, Object> row1 = new LinkedHashMap<String, Object>() {{
			put("col1", 1l);
			put("col2", "Some String, man! A very long string indeed");
			put("col3", 2.5);
		}};
		
		Map<String, Object> row2 = new LinkedHashMap<String, Object>() {{
			put("col1", 15l);
			put("col3", SQLUtils.SQL_UTILS_NULL);
			put("col2", "an another string");
		}};

		List<Map<String, Object>> toInsert = Arrays.asList(row1, row2);
		String str = SQLUtils.buildMultiRowInsert("table_name", null, null, toInsert);
		assertEquals("INSERT INTO table_name (col1,col2,col3) VALUES "
				+ "(1,$pleaseGodMoldovaRulz$Some String, man! A very long string indeed$pleaseGodMoldovaRulz$,2.5)," 
				+ "(15,$pleaseGodMoldovaRulz$an another string$pleaseGodMoldovaRulz$,NULL);",
				str
				);
	}
	
	
	public void testSQLUtilsWriter() {
		List<String> keys = Arrays.asList("col1", "col2", "col3");
		Map<String, Object> coords = new LinkedHashMap<String, Object>() {{
			put("col1", 1l);
			put("col2", "Some String, man! A very long string indeed");
			put("col3", 2.5);
		}};
		String res = SQLUtils.buildCoordsLine(coords, keys, null, null);		
		assertEquals("(1,$pleaseGodMoldovaRulz$Some String, man! A very long string indeed$pleaseGodMoldovaRulz$,2.5)", res);
		
		// a null and a missing value
		Map<String, Object> coords2 = new LinkedHashMap<String, Object>() {{
			put("col1", SQLUtils.SQL_UTILS_NULL);
			put("col2", 2);
		}};
		String res2 = SQLUtils.buildCoordsLine(coords2, keys, null, null);
		assertEquals("(NULL,2,NULL)", res2);
		System.out.println(res);
	}
	
	
	protected void testPercentage(String cor, String errors, Long idToAddIfEmpty, Pair... entries) {
		NumberedTypedEntity activity = new NumberedTypedEntity(1, ReportEntityType.ENTITY_TYPE_ACTIVITY);
		PercentagesDistribution perc = new PercentagesDistribution(activity, "primary_sector_id");
		for(Pair entry:entries) {
			perc.add(entry.id, entry.perc);
		}
		perc.postProcess(idToAddIfEmpty);
		assertEquals(cor, perc.toString());
		if (errors == null) {
			assertEquals(0, perc.getErrors().size());
		}
		else
			assertEquals(errors, perc.getErrors().toString());
	}
	
	/**
	 * tests distributing normal percentages (no nulls)
	 */
	public void testPercentagesDistribution() {
		testPercentage("{2=100.0}", null, null, new Pair(2, 10.0));
		testPercentage("{2=10.0, 3=90.0}", null, null, new Pair(2, 10.0), new Pair(3, 90.0));
		testPercentage("{2=25.0, 4=25.0, 5=25.0, 17=25.0}", null, null, new Pair(2, 100.0), new Pair(4, 100.0), new Pair(5, 100.0), new Pair(17, 100.0));
		testPercentage("{}", null, null);
		testPercentage("{999999999=100.0}", null, MondrianETL.MONDRIAN_DUMMY_ID_FOR_ETL);
		testPercentage("{9=100.0}", null, 9l);
	}
	
	/**
	 * tests distributing normal percentages, with nulls
	 */
	public void testPercentagesDistributionWithNulls() {
		testPercentage("{2=100.0}", "[WARNING_TYPE_ENTRY_WITH_NULL on (primary_sector_id, 2) of ENTITY_TYPE_ACTIVITY 1]", null,
			new Pair(2, null));
		testPercentage("{2=50.0, 3=50.0}", "[WARNING_TYPE_ENTRY_WITH_NULL on (primary_sector_id, 2) of ENTITY_TYPE_ACTIVITY 1, WARNING_TYPE_ENTRY_WITH_NULL on (primary_sector_id, 3) of ENTITY_TYPE_ACTIVITY 1]", null,
			new Pair(2, null), new Pair(3, null));
		
		testPercentage("{2=50.0, 3=50.0}", "[WARNING_TYPE_ENTRY_MIXES_NULL_AND_NOT_NULL on (primary_sector_id, -1) of ENTITY_TYPE_ACTIVITY 1, WARNING_TYPE_ENTRY_WITH_NULL on (primary_sector_id, 3) of ENTITY_TYPE_ACTIVITY 1]", null,
			new Pair(2, 15.0), new Pair(3, null));
		
		testPercentage("{2=12.5, 3=37.5, 4=50.0}", "[WARNING_TYPE_ENTRY_MIXES_NULL_AND_NOT_NULL on (primary_sector_id, -1) of ENTITY_TYPE_ACTIVITY 1, WARNING_TYPE_ENTRY_WITH_NULL on (primary_sector_id, 4) of ENTITY_TYPE_ACTIVITY 1]", null,
			new Pair(2, 15.0), new Pair(3, 45.0), new Pair(4, null));
		
		testPercentage("{2=100.0}", "[WARNING_TYPE_ENTRY_WITH_NULL on (primary_sector_id, 2) of ENTITY_TYPE_ACTIVITY 1]", MondrianETL.MONDRIAN_DUMMY_ID_FOR_ETL, 
			new Pair(2, null)); // test that a single null is not completed by the dummy id
	}

	public void testDateRateInfo() {
		DateRateInfo dri = new DateRateInfo(10, 0, 5.5);
		assertEquals(dri.requestedDate, 10);
		assertEquals(dri.minDateDelta, 0);
		assertEquals(dri.rate, 5.5);
		
		dri = new DateRateInfo(3, -2, 5.5);
		assertEquals(dri.requestedDate, 3);
		assertEquals(dri.minDateDelta, 2);
		assertEquals(dri.rate, 5.5);
		
		dri = new DateRateInfo(3, 2, 5.5);
		assertEquals(dri.requestedDate, 3);
		assertEquals(dri.minDateDelta, 2);
		assertEquals(dri.rate, 5.5);
	}
	
	public void testExchangeRates() {
		ExchangeRates er = new ExchangeRates(1, 2);
		assertNull(er.getRatesOnDate(555)); // no exchange rate at all
		
		er.importRate(100, 17.5); // exchange rate of 17.5 on day 100
		assertEquals(new DateRateInfo(50, 50, 17.5), er.getRatesOnDate(50));
		assertEquals(new DateRateInfo(100, 0, 17.5), er.getRatesOnDate(100));
		assertEquals(new DateRateInfo(120, 20, 17.5), er.getRatesOnDate(120));
		
		// second exchange rate: 18.5 on day 120
		er.importRate(120, 18.5);
		assertEquals(new DateRateInfo(50, 50, 17.5), er.getRatesOnDate(50));
		assertEquals(new DateRateInfo(100, 0, 17.5), er.getRatesOnDate(100));
		assertEquals(new DateRateInfo(120, 0, 18.5), er.getRatesOnDate(120));
		assertEquals(new DateRateInfo(110, 10, 17.5), er.getRatesOnDate(110));
		assertEquals(new DateRateInfo(320, 200, 18.5), er.getRatesOnDate(320));
		
		// zeroeth exchange rate: 17 on day 19
		er.importRate(19, 17);
		assertEquals(new DateRateInfo(18, 1, 17), er.getRatesOnDate(18));
		assertEquals(new DateRateInfo(50, 31, 17), er.getRatesOnDate(50));
		assertEquals(new DateRateInfo(100, 0, 17.5), er.getRatesOnDate(100));
		assertEquals(new DateRateInfo(120, 0, 18.5), er.getRatesOnDate(120));
		assertEquals(new DateRateInfo(110, 10, 17.5), er.getRatesOnDate(110));
		assertEquals(new DateRateInfo(320, 200, 18.5), er.getRatesOnDate(320));
	}
	
	public void testCurrencyETL() {
		DateRateInfo rateA = new DateRateInfo(10, 0, 21.0);
		DateRateInfo rateB = new DateRateInfo(12, 2, 31.0);
		
		assertEquals(21.0, CurrencyETL.chooseBestRate(rateA, rateB));
		assertEquals(1/21.0, CurrencyETL.chooseBestRate(rateB, rateA));
		
		rateB = new DateRateInfo(10, 0, 10);
		assertEquals(21.0, CurrencyETL.chooseBestRate(rateA, rateB)); // should favour direct exchange rate
		assertEquals(10.0, CurrencyETL.chooseBestRate(rateB, rateA)); // should favour direct exchange rate
		
		rateA = new DateRateInfo(15, 3, 8);
		rateB = new DateRateInfo(19, 1, 0.12);
		assertEquals(8.333333, CurrencyETL.chooseBestRate(rateA, rateB), 0.0001); // should favour the opposite exchange rate, because it is closer
	}
	
	public void testCurrencyCombining() {
		ExchangeRates direct = new ExchangeRates(1, 2);
		direct.importRate(70, 6.0);
		direct.importRate(60, 5.8);
		direct.importRate(30, 8.0);
		direct.importRate(10, 9.0);

		ExchangeRates inverse = new ExchangeRates(2, 1);
		inverse.importRate(90, 0.22);
		inverse.importRate(80, 0.2);
		inverse.importRate(50, 0.17);
		inverse.importRate(40, 0.15);
		inverse.importRate(20, 0.1);
		
		Map<Long, Double> cor = new java.util.TreeMap<Long, Double>(){{
			put(3l, 9.0);
			put(10l, 9.0);
			put(15l, 9.0);
			put(20l, 10.0);
			put(22l, 10.0);
			put(30l, 8.0);
			put(40l, 1/0.15);
			put(45l, 1/0.15);
			put(50l, 1/0.17);
			put(60l, 5.8);
			put(65l, 5.8);
			put(70l, 6.0);
			put(80l, 1/0.2);
			put(85l, 1/0.2);
			put(90l, 1/0.22);
			put(700l, 1/0.22);
		}};
		
		SortedSet<Long> interestingDates = new TreeSet<>(cor.keySet());
		Map<Long, Double> rates = CurrencyETL.computeCurrencyRates(interestingDates, inverse, direct);
		System.out.println(rates.toString()); // debug output
		
		for (long day:interestingDates) {
			assertEquals("comparing on day " + day, cor.get(day), rates.get(day), 0.001);
		}
	}
	
	@Override
    protected void setUp() throws Exception
    {
		//TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
		super.setUp();
        // do nothing now                
    }
}