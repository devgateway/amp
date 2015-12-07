package org.digijava.kernel.ampapi.endpoints.currency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.VirtualCurrenciesMaintainer;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpInflationRate;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.util.caching.AmpCaching;

import com.google.common.base.Function;

//DEFLATOR: cleanup unused
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
	
	/******************************** REVIEW AND CLEANUP below *************************************************/
	
	@GET
	@Path("/inflatableCurrencies")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	/**
	 * List<{name: String, code: String, id: long}>
	 * @return
	 */
	public List<Map<String, Object>> getInflatableCurrencies() {
		AmpCurrency baseCurrency = AmpARFilter.getDefaultCurrency();
		Map<String, Object> rs = new HashMap<>();
		rs.put("name", baseCurrency.getCurrencyName());
		rs.put("code", baseCurrency.getCurrencyCode());
		rs.put("id", baseCurrency.getAmpCurrencyId());
		return Arrays.asList(rs);
	}
	
	@GET
	@Path("/getInflationRates")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	/**
	 * 
	 * @return
	 */
	public Map<String, List<JsonBean>> getInflationRates() {
		List<AmpInflationRate> rates = PersistenceManager.getSession()
			.createQuery("FROM " + AmpInflationRate.class.getName())
			.list();
		List<JsonBean> jsonRates = new ArrayList<>();
		for(AmpInflationRate rate:rates) {
			JsonBean elem = new JsonBean();
			elem.set("inflationRate", rate.getInflationRate());
			elem.set("year", rate.getYear());
			elem.set("constantCurrency", rate.isConstantCurrency());
			elem.set("currencyCode", rate.getCurrency().getCurrencyCode());
			jsonRates.add(elem);
		}
		return AmpCollections.distribute(jsonRates, new Function<JsonBean, String>() {
			@Override public String apply(JsonBean arg0) {
				return arg0.getString("currencyCode");
			}
		});
	}
	
	/**
	 * deletes all virtual currencies and their rates, as long as they are NOT based on a real currency which is on the white list
	 * @param okCurrencyCodes
	 */
	protected static void deleteAllVirtualCurrenciesExceptGiven(Set<String> okCurrencyCodes) {
		PersistenceManager.getSession().flush();
		SortedSet<AmpCurrency> currenciesToDelete = new TreeSet<>();
		List<AmpInflationRate> allInflationRates = PersistenceManager.getSession().createQuery("FROM " + AmpInflationRate.class.getName()).list();
		for(AmpInflationRate ir:allInflationRates) {
			if (!okCurrencyCodes.contains(ir.getCurrency().getCurrencyCode())) {
				//ratesToDelete.add(ir);
				currenciesToDelete.add(ir.getCurrency());
				PersistenceManager.getSession().delete(ir);
			}
		}
		PersistenceManager.getSession().flush();
		List<AmpCurrency> allCurrencies = PersistenceManager.getSession().createQuery("FROM " + AmpCurrency.class.getName()).list();
		for(AmpCurrency curr:allCurrencies) {
			if (!okCurrencyCodes.contains(curr.getCurrencyCode()))
				currenciesToDelete.add(curr);
		}
		for(AmpCurrency curr:currenciesToDelete) {
			if (curr.isVirtual()) {
				VirtualCurrenciesMaintainer.deleteCurrencyIfPossible(curr);
			} else {
				new VirtualCurrenciesMaintainer().markDisappearedCurrenciesAsUnavailable(new ArrayList<AmpInflationRate>(), curr, new HashSet<String>());
			}
		}
		PersistenceManager.getSession().flush();
		AmpCaching.getInstance().currencyCache.reset();
	}
	
	@POST
	@Path("/setInflationRate/{currCode}")
	@ApiMethod(authTypes = {AuthRule.IN_ADMIN}, id = "setInflationRates", ui = false)
	/**
	 * expects an input of the type "rates: [{year: integer, inflationRate: double, constantCurrency: boolean}]"
	 * @param currCode
	 * @param param
	 */
	public void setInflationRates(@PathParam("currCode") String currCode, final JsonBean param) {
		if (!TeamUtil.isCurrentMemberAdmin())
			throw new RuntimeException("you must be an admin");
		if (!currCode.equals(AmpARFilter.getDefaultCurrency().getCurrencyCode()))
			throw new RuntimeException("not allowed to set inflation rates for currency: " + currCode);
		
		deleteAllVirtualCurrenciesExceptGiven(new HashSet<String>(Arrays.asList(currCode)));
		AmpCurrency baseCurrency = CurrencyUtil.getCurrencyByCode(currCode);
		List<AmpInflationRate> oldRates = PersistenceManager.getSession().createQuery("FROM " + AmpInflationRate.class.getName() + " ir WHERE ir.baseCurrency.currencyCode=:currCode")
			.setString("currCode", currCode)
			.list();
		
		SortedMap<Integer, AmpInflationRate> existingEntries = distributeByYear(oldRates);				
		SortedMap<Integer, AmpInflationRate> newEntries = new TreeMap<>();
		
		List<Map<String, ?>> arr = (List) param.get("rates");
		for(Map<String, ?> entry:arr) {
			AmpInflationRate air = buildInflationRate(baseCurrency, entry);
			newEntries.put(air.getYear(), air);
//			PersistenceManager.getSession().save(air);
		}
		for(int year:existingEntries.keySet()) {
			if (!newEntries.containsKey(year)) {
				// year has disappeared -> delete entry altogether
				PersistenceManager.getSession().delete(existingEntries.get(year));
				PersistenceManager.getSession().flush();
			}
		}
		for(int year:newEntries.keySet()) {
			AmpInflationRate newRate = newEntries.get(year);
			AmpInflationRate rateToSave;
			if (existingEntries.containsKey(year)) {
				// must UPDATE instead of INSERT
				rateToSave = existingEntries.get(year);
				rateToSave.importDataFrom(newRate);
			} else {
				rateToSave = newRate;
			}
			PersistenceManager.getSession().save(rateToSave);
		}
		PersistenceManager.getSession().flush();
		CurrencyUtil.maintainVirtualCurrencies();
	}
	
	protected SortedMap<Integer, AmpInflationRate> distributeByYear(Collection<AmpInflationRate> rates) {
		SortedMap<Integer, AmpInflationRate> res = new TreeMap<>();
		for(AmpInflationRate air:rates)
			res.put(air.getYear(), air);
		return res;
	}
	
	protected AmpInflationRate buildInflationRate(AmpCurrency baseCurrency, Map<String, ?> raw) {
		//DEFLATOR: review
		return null;/*new AmpInflationRate(baseCurrency, 
				PersistenceManager.getInteger(raw.get("year")), 
				PersistenceManager.getDouble(raw.get("inflationRate")),
				PersistenceManager.getBoolean(raw.get("constantCurrency")));*/
	}
}
