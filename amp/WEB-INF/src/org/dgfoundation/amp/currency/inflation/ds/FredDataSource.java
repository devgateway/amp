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
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.restclient.RestClient;
import org.digijava.kernel.restclient.RestClient.Type;

/**
 * Use of FRED®API 
 * 
 * @author Nadejda Mandrescu
 */
public class FredDataSource {
	protected static final Logger logger = Logger.getLogger(RestClient.class);
	
	//DEFLATOR: move EP url to settings once Settings v2 are available
	public static final String FRED_OBSERVATIONS_EP_URL = "https://api.stlouisfed.org/fred/series/observations";
	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	{
		DATE_FORMATTER.setLenient(false);
	}
	
	//DEFLATOR: also not making it very configurable yet since the approach plan may change again
	public Map<String, List<String>> queryParamsGNPDEF = new HashMap<String, List<String>>() {{
		put("series_id", Arrays.asList("GNPDEF"));
		put("file_type", Arrays.asList("json"));
		put("units", Arrays.asList("pch"));
		put("api_key", new ArrayList<String>());
		put("frequency", new ArrayList<String>());
	}};
	
	public FredDataSource(String apiToken, IRFrequency frequency) {
		queryParamsGNPDEF.get("api_key").add(apiToken);
		queryParamsGNPDEF.get("frequency").add(frequency.name().toLowerCase());
	}
	
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
	 * @return SortedMap<period_start_date_as_string, value_as_string>
	 */
	public SortedMap<String, Double> getGNPDEFObservationsAsIs() {
		RestClient rc = RestClient.getInstance(Type.JSON);
		String json = rc.requestGET(FRED_OBSERVATIONS_EP_URL, queryParamsGNPDEF);
		SortedMap<String, Double> result = new TreeMap<String, Double>();
		if (json != null) { 
			List<Map<String, String>> observations = 
					(List<Map<String, String>>) JsonBean.getJsonBeanFromString(json).get("observations");
			for (Map<String, String> pair : observations) {
				String valStr = pair.get("value").trim();
				Double value = Double.parseDouble(".".equals(valStr) ? "0" : valStr);
				result.put(pair.get("date"), value);
			}
		}
		return result;
	}

}
