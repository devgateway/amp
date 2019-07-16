package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.currencyconvertor.DateRateInfo;
import org.dgfoundation.amp.currencyconvertor.ExchangeRates;
import org.dgfoundation.amp.currencyconvertor.OneCurrencyCalculator;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.junit.Test;

/**
 * ETL Testcases
 * @author Dolghier Constantin
 *
 */
public class ETLTests extends AmpTestCase {
    
    @Test
    public void testSQLUtilsMultiLineWriter() {
/*      Map<String, Object> row1 = new LinkedHashMap<String, Object>() {{
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
                + "(1,'Some String, man! A very long string indeed',2.5)," 
                + "(15,'an another string',NULL);",
                str
                ); */
    }
    
    @Test
    public void testSQLUtilsWriter() {
        List<String> keys = Arrays.asList("col1", "col2", "col3");
        List<Object> coords = Arrays.<Object>asList(1l, "Some String, man! A very long string indeed", 2.5);
        String res = SQLUtils.buildCoordsLine(coords, null, null);      
        assertEquals("(1,'Some String, man! A very long string indeed',2.5)", res);
        
        // a null and a missing value
        List<Object> coords2 = Arrays.<Object>asList(SQLUtils.SQL_UTILS_NULL, 2, null);
        String res2 = SQLUtils.buildCoordsLine(coords2, null, null);
        assertEquals("(NULL,2,NULL)", res2);
        System.out.println(res);
    }
    
    @Test
    public void testDateRateInfo() {
        DateRateInfo dri = new DateRateInfo(10, 0, 5.5);
        assertEquals(dri.requestedDate, 10);
        assertEquals(dri.minDateDelta, 0);
        assertEquals(dri.rate, 5.5, DELTA_6);
        
        dri = new DateRateInfo(3, -2, 5.5);
        assertEquals(dri.requestedDate, 3);
        assertEquals(dri.minDateDelta, 2);
        assertEquals(dri.rate, 5.5, DELTA_6);
        
        dri = new DateRateInfo(3, 2, 5.5);
        assertEquals(dri.requestedDate, 3);
        assertEquals(dri.minDateDelta, 2);
        assertEquals(dri.rate, 5.5, DELTA_6);
    }
    
    @Test
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
    
    @Test
    public void testCurrencyETL() {
        DateRateInfo rateA = new DateRateInfo(10, 0, 21.0);
        DateRateInfo rateB = new DateRateInfo(12, 2, 31.0);

        assertEquals(21.0, OneCurrencyCalculator.chooseBestRate(rateA, rateB), DELTA_6);
        assertEquals(1/21.0, OneCurrencyCalculator.chooseBestRate(rateB, rateA), DELTA_6);
        
        rateB = new DateRateInfo(10, 0, 10);
        assertEquals(21.0, OneCurrencyCalculator.chooseBestRate(rateA, rateB), DELTA_6); // should favour direct exchange rate
        assertEquals(10.0, OneCurrencyCalculator.chooseBestRate(rateB, rateA), DELTA_6); // should favour direct exchange rate
        
        rateA = new DateRateInfo(15, 3, 8);
        rateB = new DateRateInfo(19, 1, 0.12);
        assertEquals(8.333333, OneCurrencyCalculator.chooseBestRate(rateA, rateB), 0.0001); // should favour the opposite exchange rate, because it is closer
    }
    
    @Test
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
        
        OneCurrencyCalculator calc = new OneCurrencyCalculator(inverse, direct);
//      System.out.println(rates.toString()); // debug output
        
        for (long day:cor.keySet()) {
            assertEquals("comparing on day " + day, cor.get(day), calc.getRate(day), 0.001);
        }
    }
}
