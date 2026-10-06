/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.currency;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Currency Endpoint Constants
 * 
 * @author Nadejda Mandrescu
 */
public class CurrencyEPConstants {

    public static final Map<Integer, Boolean> CURRENCY_ACTIVE_MAP =
            Collections.unmodifiableMap(new HashMap<Integer, Boolean>() {
                {
                    put(0, Boolean.FALSE);
                    put(1, Boolean.TRUE);
                }
    });
    
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "desc";
    public static final String SELECTED = "selected";
    public static final String CURRENCY_CODE = "currency-code";
    public static final String FREQUENCY = "frequency";
    public static final String API_TOKEN = "api_token";
    
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    
    // Data Sources
    public static final String FRED_GNPDEF = "FRED-GNPDEF";

}
