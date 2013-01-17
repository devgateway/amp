package org.digijava.module.aim.helper ;

public class QuarterlyGrouping
{
	private int fiscalYear ;
	private int fiscalQuarter ;
	private String state;	
	
	
	/**
	 * @return
	 */
	public int getFiscalQuarter() {
		return fiscalQuarter;
	}

	/**
	 * @return
	 */
	public int getFiscalYear() {
		return fiscalYear;
	}

	/**
	 * @param i
	 */
	public void setFiscalQuarter(int i) {
		fiscalQuarter = i;
	}

	/**
	 * @param i
	 */
	public void setFiscalYear(int i) {
		fiscalYear = i;
	}
	
	/**
	 * @return
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param string
	 */
	public void setState(String string) {
		state = string;
	}

}