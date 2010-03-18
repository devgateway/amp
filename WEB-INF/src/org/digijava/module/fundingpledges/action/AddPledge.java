package org.digijava.module.fundingpledges.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.fundingpledges.form.PledgeForm;

public class AddPledge extends Action {

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        	
    		PledgeForm plForm = (PledgeForm) form;
    	
    		Collection currencies = CurrencyUtil.getAmpCurrency();
    		ArrayList<AmpCurrency> validcurrencies = new ArrayList<AmpCurrency>();
	    	plForm.setValidcurrencies(validcurrencies);
	        if(currencies!=null && currencies.size()>0){
	      	  for (Iterator iter = currencies.iterator(); iter.hasNext();) {
	      			AmpCurrency element = (AmpCurrency) iter.next();
	      			 if( CurrencyUtil.isRate(element.getCurrencyCode())== true)
	      					{
	      				 		plForm.getValidcurrencies().add((CurrencyUtil.getCurrencyByCode(element.getCurrencyCode())));
	      					}
	      			}
	        }
            return mapping.findForward("forward");

    }
}
