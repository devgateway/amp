

package org.digijava.module.aim.form;

import java.util.List;

/**
 *
 * @author 
 */
public class MonthlyInfoForm extends DetailedInfoForm {

    private List monthlyInfoList;
    private Double totalPlanned;
    private Double totalActual;
    private Double totalActualCommitment;
    private Double totalPlannedDisbursement;
    private Double totalActualDisbursement;
    private Double totalActualExpenditure;
    private Double totalPlannedExpenditure;
    private Double totalDisbOrder;

    public Double getTotalActualCommitment() {
        return totalActualCommitment;
    }

    public void setTotalActualCommitment(Double totalActualCommitment) {
        this.totalActualCommitment = totalActualCommitment;
    }

    public Double getTotalActualDisbursement() {
        return totalActualDisbursement;
    }

    public void setTotalActualDisbursement(Double totalActualDisbursement) {
        this.totalActualDisbursement = totalActualDisbursement;
    }

    public Double getTotalActualExpenditure() {
        return totalActualExpenditure;
    }

    public void setTotalActualExpenditure(Double totalActualExpenditure) {
        this.totalActualExpenditure = totalActualExpenditure;
    }

    public Double getTotalDisbOrder() {
        return totalDisbOrder;
    }

    public void setTotalDisbOrder(Double totalDisbOrder) {
        this.totalDisbOrder = totalDisbOrder;
    }

    public Double getTotalPlannedDisbursement() {
        return totalPlannedDisbursement;
    }

    public void setTotalPlannedDisbursement(Double totalPlannedDisbursement) {
        this.totalPlannedDisbursement = totalPlannedDisbursement;
    }
   

    public List getMonthlyInfoList() {
        return monthlyInfoList;
    }

    public void setMonthlyInfoList(List monthlyInfoList) {
        this.monthlyInfoList = monthlyInfoList;
    }

    public Double getTotalActual() {
        return totalActual;
    }

    public void setTotalActual(Double totalActual) {
        this.totalActual = totalActual;
    }

    public Double getTotalPlanned() {
        return totalPlanned;
    }

    public void setTotalPlanned(Double totalPlanned) {
        this.totalPlanned = totalPlanned;
    }

    public Double getTotalPlannedExpenditure() {
        return totalPlannedExpenditure;
    }

    public void setTotalPlannedExpenditure(Double totalPlannedExpenditure) {
        this.totalPlannedExpenditure = totalPlannedExpenditure;
    }
}
