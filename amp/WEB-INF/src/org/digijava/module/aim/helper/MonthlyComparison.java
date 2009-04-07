package org.digijava.module.aim.helper;

import java.math.BigDecimal;

/**
 *
 * @author
 */
public class MonthlyComparison implements Comparable<MonthlyComparison> {

    private String month;
    private int monthNumber; 
    private int fiscalYear;
    private BigDecimal plannedCommitment;
    private BigDecimal actualCommitment;
    private BigDecimal plannedDisbursement;
    private BigDecimal actualDisbursement;
    private BigDecimal plannedExpenditure;
    private BigDecimal actualExpenditure;
    private BigDecimal disbOrders;

    public String getMonth() {
        return month;
    }

    public BigDecimal getActualCommitment() {
        return actualCommitment;
    }

    public void setActualCommitment(BigDecimal actualCommitment) {
        this.actualCommitment = actualCommitment;
    }

    public BigDecimal getActualDisbursement() {
        return actualDisbursement;
    }

    public void setActualDisbursement(BigDecimal actualDisbursement) {
        this.actualDisbursement = actualDisbursement;
    }

    public BigDecimal getActualExpenditure() {
        return actualExpenditure;
    }

    public void setActualExpenditure(BigDecimal actualExpenditure) {
        this.actualExpenditure = actualExpenditure;
    }

    public BigDecimal getDisbOrders() {
        return disbOrders;
    }

    public void setDisbOrders(BigDecimal disbOrders) {
        this.disbOrders = disbOrders;
    }

    public int getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(int fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public BigDecimal getPlannedCommitment() {
        return plannedCommitment;
    }

    public void setPlannedCommitment(BigDecimal plannedCommitment) {
        this.plannedCommitment = plannedCommitment;
    }

    public BigDecimal getPlannedDisbursement() {
        return plannedDisbursement;
    }

    public void setPlannedDisbursement(BigDecimal plannedDisbursement) {
        this.plannedDisbursement = plannedDisbursement;
    }

    public BigDecimal getPlannedExpenditure() {
        return plannedExpenditure;
    }

    public void setPlannedExpenditure(BigDecimal plannedExpenditure) {
        this.plannedExpenditure = plannedExpenditure;
    }

    public void setMonth(String month) {
        this.month = month;
    }

	
	public int compareTo(MonthlyComparison o) {
		if (Integer.valueOf(this.fiscalYear).compareTo(o.fiscalYear) !=0 ) 
			return Integer.valueOf(this.fiscalYear).compareTo(o.fiscalYear);
		else
			return Integer.valueOf(this.monthNumber).compareTo(o.monthNumber);
	}

	public void setMonthNumber(int monthNumber) {
		this.monthNumber = monthNumber;
	}

	public int getMonthNumber() {
		return monthNumber;
	}
}
