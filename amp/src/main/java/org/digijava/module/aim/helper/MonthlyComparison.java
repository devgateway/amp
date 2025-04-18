package org.digijava.module.aim.helper;

/**
 *
 * @author
 */
public class MonthlyComparison implements Comparable<MonthlyComparison> {

    private String month;
    private int monthNumber; 
    private int fiscalYear;
    private double plannedCommitment;
    private double actualCommitment;
    private double plannedDisbursement;
    private double actualDisbursement;
    private double plannedExpenditure;
    private double actualExpenditure;
    private double disbOrders;

    public String getMonth() {
        return month;
    }

    public double getActualCommitment() {
        return actualCommitment;
    }

    public void setActualCommitment(double actualCommitment) {
        this.actualCommitment = actualCommitment;
    }

    public double getActualDisbursement() {
        return actualDisbursement;
    }

    public void setActualDisbursement(double actualDisbursement) {
        this.actualDisbursement = actualDisbursement;
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

    public double getPlannedDisbursement() {
        return plannedDisbursement;
    }

    public void setPlannedDisbursement(double plannedDisbursement) {
        this.plannedDisbursement = plannedDisbursement;
    }

    public double getPlannedExpenditure() {
        return plannedExpenditure;
    }

    public void setPlannedExpenditure(double plannedExpenditure) {
        this.plannedExpenditure = plannedExpenditure;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @Override
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
