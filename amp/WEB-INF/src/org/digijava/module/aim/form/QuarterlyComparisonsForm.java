package org.digijava.module.aim.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class QuarterlyComparisonsForm extends DetailedInfoForm {
	private Collection quarterlyComparisons;
	private Collection quarterlyDiscrepanciesAll;
	private Collection fiscalYears;
	private long fiscalCalId;
	private double totalActualCommitment;
	private double totalPlannedDisbursement;
	private double totalActualDisbursement;
	private double totalActualExpenditure;
	private double totalDisbOrder;

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

	/**
	 * @return
	 */
	public Collection getQuarterlyComparisons() {
		return quarterlyComparisons;
	}

	/**
	 * @param collection
	 */
	public void setQuarterlyComparisons(Collection collection) {
		quarterlyComparisons = collection;
	}

	public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
		ActionErrors errors = super.validate(actionMapping, httpServletRequest);
		return errors;
	}

	public Collection getQuarterlyDiscrepanciesAll() {
		return quarterlyDiscrepanciesAll;
	}

	public void setQuarterlyDiscrepanciesAll(Collection quarterlyDiscrepanciesAll) {
		this.quarterlyDiscrepanciesAll = quarterlyDiscrepanciesAll;
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

	public Collection getFiscalYears() {
		return fiscalYears;
	}
}