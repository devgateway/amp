package org.digijava.module.aim.action ;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.form.AddCurrencyForm;
import org.digijava.module.aim.util.CurrencyUtil;

public class EditCurrency extends Action {

          private static Logger logger = Logger.getLogger(EditCurrency.class);

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

                     AddCurrencyForm editForm = (AddCurrencyForm) form;

                     logger.debug("In edit currency");

                     if (editForm.getCurrencyId() != null) {
                                if (session.getAttribute("ampCurrencies") != null) {
                                    session.removeAttribute("ampCurrencies");
                                }
                            
                                AmpCurrency ampCurr = new AmpCurrency();
                                AmpCurrencyRate ampCurrRate = new AmpCurrencyRate();

                                ampCurr.setAmpCurrencyId(editForm.getCurrencyId());
                                ampCurr.setCurrencyCode(editForm.getCurrencyCode());
                                ampCurr.setCountryName(editForm.getCountryName());
                                ampCurrRate.setAmpCurrencyRateId(editForm.getCurrencyRateId());
                                ampCurrRate.setToCurrencyCode(editForm.getCurrencyCode());
                                ampCurrRate.setExchangeRate(editForm.getExchangeRate());
                                
                                CurrencyUtil.updateCurrency(ampCurr,ampCurrRate);
                                logger.debug("Currency updated");
                     }
                     return mapping.findForward("forward");
          }
}

