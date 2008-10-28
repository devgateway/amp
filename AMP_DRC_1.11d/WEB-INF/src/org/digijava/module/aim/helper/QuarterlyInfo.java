package org.digijava.module.aim.helper ;

public class QuarterlyInfo extends YearlyInfo
{
	private int fiscalQuarter ;
	private String dateDisbursed ;
	/* aggregate set to 0 means its an aggregated record and 
	if set to 1 means it is not an aggregated record 		*/
	private int aggregate;
	private boolean actualAmountSet;
	private boolean plus;
	private boolean display;
	
	/**
	 * @return
	 */
	public String getDateDisbursed() {
		return dateDisbursed;
	}

	/**
	 * @return
	 */
	public int getFiscalQuarter() {
		return fiscalQuarter;
	}

	/**
	 * @param string
	 */
	public void setDateDisbursed(String string) {
		dateDisbursed = string;
	}

	/**
	 * @param i
	 */
	public void setFiscalQuarter(int i) {
		fiscalQuarter = i;
	}
	/**
	 * @return
	 */
	public int getAggregate() {
		return aggregate;
	}

	/**
	 * @param i
	 */
	public void setAggregate(int i) {
		aggregate = i;
	}


	/**
	 * @return
	 */
	public boolean isActualAmountSet() {
		return actualAmountSet;
	}

	/**
	 * @param b
	 */
	public void setActualAmountSet(boolean b) {
		actualAmountSet = b;
	}

	/**
	 * @return
	 */
	public boolean isPlus() {
		return plus;
	}

	/**
	 * @param b
	 */
	public void setPlus(boolean b) {
		plus = b;
	}

	/**
	 * @return
	 */
	public boolean isDisplay() {
		return display;
	}

	/**
	 * @param b
	 */
	public void setDisplay(boolean b) {
		display = b;
	}

}