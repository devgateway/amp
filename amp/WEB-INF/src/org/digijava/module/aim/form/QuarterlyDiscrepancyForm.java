package org.digijava.module.aim.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class QuarterlyDiscrepancyForm extends MainProjectDetailsForm {
	
	private int transactionType;
	private Collection discrepancies;

	/**
	 * @return
	 */
	public Collection getDiscrepancies() {
		return discrepancies;
	}

	/**
	 * @return
	 */
	public int getTransactionType() {
		return transactionType;
	}

	/**
	 * @param collection
	 */
	public void setDiscrepancies(Collection collection) {
		discrepancies = collection;
	}

	/**
	 * @param i
	 */
	public void setTransactionType(int i) {
		transactionType = i;
	}

	public ActionErrors validate(ActionMapping actionMapping,
										 HttpServletRequest httpServletRequest) {
		ActionErrors errors = super.validate(actionMapping, httpServletRequest);
		return errors;
	}	
}	