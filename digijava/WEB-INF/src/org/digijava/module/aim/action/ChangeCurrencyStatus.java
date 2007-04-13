package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.form.CurrencyForm;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;

public class ChangeCurrencyStatus extends Action {
	
	private static Logger logger = Logger.getLogger(ChangeCurrencyStatus.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {

		ActionErrors actionErrors	= new ActionErrors();
		CurrencyForm crForm = (CurrencyForm) form;
		logger.debug("In ChangeCurrencyStatus");
		try { 
			String currCode = request.getParameter("currCode");
			if((request.getParameter("action")!=null)&&(request.getParameter("action").equals("deleteCurrency"))) {
				Collection fundingDetailsForCurrencyCode	= DbUtil.getFundingDetailsForCurrencyByCode(currCode);
				if ( fundingDetailsForCurrencyCode == null ) {
					ActionError error	= new ActionError("error.aim.deleteCurrency.currencyCodeDoesNotExist");
					actionErrors.add(ActionErrors.GLOBAL_ERROR, error);
					super.saveErrors(request, actionErrors);
					return mapping.findForward("forward");
				}
				else {
					if ( !fundingDetailsForCurrencyCode.isEmpty() ) {
						ActionError error	= new ActionError("error.aim.deleteCurrency.actvitiesAreUsingTheCurrency");
						actionErrors.add(ActionErrors.GLOBAL_ERROR, error);
						super.saveErrors(request, actionErrors);
						return mapping.findForward("forward");
					}
				}
				
				CurrencyUtil.deleteCurrency(currCode);
				
				Iterator itr = crForm.getAllCurrencies().iterator();
				AmpCurrency curr = null;
				while (itr.hasNext()) {
					curr = (AmpCurrency) itr.next();
					if (curr.getCurrencyCode().equals(currCode)) {
						break;
					}
				}
				
				crForm.getAllCurrencies().remove(curr);
				if (crForm.getAllCurrencies() == null) {
					crForm.setAllCurrencies(new ArrayList());
				}
				List temp = new ArrayList(crForm.getAllCurrencies());
				Collections.sort(temp);
				crForm.setAllCurrencies(temp);
				
			}
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