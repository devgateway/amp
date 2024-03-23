package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.AddCurrencyForm;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Iterator;

public class GetCurrency extends Action {

          private static Logger logger = Logger.getLogger(GetCurrency.class);

          public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String)session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                return mapping.findForward("index");
            }
        }
                     
                     AddCurrencyForm currForm = (AddCurrencyForm) form;

                     logger.debug("In get currency");
                     String action = request.getParameter("action");
    
                     if (request.getParameter("id") != null) {

                                Long currId = new Long(Long.parseLong(request.getParameter("id")));
                                Currency currency = CurrencyUtil.getCurrency(currId);

                                currForm.setCurrencyId(currId);
                                currForm.setCurrencyRateId(currency.getCurrencyRateId());
                                currForm.setCurrencyRateId(currency.getCurrencyRateId());
                                currForm.setCountryName(currency.getCountryName());
                                currForm.setCurrencyCode(currency.getCurrencyCode());
                                currForm.setExchangeRate(currency.getExchangeRate());
                                logger.debug("values set.");
                     }

                     if (action != null && action.equals("edit")) {
                                return mapping.findForward("editCurrency");
                     } else if (action != null && action.equals("delete")) {
                                Iterator itr = DbUtil.getFundingDetWithCurrId(currForm.getCurrencyId()).iterator();
                                if (itr.hasNext()) {
                                          currForm.setFlag("fundingDetailsReferences");
                                } else {
                                          currForm.setFlag("delete");
                                }
                                
                                return mapping.findForward("deleteCurrency");
                     } else if (action == null){
                                return mapping.findForward("forward");
                     } else {
                                return null;
                     }
          }
}

