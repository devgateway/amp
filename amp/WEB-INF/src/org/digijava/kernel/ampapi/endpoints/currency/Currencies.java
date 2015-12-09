package org.digijava.kernel.ampapi.endpoints.currency;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;

@Path("currency")
public class Currencies {
	
	@Context
	private HttpServletRequest httpRequest;
	@Context
	private HttpServletResponse httpResponse;
	
	/**
	 * Provides standard currency (doesn't include constant currencies)
	 * @return { "currency-code1": "currency-name1", "currency-code2": "currency-name2", ...}
	 */
	@GET
	@Path("/currencies")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "standard-currencies", authTypes = {AuthRule.IN_ADMIN}, ui = false)
	public JsonBean getStandardCurrencies() {
		JsonBean standardCurrencies = new JsonBean();
		for (AmpCurrency ampCurrencies : CurrencyUtil.getActiveAmpCurrencyByCode()) {
			standardCurrencies.set(ampCurrencies.getCurrencyCode(), ampCurrencies.getCurrencyName());
		}
		return standardCurrencies;
	}
	
	
	/**
	 * Provides inflation rates data sources
	 * @return <pre>
	 * [{
	 *    id: 123,
	 *    name: “FRED-GNPDEF”,
	 *    description: “Gross National Product: Implicit Price Deflator (USD) provide by US. Bureau of Economic Analysis”,
	 *    settings: { // for any future need to display and/or change the settings, not required for now
	 *        currency: USD,
	 *        frequency: Q,
	 *        api-token : “...”
	 *    },
	 *  },
	 *  ... // another source, e.g. CSV import
	 * ]
	 * </pre>
	 */
	
	@GET
	@Path("/inflation-sources")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "inflation-sources", authTypes = {AuthRule.IN_ADMIN}, ui = false)
	public List<JsonBean> getCurrencyInflationDataSources() {
		return CurrencyService.getInflationDataSources();
	}
	
	/**
	 * Provides inflation rates already stored in AMP, sorted by date 
	 * @return <pre>
	 * {
	 *     "USD": {"2008-01-01" : 1.2, ...},		//sorted by date
	 *     "ETB": {"2008-01-01" : 12.7, ...},
	 *     ...
	 * }
	 * </pre>
	 */
	@GET
	@Path("/inflation-rates")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "inflation-rates", authTypes = {AuthRule.IN_ADMIN}, ui = false)
	public JsonBean getAmpInflationRates() {
		return CurrencyService.getAmpInflationRates();
	}
	
	/**
	 * Save new inflation rates AMP. Input example:
	 * <pre>
	 * {
	 *     "USD": {"2008-01-01" : 1.2, ...},
	 *     "ETB": {"2008-01-01" : 12.7, ...},
	 *     ...
	 * }
	 * </pre>
	 * @param inflationRates data input
	 * @return no output with 200 HTTP status or validation error with 400 HTTP status (bad request) 
	 */
	@POST
	@Path("/inflation-rates")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "store-inflation-rates", authTypes = {AuthRule.IN_ADMIN}, ui = false)
	public JsonBean saveInflationRates(JsonBean inflationRates) {
		return CurrencyService.saveInflationRates(inflationRates);
	}
	
	/**
	 * Retrieve and provide inflation rates from the selected datasource
	 * @param sourceId source ID to query for inflation rates 
	 * @return <pre>
	 * {
	 *     "USD": {"2008-01-01" : 1.2, ...},		//sorted by date
	 *     "ETB": {"2008-01-01" : 12.7, ...},
	 *     ...
	 * }
	 * </pre>
	 */
	@GET
	@Path("/inflation-rates/{source_id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "inflation-rates-from-source", authTypes = {AuthRule.IN_ADMIN}, ui = false)
	public JsonBean getInflationRatesFromSource(@PathParam("source_id") Long sourceId) {
		return CurrencyService.getInflationRatesFromSource(sourceId);
	}
	
	/**
	 * Provides configured constant currencies per calendar
	 * @return <pre>
	 * {
	 *    "4": { // calendar_id for Gregorian Calendar in AMP Ethiopia
	 *      "USD": "2008, 2010-2015",
	 *      "ETB": "2008-2015"
	 *    },
	 *    "1": {...},
	 *    ...
	 * }
	 * </pre>
	 */
	@GET
	@Path("/constant-currencies")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "get-constant-currencies", authTypes = {AuthRule.IN_ADMIN}, ui = false)
	public JsonBean getConstantCurrencies() {
		return CurrencyService.getConstantCurrencies();
	}
	
	/**
	 * Stores constant currencies per calendar
	 * @param input <pre>
	 * {
	 *    "4": { // calendar_id for Gregorian Calendar in AMP Ethiopia
	 *      "USD": "2008, 2010-2015",
	 *      "ETB": "2008-2015"
	 *    },
	 *    "1": {...},
	 *    ...
	 * }
	 * </pre>
	 * @return nothing or error
	 */
	@POST
	@Path("/constant-currencies")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "save-constant-currencies", authTypes = {AuthRule.IN_ADMIN}, ui = false)
	public JsonBean saveConstantCurrencies(JsonBean input) {
		return CurrencyService.saveConstantCurrencies(input);
	}
	
}
