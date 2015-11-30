package org.digijava.kernel.ampapi.endpoints.common;

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
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpInflationRate;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.util.caching.AmpCaching;

import java.util.function.Function;

@Path("currencies")
public class Currencies {
	
	@Context
	private HttpServletRequest httpRequest;
	@Context
	private HttpServletResponse httpResponse;
	
	
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
			elem.set("currencyCode", rate.getBaseCurrency().getCurrencyCode());
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
			if (!okCurrencyCodes.contains(ir.getBaseCurrency().getCurrencyCode())) {
				//ratesToDelete.add(ir);
				currenciesToDelete.add(ir.getBaseCurrency());
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
		return new AmpInflationRate(baseCurrency, 
				PersistenceManager.getInteger(raw.get("year")), 
				PersistenceManager.getDouble(raw.get("inflationRate")),
				PersistenceManager.getBoolean(raw.get("constantCurrency")));
	}
}
