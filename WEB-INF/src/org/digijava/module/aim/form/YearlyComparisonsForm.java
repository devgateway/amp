package org.digijava.module.aim.form ;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class YearlyComparisonsForm extends DetailedInfoForm	{
	private Collection yearlyComparisons;
	private String totalActualCommitment;
	private String totalPlannedDisbursement;
	private String totalActualDisbursement;
	private String totalActualExpenditure;
        private String totalDisbOrder;
	private Collection yearlyDiscrepanciesAll;
	private Collection fiscalYears;
	private long fiscalCalId;

	/**
	 * @return
	 */
	public Collection getYearlyComparisons() {
		return yearlyComparisons;
	}

	/**
	 * @param collection
	 */
	public void setYearlyComparisons(Collection collection) {
		yearlyComparisons = collection;
	}

	public ActionErrors validate(ActionMapping actionMapping,
									 HttpServletRequest httpServletRequest) {
			ActionErrors errors = super.validate(actionMapping, httpServletRequest);
			return errors;
	}

	/**
	 * @return
	 */
	public String getTotalActualCommitment() {
		return totalActualCommitment;
	}

	/**
	 * @return
	 */
	public String getTotalActualDisbursement() {
		return totalActualDisbursement;
	}

	/**
	 * @return
	 */
	public String getTotalActualExpenditure() {
		return totalActualExpenditure;
	}

	/**
	 * @return
	 */
	public String getTotalPlannedDisbursement() {
		return totalPlannedDisbursement;
	}

	/**
	 * @param string
	 */
	public void setTotalActualCommitment(String string) {
		totalActualCommitment = string;
	}

	/**
	 * @param string
	 */
	public void setTotalActualDisbursement(String string) {
		totalActualDisbursement = string;
	}

	/**
	 * @param string
	 */
	public void setTotalActualExpenditure(String string) {
		totalActualExpenditure = string;
	}

	/**
	 * @param string
	 */
	public void setTotalPlannedDisbursement(String string) {
		totalPlannedDisbursement = string;
	}

	public Collection getYearlyDiscrepanciesAll() {
		return yearlyDiscrepanciesAll;
	}

	public void setYearlyDiscrepanciesAll(Collection yearlyDiscrepanciesAll) {
		this.yearlyDiscrepanciesAll = yearlyDiscrepanciesAll;
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

        public void setTotalDisbOrder(String totalDisbOrder) {
                this.totalDisbOrder = totalDisbOrder;
        }

        public Collection getFiscalYears()
	{
		return fiscalYears;
	}

        public String getTotalDisbOrder() {
                return totalDisbOrder;
        }
}
