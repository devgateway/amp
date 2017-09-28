package org.digijava.module.translation.exotic;


import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.dgfoundation.amp.algo.AlgoUtils;

/**
 * Class containing month names with locales unsupported by Java 8. 
 * To add a new one, create a file ExoticMonthNames_{two-letter-code}.properties in this package. 
 * Originally created for Tetum (Timor). 
 * @author acartaleanu
 *
 */
public class ExoticMonthNames {

    private static ConcurrentHashMap<Locale, ExoticMonthNames> localesToMonthNames = new ConcurrentHashMap<>();

    static ExoticMonthNames forLocale(Locale loc) {
        return localesToMonthNames.computeIfAbsent(loc, z -> new ExoticMonthNames(loc));
    }
    
    private final Locale locale;
    private final Map<String, Integer> namesToNumbers;
    private final Map<String, Integer> shortNamesToNumbers;
    private final Map<Integer, String> numbersToNames;
    
    private ExoticMonthNames(Locale loc) {
        this.namesToNumbers = Collections.unmodifiableMap(loadMonthNamesForLocale(loc));
        this.numbersToNames = Collections.unmodifiableMap(AlgoUtils.reverseIntoHashMap(namesToNumbers));
        Map<String, Integer> t = new HashMap<>();
        namesToNumbers.forEach((key, value) -> t.put(shortenMonthName(key), value));
        this.shortNamesToNumbers = Collections.unmodifiableMap(t);
        this.locale = loc;
    }
    
    private Map<String, Integer> loadMonthNamesForLocale(Locale loc) {
        ResourceBundle rb = ResourceBundle.getBundle("org.digijava.module.translation.exotic.ExoticMonthNames", loc);
        HashMap<String, Integer> res = new HashMap<>();
        Enumeration<String> keys = rb.getKeys();
        while (keys.hasMoreElements()) {
            String skey = keys.nextElement();
            Integer key = Integer.parseInt(skey);
            String value = rb.getString(skey);
            res.put(value, key);
        }
        return res;
    }
    
    public int getMonthNumber(String shortMonthName) {
        Integer res = shortNamesToNumbers.get(shortMonthName);
        if (res == null)
            throw new RuntimeException("Couldn't find month with short name " + shortMonthName + " in locale " + locale.toString());
        return res;
    }
    
    /**
     * 
     * @param monthNumber number of the month, starting from 1
     * @return capitalized month name in that language
     */
    public String getFullMonthName(int monthNumber) {
        String res = numbersToNames.get(monthNumber);
        if (res == null) 
            throw new RuntimeException("Couldn't find month with number " + monthNumber + " in locale " + locale.toString());
        return numbersToNames.get(monthNumber);
    }
    
    public String getShortMonthName(int monthNumber) {
        return shortenMonthName(getFullMonthName(monthNumber));
    }

    private static String shortenMonthName(String in) {
        return in.substring(0, 3);
    }
    
}
