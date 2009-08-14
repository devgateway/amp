package org.digijava.module.aim.helper ;

public class YearlyInfo 
{
	private int fiscalYear;
	private double plannedAmount ;
	private double actualAmount ;
	private String wrapedPlanned;
	private String wrapedActual;
	public String getWrapedActual() {
		return wrapedActual;
	}

	public void setWrapedActual(String wrapedActual) {
		this.wrapedActual = wrapedActual;
	}

	public String getWrapedPlanned() {
		return wrapedPlanned;
	}

	public void setWrapedPlanned(String wrapedPlanned) {
		this.wrapedPlanned = wrapedPlanned;
	}

	/**
	 * @return
	 */
	public double getActualAmount() {
		return actualAmount;
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
	public double getPlannedAmount() {
		return plannedAmount;
	}

	/**
	 * @param string
	 */
	public void setActualAmount(double amount) {
		actualAmount = amount;
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
	public void setPlannedAmount(double amount) {
		plannedAmount = amount;
	}
	
}