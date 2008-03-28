

package org.digijava.module.aim.form;

import java.util.List;

/**
 *
 * @author 
 */
public class MonthlyInfoForm extends DetailedInfoForm {

    private List monthlyInfoList;
    private String totalActualCommitment;
    private String totalPlannedDisbursement;
    private String totalActualDisbursement;
    private String totalActualExpenditure;
    private String totalDisbOrder;

    public String getTotalActualCommitment() {
        return totalActualCommitment;
    }

    public void setTotalActualCommitment(String totalActualCommitment) {
        this.totalActualCommitment = totalActualCommitment;
    }

    public String getTotalActualDisbursement() {
        return totalActualDisbursement;
    }

    public void setTotalActualDisbursement(String totalActualDisbursement) {
        this.totalActualDisbursement = totalActualDisbursement;
    }

    public String getTotalActualExpenditure() {
        return totalActualExpenditure;
    }

    public void setTotalActualExpenditure(String totalActualExpenditure) {
        this.totalActualExpenditure = totalActualExpenditure;
    }

    public String getTotalDisbOrder() {
        return totalDisbOrder;
    }

    public void setTotalDisbOrder(String totalDisbOrder) {
        this.totalDisbOrder = totalDisbOrder;
    }

    public String getTotalPlannedDisbursement() {
        return totalPlannedDisbursement;
    }

    public void setTotalPlannedDisbursement(String totalPlannedDisbursement) {
        this.totalPlannedDisbursement = totalPlannedDisbursement;
    }
   

    public List getMonthlyInfoList() {
        return monthlyInfoList;
    }

    public void setMonthlyInfoList(List monthlyInfoList) {
        this.monthlyInfoList = monthlyInfoList;
    }
}
