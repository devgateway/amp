package org.digijava.module.aim.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class YearlyComparisonsForm extends DetailedInfoForm {
	private Collection yearlyComparisons;
	private double totalActualCommitment;
	private double totalPlannedDisbursement;
	private double totalActualDisbursement;
	private double totalActualExpenditure;
	private double totalDisbOrder;
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

	public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
		ActionErrors errors = super.validate(actionMapping, httpServletRequest);
		return errors;
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

	public void setFiscalYears(Collection c) {
		fiscalYears = c;
	}

	public double getTotalActualCommitment() {
		return totalActualCommitment;
	}

	public void setTotalActualCommitment(double totalActualCommitment) {
		this.totalActualCommitment = totalActualCommitment;
	}

	public double getTotalPlannedDisbursement() {
		return totalPlannedDisbursement;
	}

	public void setTotalPlannedDisbursement(double totalPlannedDisbursement) {
		this.totalPlannedDisbursement = totalPlannedDisbursement;
	}

	public double getTotalActualDisbursement() {
		return totalActualDisbursement;
	}

	public void setTotalActualDisbursement(double totalActualDisbursement) {
		this.totalActualDisbursement = totalActualDisbursement;
	}

	public double getTotalActualExpenditure() {
		return totalActualExpenditure;
	}

	public void setTotalActualExpenditure(double totalActualExpenditure) {
		this.totalActualExpenditure = totalActualExpenditure;
	}

	public double getTotalDisbOrder() {
		return totalDisbOrder;
	}

	public void setTotalDisbOrder(double totalDisbOrder) {
		this.totalDisbOrder = totalDisbOrder;
	}

	public Collection getFiscalYears() {
		return fiscalYears;
	}


}
