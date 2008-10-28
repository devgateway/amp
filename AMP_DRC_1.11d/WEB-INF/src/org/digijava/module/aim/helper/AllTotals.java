package org.digijava.module.aim.helper;

public class AllTotals {
	private String totalActualCommitment;
	private String totalPlannedDisbursement;
	private String totalActualDisbursement;
	private String totalActualExpenditure;
        private String totalDisbOrder;
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

        public String getTotalDisbOrder() {
                return totalDisbOrder;
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

        public void setTotalDisbOrder(String totalDisbOrder) {
                this.totalDisbOrder = totalDisbOrder;
        }

}
