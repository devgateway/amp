package org.digijava.module.aim.helper ;

public class YearlyComparison
{
	private int fiscalYear;
	private String plannedCommitment ="";
	private String actualCommitment ="";
	private String plannedDisbursement="" ;
	private String actualDisbursement ="";
	private String plannedExpenditure ="";
	private String actualExpenditure ="";
   private String disbOrders="" ;

	/**
	 * @return
	 */
	public String getActualCommitment() {
		return actualCommitment;
	}

	/**
	 * @return
	 */
	public String getActualDisbursement() {
		return actualDisbursement;
	}

	/**
	 * @return
	 */
	public String getActualExpenditure() {
		return actualExpenditure;
	}

	/**
	 * @return
	 */
	public int getFiscalYear() {
		return fiscalYear;
	}

	/**
	 * @return
	 */
	public String getPlannedCommitment() {
		return plannedCommitment;
	}

	/**
	 * @return
	 */
	public String getPlannedDisbursement() {
		return plannedDisbursement;
	}

	/**
	 * @return
	 */
	public String getPlannedExpenditure() {
		return plannedExpenditure;
	}

        public String getDisbOrders() {
                return disbOrders;
        }

        /**
	 * @param string
	 */
	public void setActualCommitment(String string) {
		actualCommitment = string;
	}

	/**
	 * @param string
	 */
	public void setActualDisbursement(String string) {
		actualDisbursement = string;
	}

	/**
	 * @param string
	 */
	public void setActualExpenditure(String string) {
		actualExpenditure = string;
	}

	/**
	 * @param i
	 */
	public void setFiscalYear(int i) {
		fiscalYear = i;
	}

	/**
	 * @param string
	 */
	public void setPlannedCommitment(String string) {
		plannedCommitment = string;
	}

	/**
	 * @param string
	 */
	public void setPlannedDisbursement(String string) {
		plannedDisbursement = string;
	}

	/**
	 * @param string
	 */
	public void setPlannedExpenditure(String string) {
		plannedExpenditure = string;
	}

        public void setDisbOrders(String disbOrders) {
                this.disbOrders = disbOrders;
        }

}

