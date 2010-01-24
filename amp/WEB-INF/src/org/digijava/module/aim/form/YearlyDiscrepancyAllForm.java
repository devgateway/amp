package org.digijava.module.aim.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class YearlyDiscrepancyAllForm extends MainProjectDetailsForm {
	
	private Collection yearlyDiscrepanciesAll;
	private int transactionType;

	public ActionErrors validate(ActionMapping actionMapping,
										 HttpServletRequest httpServletRequest) {
		ActionErrors errors = super.validate(actionMapping, httpServletRequest);
		return errors;
	}	

	/**
	 * @return
	 */
	public int getTransactionType() {
		return transactionType;
	}

	/**
	 * @param i
	 */
	public void setTransactionType(int i) {
		transactionType = i;
	}

	/**
	 * @return
	 */
	public Collection getYearlyDiscrepanciesAll() {
		return yearlyDiscrepanciesAll;
	}

	/**
	 * @param collection
	 */
	public void setYearlyDiscrepanciesAll(Collection collection) {
		yearlyDiscrepanciesAll = collection;
	}

}	