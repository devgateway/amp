package org.digijava.module.aim.form ;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class DetailedInfoForm extends MainProjectDetailsForm
{
	private int transactionType ;
	private String commitmentTabColor ;
	private String disbursementTabColor ;
	private String expenditureTabColor ;
	private String totalCommitted ;
	private String totalRemaining ;
	private String totalDisbursed ;
	private String totalUnExpended ;
	private String totalExpended; 
    private String totalDisbOrdered ;
	private String currCode;

	/**
	 * @return
	 */
	public String getCommitmentTabColor() {
		return commitmentTabColor;
	}

	/**
	 * @return
	 */
	public String getDisbursementTabColor() {
		return disbursementTabColor;
	}

	/**
	 * @return
	 */
	public String getExpenditureTabColor() {
		return expenditureTabColor;
	}

/**
	 * @return
	 */
	public int getTransactionType() {
		return transactionType;
	}

	/**
	 * @param string
	 */
	public void setCommitmentTabColor(String string) {
		commitmentTabColor = string;
	}

	/**
	 * @param string
	 */
	public void setDisbursementTabColor(String string) {
		disbursementTabColor = string;
	}

	/**
	 * @param string
	 */
	public void setExpenditureTabColor(String string) {
		expenditureTabColor = string;
	}

	/**
	 * @param i
	 */
	public void setTransactionType(int i) {
		transactionType = i;
	}

	/**
	 * @return
	 */
	public String getTotalCommitted() {
		return totalCommitted;
	}

	/**
	 * @return
	 */
	public String getTotalDisbursed() {
		return totalDisbursed;
	}

	/**
	 * @return
	 */
	public String getTotalRemaining() {
		return totalRemaining;
	}

	/**
	 * @return
	 */
	public String getTotalUnExpended() {
		return totalUnExpended;
	}

	/**
	 * @param string
	 */
	public void setTotalCommitted(String string) {
		totalCommitted = string;
	}

	/**
	 * @param string
	 */
	public void setTotalDisbursed(String string) {
		totalDisbursed = string;
	}

	/**
	 * @param string
	 */
	public void setTotalRemaining(String string) {
		totalRemaining = string;
	}

	/**
	 * @param string
	 */
	public void setTotalUnExpended(String string) {
		totalUnExpended = string;
	}

	public ActionErrors validate(ActionMapping actionMapping,
									 HttpServletRequest httpServletRequest) {
			ActionErrors errors = super.validate(actionMapping, httpServletRequest);
			return errors;
	}
	/**
	 * @return Returns the currCode.
	 */
	public String getCurrCode() {
		return currCode;
	}

        public String getTotalDisbOrdered() {
                return totalDisbOrdered;
        }

        /**
	 * @param currCode The currCode to set.
	 */
	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}

        public void setTotalDisbOrdered(String totalDisbOrdered) {
                this.totalDisbOrdered = totalDisbOrdered;
        }

		public String getTotalExpended() {
			return totalExpended;
		}

		public void setTotalExpended(String totalExpended) {
			this.totalExpended = totalExpended;
		}
}
