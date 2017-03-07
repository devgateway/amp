package org.digijava.kernel.ampapi.endpoints.gpi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpGPINiAidOnBudget;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * 
 * @author gerald
 *
 */
public class GPIDataService {
	public static JsonBean getAidOnBudgetById(Long id) {
		AmpGPINiAidOnBudget aidOnBudget = GPIUtils.getAidOnBudgetById(id);
		if (aidOnBudget != null)
			return modelToJsonBean(aidOnBudget);
		else
			return null;
	}

	public static JsonBean getAidOnBudgetList() {
		List<AmpGPINiAidOnBudget> aidOnBudgetList = GPIUtils.getAidOnBudgetList();
		JsonBean data = new JsonBean();
		List<JsonBean> lst = new ArrayList<>();
		for (AmpGPINiAidOnBudget aidOnBudget : aidOnBudgetList) {
			lst.add(modelToJsonBean(aidOnBudget));
		}
		data.set("data", lst);
		return data;
	}

	private static JsonBean modelToJsonBean(AmpGPINiAidOnBudget aidOnBudget) {
		JsonBean data = new JsonBean();
		data.set(GPIEPConstants.FIELD_ID, aidOnBudget.getAmpGPINiAidOnBudgetId());
		data.set(GPIEPConstants.FIELD_DONOR_ID, aidOnBudget.getDonor().getAmpOrgId());
		data.set(GPIEPConstants.FIELD_CURRENCY_CODE, aidOnBudget.getCurrency().getCurrencyCode());
		data.set(GPIEPConstants.FIELD_AMOUNT, aidOnBudget.getAmount());		
		data.set(GPIEPConstants.FIELD_DATE, DateTimeUtil.formatDate(aidOnBudget.getIndicatorDate(), GPIEPConstants.DATE_FORMAT));		
		return data;
	}

	private static AmpGPINiAidOnBudget getAidOnBudget(JsonBean data) {
		Long id;
		AmpGPINiAidOnBudget aidOnBudget = null;
		if (data.getString(GPIEPConstants.FIELD_ID) != null
				&& NumberUtils.isNumber(data.getString(GPIEPConstants.FIELD_ID))) {
			id = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_ID)));
			aidOnBudget = GPIUtils.getAidOnBudgetById(id);
		} else {
			aidOnBudget = new AmpGPINiAidOnBudget();
		}

		if (data.get(GPIEPConstants.FIELD_CURRENCY_CODE) != null) {
			String currencyCode = data.getString((GPIEPConstants.FIELD_CURRENCY_CODE));
			aidOnBudget.setCurrency(CurrencyUtil.getAmpcurrency(currencyCode));
		}

		if (data.getString(GPIEPConstants.FIELD_DONOR_ID) != null) {
			Long donorId = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_DONOR_ID)));
			aidOnBudget.setDonor(GPIUtils.getOrganisation(donorId));
		}

		if (data.get(GPIEPConstants.FIELD_AMOUNT) != null) {
			aidOnBudget.setAmount((Double.parseDouble(String.valueOf(data.get(GPIEPConstants.FIELD_AMOUNT)))));
		}

		if (data.getString(GPIEPConstants.FIELD_DATE) != null) {
			Date date = DateTimeUtil.parseDate(data.getString(GPIEPConstants.FIELD_DATE), GPIEPConstants.DATE_FORMAT);
			aidOnBudget.setIndicatorDate(date);
		}

		return aidOnBudget;
	}
	
	public static JsonBean saveAidOnBudget(JsonBean data) {
		JsonBean result = new JsonBean();		
		AmpGPINiAidOnBudget aidOnBudget = getAidOnBudget(data);		
		if(aidOnBudget.getAmpGPINiAidOnBudgetId() == null && GPIUtils.similarRecordExists(aidOnBudget.getIndicatorDate(), aidOnBudget.getDonor().getAmpOrgId())){
			return ApiError.toError(GPIErrors.DATE_DONOR_COMBINATION_EXISTS);
		}
		
		GPIUtils.saveAidOnBudget(aidOnBudget);		
		result.set(GPIEPConstants.RESULT, GPIEPConstants.SAVED);
		result.set(GPIEPConstants.DATA, modelToJsonBean(aidOnBudget));
		return result;
	}
	
	public static JsonBean delete(Long id) {	
		JsonBean result = new JsonBean();
		GPIUtils.delete(id);
		result.set(GPIEPConstants.RESULT, GPIEPConstants.DELETED);
		return result;		
	}
}
