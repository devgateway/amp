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
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.CountryUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.form.CurrencyForm;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;

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
						if (curr.getCountryId() != null)
							crForm.setCountryIso(curr.getCountryId().getIso());
						else 
							crForm.setCountryIso(null);
						
						crForm.setId(curr.getAmpCurrencyId());
						break;
					}
				}
			} else {
				crForm.setId(new Long(-1));
				crForm.setCountryIso(null);
				crForm.setCurrencyCode(null);
				crForm.setCurrencyName(null);
				crForm.setExchangeRate(null);
				crForm.setExchangeRateDate(null);				
			}
			if (crForm.getCountries() == null || 
					crForm.getCountries().size() < 1) {
				List countries = org.digijava.module.um.util.DbUtil.getCountries();
				List sortedCountries = new ArrayList();
				Iterator iterator = countries.iterator();
				while (iterator.hasNext()) {
					Country item = (Country) iterator.next();
					if (item != null) {
						if (item.getCountryName() != null &&
								item.getCountryName().length() > 30) {
							String name = item.getCountryName().substring(0,27);
							item.setCountryName(name + "...");
						}
					}					
					sortedCountries.add(item);
				}
				//Collections.sort(sortedCountries, TrnUtil.countryNameComparator);
				crForm.setCountries(sortedCountries);				
			}
			crForm.setCloseFlag("false");
		} else {
			boolean currCodeExist = CurrencyUtil.currencyCodeExist(crForm.getCurrencyCode(),crForm.getId());
			if (!currCodeExist) {
				AmpCurrency curr = null;
				if (crForm.getId() != null && crForm.getId().longValue() > 0) {
					// edit
					curr = CurrencyUtil.getAmpcurrency(crForm.getId());
					curr.setCurrencyCode(crForm.getCurrencyCode());
					curr.setCurrencyName(crForm.getCurrencyName());
					curr.setAmpCurrencyId(crForm.getId());
					curr.setCountryId(DbUtil.getDgCountry(crForm.getCountryIso()));
					CurrencyUtil.updateCurrency(curr);
				} else {
					// add
					curr = new AmpCurrency();
					curr.setCurrencyCode(crForm.getCurrencyCode());
					curr.setCurrencyName(crForm.getCurrencyName());
					curr.setAmpCurrencyId(crForm.getId());
					curr.setActiveFlag(new Integer(1));
					curr.setCountryId(DbUtil.getDgCountry(crForm.getCountryIso()));
					CurrencyUtil.saveCurrency(curr);
				}
				crForm.setCurrencyCode(null);
				crForm.setCurrencyName(null);
				crForm.setExchangeRate(null);
				crForm.setExchangeRateDate(null);
				crForm.setAllCurrencies(null);
				crForm.setDoAction(null);
				crForm.setId(null);		
				crForm.setCloseFlag("true");
 			} else {
 				logger.info("Error: Currency Code already exist");
 				ActionErrors errors = new ActionErrors();
 				errors.add("currencyCode", new ActionError("error.aim.updateCurrency.currencyCodeAlreadyExist"));
 				saveErrors(request, errors);
 				crForm.setCloseFlag("false");
 				return mapping.getInputForward(); 				
 			}

		}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return mapping.findForward("forward");
	}

}

