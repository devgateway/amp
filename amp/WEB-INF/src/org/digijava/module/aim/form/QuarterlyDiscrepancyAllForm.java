package org.digijava.module.aim.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class QuarterlyDiscrepancyAllForm extends MainProjectDetailsForm {
	
	private Collection quarterlyDiscrepanciesAll;
	private int transactionType;

	public ActionErrors validate(ActionMapping actionMapping,
										 HttpServletRequest httpServletRequest) {
		ActionErrors errors = super.validate(actionMapping, httpServletRequest);
		return errors;
	}	
	/**
	 * @return
	 */
	public Collection getQuarterlyDiscrepanciesAll() {
		return quarterlyDiscrepanciesAll;
	}

	/**
	 * @param collection
	 */
	public void setQuarterlyDiscrepanciesAll(Collection collection) {
		quarterlyDiscrepanciesAll = collection;
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

}	