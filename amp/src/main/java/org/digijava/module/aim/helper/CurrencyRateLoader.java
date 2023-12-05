/*
 * CurrencyRateLoader.java
 * Created : 20-May-2005
 */
package org.digijava.module.aim.helper;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

/**
 * Helper class for loading currency rates from a Comma Seperated Value (CSV) file
 * The csv file should have the following format for the helper class to identify the
 * rates properly 
 * CODE,RATE,DATE 
 * for example
 * USD,1,01.01.2005
 * 
 * 'DATE' should be of the form dd.mm.yyyy
 * 
 * @author Priyajith 
 */
public class CurrencyRateLoader {
    
    private static Logger logger = Logger.getLogger(CurrencyRateLoader.class);
    
    /**
     * The function will return a collection of CurrencyRates object
     * @param fileName The csv file from where the rates are to be read
     * @return collection of CurrencyRates object
     */
    public static Collection getCurrencyRates(String fileName) throws Exception {
        Collection col = new ArrayList();
        File file = new File(fileName);
        if (file != null) {
            FileReader fReader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(fReader);
            String line = null;
            StringTokenizer st = null;
            CurrencyRates cRate = null;
            while ((line = bReader.readLine()) != null) {
                st = new StringTokenizer(line,",");
                if (st.countTokens() == 3) {
                    /*
                     * The present format is
                     * CODE,RATE,DATE
                     * To change the format just change the sequence of
                     * the following three lines according to the format
                     */
                    String code = st.nextToken().trim(); // get CODE
                    double rate = Double.parseDouble(st.nextToken().trim()); // get RATE
                    String date = st.nextToken().trim(); // get DATE
                    date = date.replace('.','/');

                    cRate = new CurrencyRates();
                    cRate.setCurrencyCode(code);
                    logger.debug("Setting rate as " + date);
                    cRate.setExchangeRateDate(date);
                    
                    cRate.setExchangeRate(new Double(rate));
                    col.add(cRate);
                }
            }   
        }
        return col;
    }
}


