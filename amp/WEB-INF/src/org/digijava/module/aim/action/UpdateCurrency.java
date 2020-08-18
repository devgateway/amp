/*
 * UpdateCurrency.java
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.form.CurrencyForm;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;

public class UpdateCurrency extends Action {

    private static Logger logger = Logger.getLogger(UpdateCurrency.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String) session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                return mapping.findForward("index");
            }
        }

        try {

            CurrencyForm crForm = (CurrencyForm) form;
            crForm.setCloseFlag("false");
            crForm.setErrors(new ArrayList());
            Collection errors =crForm.getErrors();

            AmpCurrency curr = null;

            if (crForm.getCurrencyCode() != null &&
                !crForm.getCurrencyCode().equals("") &&
                crForm.getAllCurrencies()!=null &&
                crForm.getDoAction().equalsIgnoreCase("show")) {

                Iterator itr = crForm.getAllCurrencies().iterator();
                while (itr.hasNext()) {
                    curr = (AmpCurrency) itr.next();
                    if (curr.getCurrencyCode().equals(crForm.getCurrencyCode())) {
                        if (curr.getCountryLocation() != null) {
                            crForm.setCountryId(curr.getCountryLocation().getId() );
                            crForm.setCountryName( curr.getCountryLocation().getName() );
                        }
                        crForm.setCurrencyName(curr.getCurrencyName());
                        crForm.setId(curr.getAmpCurrencyId());
                        crForm.setActiveFlag(curr.getActiveFlag());
                        break;
                    }
                }
            } else if(crForm.getDoAction().equalsIgnoreCase("new")){
                crForm.setId(new Long( -1));
                crForm.setCountryName(null);
                crForm.setCountryId(Long.valueOf( -1));
                crForm.setCurrencyCode(null);
                crForm.setCurrencyName(null);
                crForm.setExchangeRate(null);
                crForm.setExchangeRateDate(null);
            }

            Collection<AmpCategoryValueLocations> countries = DynLocationManagerUtil.getLocationsByLayer(
                    CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0);
            crForm.setCountries(countries);

            if (crForm.getCurrencyCode() != null && crForm.getDoAction().equals("add")) {
                curr = CurrencyUtil.getCurrencyByCode(crForm.getCurrencyCode());
                if (curr != null) {
                    errors.add("Currency with same code alrady exists");
                    return mapping.findForward("forward");
                }else{
                    curr = new AmpCurrency();
                }

                if (crForm.getCountryId() == null ||
                    crForm.getCountryId().equals(Long.valueOf( -1))) {
                    return mapping.findForward("forward");
                } else {
                    AmpCategoryValueLocations cn = DynLocationManagerUtil.getLocation( crForm.getCountryId(), false);
                    if (cn != null) {
                        curr.setCountryLocation(cn);
                    }
                }

                saveCurr(curr,crForm);
                crForm.setCloseFlag("true");
                return mapping.findForward("forward");

            } else if (crForm.getDoAction().equals("new")) {
                crForm.setDoAction("add");
            } else if (crForm.getCurrencyCode() != null && crForm.getDoAction().equals("edit")) {
                curr = CurrencyUtil.getCurrencyByCode(crForm.getCurrencyCode());
                if (curr == null) {
                    curr = new AmpCurrency();
                }
                curr.setCurrencyName(crForm.getCurrencyName()); 
                if (crForm.getCountryId() == null ||
                    crForm.getCountryId().equals(Long.valueOf( -1))) {
                    return mapping.findForward("forward");
                } else {
                    AmpCategoryValueLocations cn = DynLocationManagerUtil.getLocation(crForm.getCountryId(), false);
                    if (cn != null) {
                        curr.setCountryLocation(cn);
                    }
                }

                saveCurr(curr,crForm);
                crForm.setCloseFlag("true");
                return mapping.findForward("forward");
            } else if (crForm.getCurrencyCode() != null && crForm.getDoAction().equals("show")){
                crForm.setDoAction("edit");
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return mapping.findForward("forward");
    }

    private void saveCurr(AmpCurrency curr, CurrencyForm crForm) {
        curr.setCurrencyCode(crForm.getCurrencyCode());
        curr.setCurrencyName(crForm.getCurrencyName());
        curr.setAmpCurrencyId(crForm.getId());
        if (crForm.getActiveFlag()!=null){
            curr.setActiveFlag(crForm.getActiveFlag());
        }else{
            curr.setActiveFlag(new Integer(1));
        }
        AmpCurrencyRate cRate = null;
        if (crForm.getExchangeRate() != null &&
            crForm.getExchangeRateDate() != null &&
            crForm.getExchangeRateDate().trim().length() > 0) {
            cRate = new AmpCurrencyRate();
            cRate.setExchangeRate(crForm.getExchangeRate());
            cRate.setExchangeRateDate(
                DateConversion.getDate(crForm.getExchangeRateDate()));
            cRate.setToCurrencyCode(crForm.getCurrencyCode());

        }
        CurrencyUtil.saveCurrency(curr, cRate);
        crForm.setCountryName(null);
        crForm.setCountryId(Long.valueOf( -1));
        crForm.setCurrencyCode(null);
        crForm.setCurrencyName(null);
        crForm.setExchangeRate(null);
        crForm.setExchangeRateDate(null);
        crForm.setAllCurrencies(null);
        crForm.setDoAction(null);
        crForm.setId(null);
    }
}

