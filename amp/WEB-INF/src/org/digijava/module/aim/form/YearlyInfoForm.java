package org.digijava.module.aim.form ;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class YearlyInfoForm extends DetailedInfoForm
{
	private Collection yearlyInfo;
	private String totalPlanned ;
	private String totalActual ;
	private Collection discrepancies;
	private Collection fiscalYears;
	private long fiscalCalId;

	/**
	 * @return
	 */
	public String getTotalActual() {
		return totalActual;
	}

	/**
	 * @return
	 */
	public String getTotalPlanned() {
		return totalPlanned;
	}

	/**
	 * @return
	 */
	public Collection getYearlyInfo() {
		return yearlyInfo;
	}

	/**
	 * @param string
	 */
	public void setTotalActual(String string) {
		totalActual = string;
	}

	/**
	 * @param string
	 */
	public void setTotalPlanned(String string) {
		totalPlanned = string;
	}

	/**
	 * @param collection
	 */
	public void setYearlyInfo(Collection collection) {
		yearlyInfo = collection;
	}
	
	public ActionErrors validate(ActionMapping actionMapping,
									 HttpServletRequest httpServletRequest) {
		ActionErrors errors = super.validate(actionMapping, httpServletRequest);
		return errors;
	}	
	
	public Collection getDiscrepancies() {
		return discrepancies;
	}
	
	public void setDiscrepancies(Collection discrepancies) {
		this.discrepancies = discrepancies;
	}

	
	public long getFiscalCalId() {	
		return fiscalCalId;
	}

	/**
	 * @param i
	 */
	public void setFiscalCalId(long i) {
		fiscalCalId = i;
	}

	public void setFiscalYears(Collection c)
	{
		fiscalYears=c;
	}

	public Collection getFiscalYears()
	{
		return fiscalYears;
	}
}