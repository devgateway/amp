package org.digijava.kernel.ampapi.endpoints.currency;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.digijava.kernel.ampapi.endpoints.currency.dto.ExchangeRatesForPair;
import org.digijava.kernel.ampapi.endpoints.currency.dto.InflationDataSource;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("currency")
@Api("currency")
public class Currencies {
    
    @Context
    private HttpServletRequest httpRequest;
    @Context
    private HttpServletResponse httpResponse;

    @GET
    @Path("/currencies")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "standard-currencies", authTypes = {AuthRule.IN_ADMIN}, ui = false)
    @ApiOperation(
            value = "Provides a list of standard currencies.",
            notes = "The list doesn't include constant currencies.\n\n"
                    + "Returned objects has key as currency code and value as currency name.\n\n"
                    + "### Sample Output\n"
                    + "```\n"
                    + "{\n"
                    + "  \"CAD\": \"Canadian Dollar\",\n"
                    + "  \"CHF\": \"Swiss Franc\"\n"
                    + "}\n"
                    + "```")
    public Map<String, String> getStandardCurrencies() {
        return CurrencyUtil.getActiveAmpCurrencyByCode().stream()
                .collect(Collectors.toMap(AmpCurrency::getCurrencyCode, AmpCurrency::getCurrencyName,
                        (k, v) -> k, LinkedHashMap::new));
    }
    
    @GET
    @Path("/inflation-sources")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "inflation-sources", authTypes = {AuthRule.IN_ADMIN}, ui = false)
    @ApiOperation(value = "Provides inflation rates data sources.")
    public List<InflationDataSource> getCurrencyInflationDataSources() {
        return CurrencyService.getInflationDataSources();
    }

    @GET
    @Path("/inflation-rates")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "inflation-rates", authTypes = {AuthRule.IN_ADMIN}, ui = false)
    @ApiOperation(
            value = "Provides inflation rates already stored in AMP, sorted by date.",
            notes = "The object holds information regarding:\n\n"
                    + "Field|Description\n"
                    + "---|---\n"
                    + "name|the currency code\n"
                    + "value|a list of currency rates\n\n"
                    + "### Sample Output\n"
                    + "```\n"
                    + "{\n"
                    + "    \"USD\": {\"2008-01-01\" : 1.2, ...},        //sorted by date\n"
                    + "    \"ETB\": {\"2008-01-01\" : 12.7, ...},\n"
                    + "    ...\n"
                    + "}\n")
    public Map<String, Map<String, Double>> getAmpInflationRates() {
        return CurrencyService.getAmpInflationRates();
    }

    @POST
    @Path("/inflation-rates")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "store-inflation-rates", authTypes = {AuthRule.IN_ADMIN}, ui = false)
    @ApiOperation(
            value = "Update inflation rates",
            notes = "The object holds information regarding:\n"
                    + "\n"
                    + "Field|Description\n"
                    + "---|---\n"
                    + "name|the currency code\n"
                    + "value|a list of date and currency rates\n"
                    + "\n"
                    + "### Sample Input\n"
                    + "```\n"
                    + "{\n"
                    + "     \"USD\": {\"2008-01-01\" : 1.2},\n"
                    + "     \"ETB\": {\"2008-01-01\" : 12.7),\n"
                    + "    ...\n"
                    + "}\n"
                    + "```\n")
    public void saveInflationRates(Map<String, Map<String, Double>> inflationRates) {
        CurrencyService.saveInflationRates(inflationRates);
    }

    @GET
    @Path("/inflation-rates/{source_id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "inflation-rates-from-source", authTypes = {AuthRule.IN_ADMIN}, ui = false)
    @ApiOperation(
            value = "Retrieve and provide inflation rates from the selected datasource.",
            notes = "Check /inflation-rates method to see the example of result.")
    public Map<String, Map<String, Double>> getInflationRatesFromSource(
            @ApiParam("source ID to query for inflation rates") @PathParam("source_id") Long sourceId) {
        return CurrencyService.getInflationRatesFromSource(sourceId);
    }

    @GET
    @Path("/constant-currencies")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "get-constant-currencies", authTypes = {AuthRule.IN_ADMIN}, ui = false)
    @ApiOperation(
            value = "Provides configured constant currencies per calendar.",
            notes = "The object holds information regarding:\n"
                    + "\n"
                    + "Field|Description\n"
                    + "---|---\n"
                    + "name|the calendar_id for Gregorian Calendar in the currency AMP instance\n"
                    + "value|a list of constant currencies\n"
                    + "\n"
                    + "### Sample Output\n"
                    + "```\n"
                    + "{\n"
                    + "   \"4\": { // calendar_id for Gregorian Calendar in AMP Ethiopia\n"
                    + "     \"USD\": \"2008, 2010-2015\",\n"
                    + "     \"ETB\": \"2008-2015\"\n"
                    + "   },\n"
                    + "   \"1\": {...},\n"
                    + "   ...\n"
                    + "}\n"
                    + "```")
    public  Map<String, Map<String, String>> getConstantCurrencies() {
        return CurrencyService.getConstantCurrencies();
    }

    @POST
    @Path("/constant-currencies")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "save-constant-currencies", authTypes = {AuthRule.IN_ADMIN}, ui = false)
    @ApiOperation(
            value = "Stores constant currencies per calendar.",
            notes = "The object holds information regarding:\n"
                    + "\n"
                    + "Field|Description\n"
                    + "---|---\n"
                    + "name|the calendar_id for Gregorian Calendar in the currency AMP instance\n"
                    + "value|a list of constant currencies\n"
                    + "\n"
                    + "### Sample Input\n"
                    + "```\n"
                    + "{\n"
                    + "   \"4\": { // calendar_id for Gregorian Calendar in AMP Ethiopia\n"
                    + "     \"USD\": \"2008, 2010-2015\",\n"
                    + "     \"ETB\": \"2008-2015\"\n"
                    + "   },\n"
                    + "   \"1\": {...},\n"
                    + "   ...\n"
                    + "}\n"
                    + "```")
    public void saveConstantCurrencies(@ApiParam("a JSON object with the constant currencies information")
                                                       Map<String, Map<String, String>> input) {
        CurrencyService.saveConstantCurrencies(input);
    }

    @GET
    @Path("/exchange-rates")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "exchange-rates", authTypes = AuthRule.AUTHENTICATED, ui = false)
    @ApiOperation("Retrieve exchange rates for all active currency pairs.")
    public List<ExchangeRatesForPair> getExchangeRates() {
        return CurrencyService.INSTANCE.getExchangeRatesForPairs();
    }

}
