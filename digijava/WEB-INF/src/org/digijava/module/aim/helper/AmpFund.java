package org.digijava.module.aim.helper ;


public class AmpFund
{
	private String commAmount;
	private String disbAmount;
	private String plannedDisbAmount;
	private String unDisbAmount;
	private String expAmount;
	private String commAmountG;
	private String disbAmountG;
	private String expAmountG;
	
	
	/**
	 * @return
	 */
	public String getCommAmount() {
		return commAmount;
	}

	/**
	 * @return
	 */
	public String getCommAmountG() {
		return commAmountG;
	}

	/**
	 * @return
	 */
	public String getDisbAmount() {
		return disbAmount;
	}

	public String getPlannedDisbAmount() {
		return plannedDisbAmount;
	}

	/**
	 * @return
	 */
	public String getDisbAmountG() {
		return disbAmountG;
	}

	/**
	 * @return
	 */
	public String getExpAmount() {
		return expAmount;
	}

	/**
	 * @return
	 */
	public String getExpAmountG() {
		return expAmountG;
	}

	/**
	 * @param string
	 */
	public void setCommAmount(String string) {
		commAmount = string;
	}

	/**
	 * @param string
	 */
	public void setCommAmountG(String string) {
		commAmountG = string;
	}

	/**
	 * @param string
	 */
	public void setDisbAmount(String string) {
		disbAmount = string;
	}

	public String getUnDisbAmount() {
		return unDisbAmount;
	}

	public void setPlannedDisbAmount(String string) {
		plannedDisbAmount = string;
	}

	/**
	 * @param string
	 */
	public void setDisbAmountG(String string) {
		disbAmountG = string;
	}

	/**
	 * @param string
	 */
	public void setExpAmount(String string) {
		expAmount = string;
	}

	/**
	 * @param string
	 */
	public void setExpAmountG(String string) {
		expAmountG = string;
	}

	public void setUnDisbAmount(String string) {
		unDisbAmount = string;
	}
}