/**
 * 
 */
package org.dgfoundation.amp.currency.inflation.ds;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.currency.IRFrequency;
import org.digijava.kernel.ampapi.endpoints.util.ObjectMapperUtils;
import org.digijava.kernel.restclient.RestClient;
import org.digijava.kernel.restclient.RestClient.Type;
import org.digijava.module.aim.dbentity.AmpInflationRate;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * Use of FRED®API 
 * Based on Source: U.S. Bureau of Economic Analysis.
 * 
 * @author Nadejda Mandrescu
 */
public class FredDataSource {
    protected static final Logger logger = Logger.getLogger(RestClient.class);
    
    //DEFLATOR: move EP url to settings once Settings v2 are available
    public static final String FRED_OBSERVATIONS_EP_URL = "https://api.stlouisfed.org/fred/series/observations";
    public static final SimpleDateFormat DATE_FORMATTER = DateTimeUtil.getStrictSimpleDateFormat("yyyy-MM-dd");
    
    //DEFLATOR: also not making it very configurable yet since the approach plan may change again
    public Map<String, List<String>> queryParamsGNPDEF = new HashMap<String, List<String>>() {{
        put("series_id", Arrays.asList("GNPDEF"));
        put("observation_start", Arrays.asList(AmpInflationRate.MIN_DATE_STR));
        put("observation_end", Arrays.asList(AmpInflationRate.MAX_DATE_STR));
        put("file_type", Arrays.asList("json"));
        put("units", Arrays.asList("pch"));
        put("api_key", new ArrayList<String>());
        put("frequency", new ArrayList<String>());
    }};
    
    public FredDataSource(String apiToken, IRFrequency frequency) {
        queryParamsGNPDEF.get("api_key").add(apiToken);
        queryParamsGNPDEF.get("frequency").add(frequency.name().toLowerCase());
    }
    
    /**
     * @return SortedMap<period_start_date_as_date, value_as_double>
     */
    public SortedMap<Date, Double> getGNPDEFObservations() {
        SortedMap<Date, Double> result = new TreeMap<Date, Double>();
        SortedMap<String, Double> observations = getGNPDEFObservationsAsIs();
        for (Entry<String, Double> entry: observations.entrySet() ) {
            try {
                result.put(DATE_FORMATTER.parse(entry.getKey()), entry.getValue());
            } catch (ParseException e) {
                logger.error(e.getMessage());
            }
        }
        return result;
    }
    
    /**
     * @return SortedMap<period_start_date_as_string, value_as_double>
     */
    public SortedMap<String, Double> getGNPDEFObservationsAsIs() {
        RestClient rc = RestClient.getInstance(Type.JSON);
        String json = rc.requestGET(FRED_OBSERVATIONS_EP_URL, queryParamsGNPDEF);
        SortedMap<String, Double> result = new TreeMap<String, Double>();
        List<Map<String, String>> observations = json == null ? null :  
                (List<Map<String, String>>) ObjectMapperUtils.getMapFromString(json).get("observations");
        if (observations != null) {
            for (Map<String, String> pair : observations) {
                String valStr = pair.get("value").trim();
                Double value = Double.parseDouble(".".equals(valStr) ? "0" : valStr);
                result.put(pair.get("date"), value);
            }
        }
        return result;
    }

}
