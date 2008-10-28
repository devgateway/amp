package org.digijava.module.aim.helper;

public class YearlyDiscrepancy	{
	
	private int fiscalYear;
	private String donorPlanned;
	private String implAgencyPlanned;
	private String mofedPlanned;
	private String donorActual;
	private String implAgencyActual;
	private String mofedActual;
	
	/**
	 * @return
	 */
	public String getDonorActual() {
		return donorActual;
	}

	/**
	 * @return
	 */
	public String getDonorPlanned() {
		return donorPlanned;
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
	public String getImplAgencyActual() {
		return implAgencyActual;
	}

	/**
	 * @return
	 */
	public String getImplAgencyPlanned() {
		return implAgencyPlanned;
	}

	/**
	 * @return
	 */
	public String getMofedActual() {
		return mofedActual;
	}

	/**
	 * @return
	 */
	public String getMofedPlanned() {
		return mofedPlanned;
	}

	/**
	 * @param string
	 */
	public void setDonorActual(String string) {
		donorActual = string;
	}

	/**
	 * @param string
	 */
	public void setDonorPlanned(String string) {
		donorPlanned = string;
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
	public void setImplAgencyActual(String string) {
		implAgencyActual = string;
	}

	/**
	 * @param string
	 */
	public void setImplAgencyPlanned(String string) {
		implAgencyPlanned = string;
	}

	/**
	 * @param string
	 */
	public void setMofedActual(String string) {
		mofedActual = string;
	}

	/**
	 * @param string
	 */
	public void setMofedPlanned(String string) {
		mofedPlanned = string;
	}

}	