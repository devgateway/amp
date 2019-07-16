package org.digijava.kernel.ampapi.endpoints.currency;

import org.digijava.kernel.ampapi.endpoints.currency.dto.ExchangeRatesForPair;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @implicitParam X-Auth-Token|string|header
 */
@Path("currency")
public class Currencies implements ErrorReportingEndpoint {
    
    @Context
    private HttpServletRequest httpRequest;
    @Context
    private HttpServletResponse httpResponse;

    /**
     * Provides standard currency.
     * </br>
     * <dl>
     * The list doesn't include constant currencies
     * The JSON object holds information regarding:
     * <dt><b>name</b><dd> - is the currency code
     * <dt><b>value</b><dd> - is the currency name
     * </dl></br></br>
     *
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *   "AUD": "Australian Dollar 2000",
     *   "CAD": "Canadian Dollar",
     *   "CHF": "Swiss Franc",
     *   "CNY": "Chinese Yuan",
     *   "DKK": "Danish Kroner",
     *   "EUR": "Euro",
     *   "GBP": "Pound Sterling",
     *   "IDR": "Indonesian Rupiah",
     *   "JPY": "Japanese Yen",
     *   "KRW": "Korean Won",
     *   "NOK": "Norwegian Krone",
     *   "NZD": "New Zealand Dollar",
     *   "SDR": "Constant US Dollar 2012 (Ethiopian Fiscal Calendar)",
     *   "SEK": "Swedish Krona",
     *   "SGD": "Singapore ",
     *   "THB": "Thailand Baht",
     *   "USD": "US Dollar"
     * }</pre>
     *
     * @return a JSON object with all available currencies or error
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
     * Provides inflation rates data sources.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>name</b><dd> - the name of the source
     * <dt><b>description</b><dd> - the description of the source
     * <dt><b>settings</b><dd> - settings info with the currency and frequency
     * </dl></br></br>
     *
     * </br>
     * <h3>Sample Output:</h3><pre>
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
     * ]</pre>
     *
     * @return a list of JSON objects
     */
    @GET
    @Path("/inflation-sources")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "inflation-sources", authTypes = {AuthRule.IN_ADMIN}, ui = false)
    public List<JsonBean> getCurrencyInflationDataSources() {
        return CurrencyService.getInflationDataSources();
    }

    /**
     * Provides inflation rates already stored in AMP, sorted by date.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>name</b><dd> - the currency code
     * <dt><b>value</b><dd> - a list of currency rates
     * </dl></br></br>
     *
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *     "USD": {"2008-01-01" : 1.2, ...},        //sorted by date
     *     "ETB": {"2008-01-01" : 12.7, ...},
     *     ...
     * }</pre>
     *
     * @return a JSON objects with the inflation rates
     */
    @GET
    @Path("/inflation-rates")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "inflation-rates", authTypes = {AuthRule.IN_ADMIN}, ui = false)
    public JsonBean getAmpInflationRates() {
        return CurrencyService.getAmpInflationRates();
    }

    /**
     * Save new inflation rates AMP.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>name</b><dd> - the currency code
     * <dt><b>value</b><dd> - a list of date and currency rates
     * </dl></br></br>
     *
     * </br>
     * <h3>Sample Input:</h3><pre>
     * {
     *      "USD": {"2008-01-01" : 1.2},
     *      "ETB": {"2008-01-01" : 12.7),
     *     ...
     * }</pre>
     *
     * @param inflationRates a JSON object with the currency codes and a list of date and currency rates
     *
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
     * Retrieve and provide inflation rates from the selected datasource.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>name</b><dd> - the currency code
     * <dt><b>value</b><dd> - a list of currency rates
     * </dl></br></br>
     *
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *     "USD": {"2008-01-01" : 1.2, ...},        //sorted by date
     *     "ETB": {"2008-01-01" : 12.7, ...},
     *     ...
     * }</pre>
     *
     * @param sourceId source ID to query for inflation rates
     *
     * @return a JSON object with the inflation rates from the selected datasource
     */
    @GET
    @Path("/inflation-rates/{source_id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "inflation-rates-from-source", authTypes = {AuthRule.IN_ADMIN}, ui = false)
    public JsonBean getInflationRatesFromSource(@PathParam("source_id") Long sourceId) {
        return CurrencyService.getInflationRatesFromSource(sourceId);
    }

    /**
     * Provides configured constant currencies per calendar.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>name</b><dd> - the calendar_id for Gregorian Calendar in the currency AMP instance
     * <dt><b>value</b><dd> - a list of constant currencies
     * </dl></br></br>
     *
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *    "4": { // calendar_id for Gregorian Calendar in AMP Ethiopia
     *      "USD": "2008, 2010-2015",
     *      "ETB": "2008-2015"
     *    },
     *    "1": {...},
     *    ...
     * }</pre>
     *
     * @return a JSON object with the constant currencies per calendar
     */
    @GET
    @Path("/constant-currencies")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "get-constant-currencies", authTypes = {AuthRule.IN_ADMIN}, ui = false)
    public JsonBean getConstantCurrencies() {
        return CurrencyService.getConstantCurrencies();
    }

    /**
     * Stores constant currencies per calendar.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>name</b><dd> - the calendar_id for Gregorian Calendar in the currency AMP instance
     * <dt><b>value</b><dd> - a list of constant currencies
     * </dl></br></br>
     *
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *    "4": { // calendar_id for Gregorian Calendar in AMP Ethiopia
     *      "USD": "2008, 2010-2015",
     *      "ETB": "2008-2015"
     *    },
     *    "1": {...},
     *    ...
     * }</pre>
     *
     * @param input a JSON object with the constant currencies information
     *
     * @return nothing or error
     */
    @POST
    @Path("/constant-currencies")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "save-constant-currencies", authTypes = {AuthRule.IN_ADMIN}, ui = false)
    public JsonBean saveConstantCurrencies(JsonBean input) {
        return CurrencyService.saveConstantCurrencies(input);
    }

    /**
     * Retrieve exchange rates for all active currency pairs.
     * <h3>Sample Output:</h3><pre>
     * [
     *   {
     *     "rates": [
     *       {
     *         "date": "2016-06-30",
     *         "rate": 1.2
     *       },
     *       {
     *         "date": "2016-04-01",
     *         "rate": 1.21
     *       },
     *       {
     *         "date": "2016-01-01",
     *         "rate": 1.26
     *       }
     *     ],
     *     "currency-pair": {
     *       "from": "USD",
     *       "to": "EUR"
     *     }
     *   },
     *   {
     *     "rates": [
     *       {
     *         "date": "2016-09-23",
     *         "rate": 0.0010
     *       },
     *       {
     *         "date": "2016-09-25",
     *         "rate": 0.00098
     *       }
     *     ],
     *     "currency-pair": {
     *       "from": "USD",
     *       "to": "XOF"
     *     }
     *   }
     * ]
     * </pre>
     * @return rates grouped by currency pairs
     */
    @GET
    @Path("/exchange-rates")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "exchange-rates", authTypes = AuthRule.AUTHENTICATED, ui = false)
    public List<ExchangeRatesForPair> getExchangeRates() {
        return CurrencyService.INSTANCE.getExchangeRatesForPairs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class getErrorsClass() {
        return CurrencyErrors.class;
    }
}
