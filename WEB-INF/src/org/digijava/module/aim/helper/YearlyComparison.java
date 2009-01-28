package org.digijava.module.aim.helper;

public class YearlyComparison {
	private int fiscalYear;
	private double plannedCommitment = 0.0;
	private double actualCommitment = 0.0;
	private double plannedDisbursement = 0.0;
	private double actualDisbursement = 0.0;
	private double plannedExpenditure = 0.0;
	private double actualExpenditure = 0.0;
	private double disbOrders = 0.0;

	public int getFiscalYear() {
		return fiscalYear;
	}

	public void setFiscalYear(int fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	public double getPlannedCommitment() {
		return plannedCommitment;
	}

	public void setPlannedCommitment(double plannedCommitment) {
		this.plannedCommitment = plannedCommitment;
	}

	public double getActualCommitment() {
		return actualCommitment;
	}

	public void setActualCommitment(double actualCommitment) {
		this.actualCommitment = actualCommitment;
	}

	public double getPlannedDisbursement() {
		return plannedDisbursement;
	}

	public void setPlannedDisbursement(double plannedDisbursement) {
		this.plannedDisbursement = plannedDisbursement;
	}

	public double getActualDisbursement() {
		return actualDisbursement;
	}

	public void setActualDisbursement(double actualDisbursement) {
		this.actualDisbursement = actualDisbursement;
	}

	public double getPlannedExpenditure() {
		return plannedExpenditure;
	}

	public void setPlannedExpenditure(double plannedExpenditure) {
		this.plannedExpenditure = plannedExpenditure;
	}

	public double getActualExpenditure() {
		return actualExpenditure;
	}

	public void setActualExpenditure(double actualExpenditure) {
		this.actualExpenditure = actualExpenditure;
	}

	public double getDisbOrders() {
		return disbOrders;
	}

	public void setDisbOrders(double disbOrders) {
		this.disbOrders = disbOrders;
	}

}
