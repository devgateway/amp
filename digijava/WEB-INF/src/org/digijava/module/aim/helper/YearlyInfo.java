package org.digijava.module.aim.helper ;

public class YearlyInfo 
{
	private int fiscalYear;
	private String plannedAmount ;
	private String actualAmount ;
	
	/**
	 * @return
	 */
	public String getActualAmount() {
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
	public String getPlannedAmount() {
		return plannedAmount;
	}

	/**
	 * @param string
	 */
	public void setActualAmount(String string) {
		actualAmount = string;
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
	public void setPlannedAmount(String string) {
		plannedAmount = string;
	}

}