/*
 * UpdateCurrencyRate.java
 * Created : 03-May-2005
 */
package org.digijava.module.aim.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.form.CurrencyRateForm;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;

public class UpdateCurrencyRate extends Action {
	private static Logger logger = Logger.getLogger(UpdateCurrencyRate.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		CurrencyRateForm crForm = (CurrencyRateForm) form;
		
		logger.debug("Reset :" + crForm.isReset());
		
		if (crForm.getDoAction() == null ||
				crForm.getDoAction().equals("showRates")) {
			if (crForm.getUpdateCRateCode() != null && 
					crForm.getUpdateCRateDate() != null) {
				Date date = DateConversion.getDate(crForm.getUpdateCRateDate());
				crForm.setUpdateCRateAmount(CurrencyUtil.getExchangeRate(
						crForm.getUpdateCRateCode(),date).toString());
				crForm.setReset(false);
			} else {
				crForm.setUpdateCRateAmount(null);
			}
			if (crForm.getCurrencyCodes() == null) {
				crForm.setCurrencyCodes(DbUtil.getAmpCurrency());
				crForm.setUpdateCRateCode(null);
				crForm.setUpdateCRateDate(null);
				crForm.setUpdateCRateId(null);				
			}

		} else {
			AmpCurrencyRate cRate = new AmpCurrencyRate();
			Double rate = new Double(
					Double.parseDouble(crForm.getUpdateCRateAmount()));
			cRate.setExchangeRate(rate);
			cRate.setExchangeRateDate(
					DateConversion.getDate(crForm.getUpdateCRateDate()));
			cRate.setToCurrencyCode(crForm.getUpdateCRateCode());
			CurrencyUtil.saveCurrencyRate(cRate);
			
			crForm.setAllRates(null);
			crForm.setUpdateCRateAmount(null);
			crForm.setUpdateCRateCode(null);
			crForm.setUpdateCRateDate(null);
			crForm.setUpdateCRateId(null);
			crForm.setDoAction(null);

		}
		return mapping.findForward("forward");
	}
}