/*
 * UpdateCurrency.java
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.form.CurrencyForm;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.util.CurrencyUtil;

public class UpdateCurrency extends Action {

	private static Logger logger = Logger.getLogger(UpdateCurrency.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String)session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}
			
		try {
			
		CurrencyForm crForm = (CurrencyForm) form;

		if (crForm.getDoAction() == null ||
				crForm.getDoAction().equals("showCurrencies")) {
			if (crForm.getCurrencyCode() != null ) {
				Iterator itr = crForm.getAllCurrencies().iterator();
				AmpCurrency curr = null;
				while (itr.hasNext()) {
					curr = (AmpCurrency) itr.next();
					if (curr.getCurrencyCode().equals(crForm.getCurrencyCode())) {
						crForm.setCurrencyName(curr.getCurrencyName());
						crForm.setCountryName(curr.getCountryName());
						crForm.setId(curr.getAmpCurrencyId());
						break;
					}
				}
			} else {
				crForm.setId(new Long(-1));
				crForm.setCountryName(null);
				crForm.setCurrencyCode(null);
				crForm.setCurrencyName(null);
				crForm.setExchangeRate(null);
				crForm.setExchangeRateDate(null);				
			}
			if (crForm.getCountries() == null || 
					crForm.getCountries().size() < 1) {
				List countries = org.digijava.module.um.util.DbUtil.getCountries();
				HashMap countriesMap = new HashMap();
				Iterator iterator = TrnUtil.getCountries(
						RequestUtils.getNavigationLanguage(request).getCode())
						.iterator();
				while (iterator.hasNext()) {
					TrnCountry item = (TrnCountry) iterator.next();
					countriesMap.put(item.getIso(), item);
				}
				//sort countries
				List sortedCountries = new ArrayList();
				iterator = countries.iterator();
				while (iterator.hasNext()) {
					Country item = (Country) iterator.next();
					sortedCountries.add(countriesMap.get(item.getIso()));
				}
				Collections.sort(sortedCountries, TrnUtil.countryNameComparator);
				crForm.setCountries(sortedCountries);				
			}

		} else {
			AmpCurrency curr = new AmpCurrency();
			curr.setCountryName(crForm.getCountryName());
			curr.setCurrencyCode(crForm.getCurrencyCode());
			curr.setCurrencyName(crForm.getCurrencyName());
			curr.setAmpCurrencyId(crForm.getId());
			curr.setActiveFlag(new Integer(1));
			AmpCurrencyRate cRate = new AmpCurrencyRate();
			if (crForm.getExchangeRate() != null && 
					crForm.getExchangeRateDate() != null && 
					crForm.getExchangeRateDate().trim().length() > 0) {
				cRate.setExchangeRate(crForm.getExchangeRate());
				cRate.setExchangeRateDate(
						DateConversion.getDate(crForm.getExchangeRateDate()));
				cRate.setToCurrencyCode(crForm.getCurrencyCode());
				
			}
			CurrencyUtil.saveCurrency(curr,cRate);
			crForm.setCountryName(null);
			crForm.setCurrencyCode(null);
			crForm.setCurrencyName(null);
			crForm.setExchangeRate(null);
			crForm.setExchangeRateDate(null);
			crForm.setAllCurrencies(null);
			crForm.setDoAction(null);
			crForm.setId(null);
		}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return mapping.findForward("forward");
	}

}

