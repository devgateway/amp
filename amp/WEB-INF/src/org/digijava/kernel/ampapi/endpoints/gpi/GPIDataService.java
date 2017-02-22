package org.digijava.kernel.ampapi.endpoints.gpi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpGPINiAidOnBudget;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.hibernate.Session;
import org.apache.log4j.Logger;

/**
 * 
 * @author gerald
 *
 */
public class GPIDataService {
	private static Logger logger = Logger.getLogger(GPIDataService.class);

	public static JsonBean saveAidOnBudget(JsonBean data) {
		JsonBean result = null;
		AmpGPINiAidOnBudget aidOnBudget = getAidOnBudget(data);
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			session.saveOrUpdate(aidOnBudget);
		} catch (Exception e) {
			logger.error("Exception from saveAidOnBudget: " + e.getMessage());
		}
		return result;
	}

	public static JsonBean getAidOnBudgetById(Long id) {
		AmpGPINiAidOnBudget aidOnBudget = GPIDbUtils.getAidOnBudgetById(id);
		if (aidOnBudget != null)
			return modelToJsonBean(aidOnBudget);
		else
			return null;
	}

	public static JsonBean getAidOnBudgetList() {
		List<AmpGPINiAidOnBudget> aidOnBudgetList = GPIDbUtils.getAidOnBudgetList();
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
		data.set(GPIEPConstants.FIELD_DONOR_ID, aidOnBudget.getDonorId().getAmpOrgId());
		data.set(GPIEPConstants.FIELD_CURRENCY_CODE, aidOnBudget.getCurrencyId().getCurrencyCode());
		data.set(GPIEPConstants.FIELD_AMOUNT, aidOnBudget.getAmount());
		data.set(GPIEPConstants.FIELD_DATE, FormatHelper.formatDate(aidOnBudget.getDate()));
		return data;
	}

	private static AmpGPINiAidOnBudget getAidOnBudget(JsonBean data) {
		Long id;
		AmpGPINiAidOnBudget aidOnBudget = null;
		if (data.getString(GPIEPConstants.FIELD_ID) != null
				&& NumberUtils.isNumber(data.getString(GPIEPConstants.FIELD_ID))) {
			id = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_ID)));
			aidOnBudget = GPIDbUtils.getAidOnBudgetById(id);
		} else {
			aidOnBudget = new AmpGPINiAidOnBudget();
		}

		if (data.get(GPIEPConstants.FIELD_CURRENCY_CODE) != null) {
			String currencyCode = data.getString((GPIEPConstants.FIELD_CURRENCY_CODE));
			aidOnBudget.setCurrencyId(CurrencyUtil.getAmpcurrency(currencyCode));
		}

		if (data.getString(GPIEPConstants.FIELD_DONOR_ID) != null) {
			Long donorId = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_DONOR_ID)));
			aidOnBudget.setDonorId(GPIDbUtils.getOrganisation(donorId));
		}

		if (data.get(GPIEPConstants.FIELD_AMOUNT) != null) {
			aidOnBudget.setAmount((Double.parseDouble(String.valueOf(data.get(GPIEPConstants.FIELD_AMOUNT)))));
		}

		if (data.getString(GPIEPConstants.FIELD_DATE) != null) {
			Date date = DateTimeUtil.parseDate(data.getString(GPIEPConstants.FIELD_DATE), GPIEPConstants.DATE_FORMAT);
			aidOnBudget.setDate(date);
		}

		return aidOnBudget;
	}
}
