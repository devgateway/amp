package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.form.CurrencyForm;
import org.digijava.module.aim.util.CurrencyUtil;

public class ChangeCurrencyStatus extends Action {
    
    private static Logger logger = Logger.getLogger(ChangeCurrencyStatus.class);
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {

        CurrencyForm crForm = (CurrencyForm) form;
        logger.debug("In ChangeCurrencyStatus");
        try {
            String currCode = request.getParameter("currCode");
            int status = Integer.parseInt(request.getParameter("status"));
            CurrencyUtil.updateCurrencyStatus(currCode,status);
            Iterator itr = crForm.getAllCurrencies().iterator();
            AmpCurrency curr = null;
            while (itr.hasNext()) {
                curr = (AmpCurrency) itr.next();
                if (curr.getCurrencyCode().equals(currCode)) {
                    curr.setActiveFlag(new Integer(status));
                    break;
                }
            }
            crForm.getAllCurrencies().remove(curr);
            if (crForm.getAllCurrencies() == null) {
                crForm.setAllCurrencies(new ArrayList());
            }
            crForm.getAllCurrencies().add(curr);
            List temp = new ArrayList(crForm.getAllCurrencies());
            Collections.sort(temp);
            crForm.setAllCurrencies(temp);
            
        } catch (NumberFormatException nfe) {
            logger.error("Exception from ChangeCurrencyStatus action");
            logger.error("Trying to parse " + request.getParameter("status") + "to int");
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return mapping.findForward("forward");
    }
}
