package org.digijava.module.aim.helper ;

public class TotalsQuarterly
{
	private String totalCommitted ;
	private String totalRemaining ;
	private String totalDisbursed ;
	private String totalUnExpended ;	
	private String currencyCode;
	
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
	/**
	 * @return Returns the currencyCode.
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	/**
	 * @param currencyCode The currencyCode to set.
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
}
