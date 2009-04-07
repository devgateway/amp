

package org.digijava.module.aim.form;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author 
 */
public class MonthlyInfoForm extends DetailedInfoForm {

    private List monthlyInfoList;
    private BigDecimal totalPlanned;
    private BigDecimal totalActual;
    private BigDecimal totalActualCommitment;
    private BigDecimal totalPlannedDisbursement;
    private BigDecimal totalActualDisbursement;
    private BigDecimal totalActualExpenditure;
    private BigDecimal totalPlannedExpenditure;
    private BigDecimal totalDisbOrder;

    public BigDecimal getTotalActualCommitment() {
        return totalActualCommitment;
    }

    public void setTotalActualCommitment(BigDecimal totalActualCommitment) {
        this.totalActualCommitment = totalActualCommitment;
    }

    public BigDecimal getTotalActualDisbursement() {
        return totalActualDisbursement;
    }

    public void setTotalActualDisbursement(BigDecimal totalActualDisbursement) {
        this.totalActualDisbursement = totalActualDisbursement;
    }

    public BigDecimal getTotalActualExpenditure() {
        return totalActualExpenditure;
    }

    public void setTotalActualExpenditure(BigDecimal totalActualExpenditure) {
        this.totalActualExpenditure = totalActualExpenditure;
    }

    public BigDecimal getTotalDisbOrder() {
        return totalDisbOrder;
    }

    public void setTotalDisbOrder(BigDecimal totalDisbOrder) {
        this.totalDisbOrder = totalDisbOrder;
    }

    public BigDecimal getTotalPlannedDisbursement() {
        return totalPlannedDisbursement;
    }

    public void setTotalPlannedDisbursement(BigDecimal totalPlannedDisbursement) {
        this.totalPlannedDisbursement = totalPlannedDisbursement;
    }
   

    public List getMonthlyInfoList() {
        return monthlyInfoList;
    }

    public void setMonthlyInfoList(List monthlyInfoList) {
        this.monthlyInfoList = monthlyInfoList;
    }

    public BigDecimal getTotalActual() {
        return totalActual;
    }

    public void setTotalActual(BigDecimal totalActual) {
        this.totalActual = totalActual;
    }

    public BigDecimal getTotalPlanned() {
        return totalPlanned;
    }

    public void setTotalPlanned(BigDecimal totalPlanned) {
        this.totalPlanned = totalPlanned;
    }

    public BigDecimal getTotalPlannedExpenditure() {
        return totalPlannedExpenditure;
    }

    public void setTotalPlannedExpenditure(BigDecimal totalPlannedExpenditure) {
        this.totalPlannedExpenditure = totalPlannedExpenditure;
    }
}
